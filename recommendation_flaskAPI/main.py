import pandas as pd
from konlpy.tag import Okt
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler
import oracledb
from flask import Flask, request, jsonify
from flask_cors import CORS

# Oracle Instant Client 설정
oracledb.init_oracle_client(lib_dir="C:/oracle/instantclient_11_2")

# Flask 서버 설정
app = Flask(__name__)
CORS(app)  # CORS 설정 추가

# 한글 형태소 분석기 초기화
okt = Okt()

# 불용어 리스트
stopwords = ["이", "가", "은", "는", "을", "를", "의", "에", "에서", "도", "와", "과",
             "하", "있", "없", "되", "아", "어", "것", "사람", "그런", "이런", "저런", "그", "저"]


def get_data():
    """ 🔹 Oracle에서 데이터 불러와서 처리하는 함수 """
    print("🔍 데이터베이스 연결 중...")
    connection = oracledb.connect(user="eatroot", password="1234", dsn="192.168.0.166:1521/xe")
    curs = connection.cursor()

    # 🔹 데이터 조회
    query_get_store = "SELECT S.*, C.MAIN_CATEGORY_NAME FROM STORE S INNER JOIN STORE_CATEGORY C ON S.STORE_ID = C.STORE_ID"
    query_get_review = "SELECT * FROM REVIEW"
    query_get_reserv = "SELECT RESERV_ID, STORE_ID, USER_ID FROM RESERV"
    query_get_store_address = "SELECT STORE_ID, ROAD_ADDRESS, DETAIL_ADDRESS FROM STORE_ADDRESS"
    query_get_store_image = "SELECT STORE_IMAGE_ID, STORE_ID, STORE_IMAGE_EXTENSION FROM STORE_IMAGE"

    curs.execute(query_get_store)
    store_data = curs.fetchall()
    curs.execute(query_get_review)
    review_data = curs.fetchall()
    curs.execute(query_get_reserv)
    reserv_data = curs.fetchall()
    curs.execute(query_get_store_address)
    store_address_data = curs.fetchall()
    curs.execute(query_get_store_image)
    store_image_data = curs.fetchall()

    # 🔹 Pandas DataFrame 변환
    store_columns = ["STORE_ID", "STORE_USER_ID", "STORE_NAME", "STORE_PHONE", "OWNER_NAME",
                     "RESERV_LIMIT", "SEAT", "BUSINESS_HOUR", "STORE_COMMENT", "DESCRIPTION",
                     "STORE_REG_DATE", "STORE_UPDATE_DATE", "SCORE", "MAIN_CATEGORY_NAME"]
    review_columns = ["REVIEW_ID", "STORE_ID", "USER_ID", "REVIEW_STAR", "REVIEW_CONTENT",
                      "REVIEW_TAG", "REVIEW_DATE", "REVIEW_LIKE", "REVIEW_REPORT", "REVIEW_UPDATE_DATE"]
    reserv_columns = ["RESERV_ID", "STORE_ID", "USER_ID"]
    store_address_columns = ["STORE_ID", "ROAD_ADDRESS", "DETAIL_ADDRESS"]
    store_image_columns = ["STORE_IMAGE_ID", "STORE_ID", "STORE_IMAGE_EXTENSION"]

    df_store = pd.DataFrame(store_data, columns=store_columns)
    df_review = pd.DataFrame(review_data, columns=review_columns)
    df_reserv = pd.DataFrame(reserv_data, columns=reserv_columns)
    df_store_address = pd.DataFrame(store_address_data, columns=store_address_columns)
    df_store_image = pd.DataFrame(store_image_data, columns=store_image_columns)

    # 🔹 리뷰 데이터 그룹화
    df_review_grouped = df_review.groupby("STORE_ID")["REVIEW_CONTENT"].agg(list).reset_index()

    # 🔹 store 데이터와 병합
    df = pd.merge(df_store, df_review_grouped, on="STORE_ID", how="left")
    df = pd.merge(df, df_store_address, on="STORE_ID", how="left")
    df = pd.merge(df, df_store_image, on="STORE_ID", how="left")

    # 텍스트 전처리
    def preprocess(text_list):
        if isinstance(text_list, float):  # NaN 값 처리
            return ""
        full_text = " ".join(text_list)
        tokens = okt.pos(full_text, stem=True)
        filtered_tokens = [word for word, pos in tokens if pos in ["Noun", "Adjective"] and word not in stopwords]
        return " ".join(filtered_tokens)

    df["processed_review"] = df["REVIEW_CONTENT"].apply(preprocess)

    # TF-IDF 벡터화 (리뷰 텍스트)
    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(df["processed_review"])
    text_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

    # One-Hot Encoding (카테고리)
    encoder = OneHotEncoder()
    category_matrix = encoder.fit_transform(df[["MAIN_CATEGORY_NAME"]])
    category_sim = cosine_similarity(category_matrix, category_matrix)

    # MinMax Scaling (평균 평점)
    scaler = MinMaxScaler()
    df["normalized_rating"] = scaler.fit_transform(df[["SCORE"]])

    # DB 연결 종료
    curs.close()
    connection.close()
    print("🔌 데이터베이스 연결 종료!")

    return df, text_sim, category_sim, df_reserv, df_review


@app.route('/recommend', methods=['POST'])
def recommend():
    try:
        print("🔹 추천 요청 수신됨!")
        df, text_sim, category_sim, df_reserv, df_review = get_data()
        data = request.get_json()
        userId = int(data.get('userId'))
        print(f"📌 요청된 userId: {userId}")

        user_reservations = df_reserv[df_reserv["USER_ID"] == userId]
        recommended_store_ids = set()  # 🔹 중복 방지용 집합
        ranked_recommendations = []
        rank = 0

        if user_reservations.empty:
            print("⚠️ 해당 유저의 예약 내역이 없습니다. 리뷰 수와 별점 기준으로 추천합니다.")

            # 🔹 리뷰 개수가 많고 평점이 높은 가게 찾기
            review_counts = df_review.groupby("STORE_ID").size().reset_index(name="REVIEW_COUNT")
            avg_scores = df.groupby("STORE_ID")["SCORE"].mean().reset_index()

            # 🔹 리뷰 개수 + 평점 기준 정렬 (리뷰 개수 → 평점 높은 순)
            top_reviewed_stores = pd.merge(review_counts, avg_scores, on="STORE_ID")
            top_reviewed_stores = top_reviewed_stores.sort_values(by=["REVIEW_COUNT", "SCORE"],
                                                                  cending=[False, False]).head(3)

            for row in top_reviewed_stores.itertuples():
                STORE_ID = row.STORE_ID
                if STORE_ID in recommended_store_ids:
                    continue

                store_info = df[df["STORE_ID"] == STORE_ID].iloc[0]
                recommended_store_ids.add(STORE_ID)

                STORE_NAME = store_info["STORE_NAME"]
                STORE_PHONE = store_info["STORE_PHONE"] if pd.notna(store_info["STORE_PHONE"]) else "전화번호 없음"
                STORE_COMMENT = store_info["STORE_COMMENT"] if pd.notna(store_info["STORE_COMMENT"]) else "코멘트 없음"
                ROAD_ADDRESS = store_info["ROAD_ADDRESS"] if pd.notna(store_info["ROAD_ADDRESS"]) else "주소 없음"
                DETAIL_ADDRESS = store_info["DETAIL_ADDRESS"] if pd.notna(store_info["DETAIL_ADDRESS"]) else "상세 주소 없음"
                STORE_IMAGE = store_info["STORE_IMAGE_EXTENSION"] if pd.notna(
                    store_info["STORE_IMAGE_EXTENSION"]) else "이미지 없음"

                ranked_recommendations.append(
                    f"{rank}. {STORE_NAME} (ID: {STORE_ID}, 전화번호: {STORE_PHONE}, "
                    f"코멘트: {STORE_COMMENT}, 주소: {ROAD_ADDRESS}, 상세 주소: {DETAIL_ADDRESS}, "
                    f"이미지: {STORE_IMAGE}, 유사도 점수: {0.00})"
                )
                print(f"🔹 추천 {rank}: {ranked_recommendations[-1]}")
                rank += 1
        else:
            print(f"✅ 유저 가게 예약 내역: {user_reservations}")

            most_reserved_store = int(user_reservations["STORE_ID"].value_counts().idxmax())
            print(f"✅ 가장 많이 예약한 가게 ID: {most_reserved_store}")

            indices = df[df["STORE_ID"] == most_reserved_store].index.tolist()
            if not indices:
                return jsonify({"message": "해당 ID의 가게가 없습니다."})

            sim_scores = []
            for index in range(len(df)):
                STORE_ID = int(df.iloc[index]["STORE_ID"])
                if STORE_ID in recommended_store_ids:  # 🔹 중복된 가게 필터링
                    continue

                text_sim_score = text_sim[indices[0], index]
                category_sim_score = category_sim[indices[0], index]
                rating_sim_score = 1 - abs(
                    df.iloc[index]["normalized_rating"] - df.iloc[indices[0]]["normalized_rating"])
                final_score = (text_sim_score * 0.4) + (category_sim_score * 0.5) + (rating_sim_score * 0.1)

                STORE_NAME = df.iloc[index]["STORE_NAME"]

                sim_scores.append((index, STORE_ID, STORE_NAME, final_score))
                recommended_store_ids.add(STORE_ID)  # 🔹 중복 방지용으로 추가

            # 🔹 유사도 순 정렬
            sim_scores = sorted(sim_scores, key=lambda x: x[3], reverse=True)[:6]  # 상위 5개만 선택

            # 🔹 가장 많이 예약한 가게 제외
            sim_scores = [score for score in sim_scores if score[1] != most_reserved_store]

            # 🔹 유사도 점수와 가게 정보 출력 (5개만)
            print("🔹 유사도 점수 TOP 5 리스트:")
            for rank, (index, store_id, store_name, score) in enumerate(sim_scores, 1):
                print(f"{rank}. {store_name} (ID: {store_id}, 유사도 점수: {score:.2f})")

            for rank, (index, store_id, store_name, score) in enumerate(sim_scores, 1):
                store_info = df.iloc[index]
                if store_id == most_reserved_store:
                    continue

                recommended_store_ids.add(STORE_ID)
                STORE_NAME = store_info["STORE_NAME"]
                STORE_PHONE = store_info["STORE_PHONE"] if pd.notna(store_info["STORE_PHONE"]) else "전화번호 없음"
                STORE_COMMENT = store_info["STORE_COMMENT"] if pd.notna(store_info["STORE_COMMENT"]) else "코멘트 없음"
                ROAD_ADDRESS = store_info["ROAD_ADDRESS"] if pd.notna(store_info["ROAD_ADDRESS"]) else "주소 없음"
                DETAIL_ADDRESS = store_info["DETAIL_ADDRESS"] if pd.notna(store_info["DETAIL_ADDRESS"]) else "상세 주소 없음"
                STORE_IMAGE = store_info["STORE_IMAGE_EXTENSION"] if pd.notna(
                    store_info["STORE_IMAGE_EXTENSION"]) else "이미지 없음"

                ranked_recommendations.append(
                    f"{rank}. {STORE_NAME} (ID: {STORE_ID}, 전화번호: {STORE_PHONE}, "
                    f"코멘트: {STORE_COMMENT}, 주소: {ROAD_ADDRESS}, 상세 주소: {DETAIL_ADDRESS}, "
                    f"이미지: {STORE_IMAGE}, 유사도 점수: {score:.2f})"
                )

                print(f"🔹 추천 {rank}: {ranked_recommendations[-1]}")
                rank += 1
                if len(ranked_recommendations) >= 3:
                    break

        return "\n".join(ranked_recommendations)

    except Exception as e:
        print(f"❌ 오류 발생: {e}")
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(port=5000, debug=False)