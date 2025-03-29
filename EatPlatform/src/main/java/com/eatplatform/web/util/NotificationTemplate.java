package com.eatplatform.web.util;

public class NotificationTemplate {

    /**
     * 알림 관련 상수
     *
     */
    public static class Types {
        public static final String ADD_REVIEW = "addReview";
        public static final String ADD_REPLY = "addReply";
        public static final String ADD_RESERV = "addReserv";
        
        public static final String USER_CANCEL_RESERV = "userCancelReserv";
        public static final String STORE_CANCEL_RESERV = "storeCancelReserv";
        
        public static final String STORE_APPROVED = "storeApproved";
        public static final String STORE_REJECTED = "storeRejected";
        
        public static final String BUSINESS_APPROVED = "businessApproved";
        public static final String BUSINESS_REJECTED = "businessRejected";
    }

    /**
     * 알림 메시지 관련 상수
     *
     */
    public static class Messages {
        public static final String ADD_REVIEW = "'%s'에 리뷰가 등록되었습니다.";
        public static final String ADD_REPLY = "'%s' 리뷰에 댓글이 등록되었습니다.";
        public static final String ADD_RESERV = "'%s'의 '%s %s' 예약되었습니다.";
        
        public static final String CANCEL_RESERV = "'%s'의 '%s %s' 예약이 취소되었습니다.";
        public static final String STORE_CANCEL_RESERV = "'%s'의 '%s' 예약이 취소되었습니다.";
        
        public static final String STORE_APPROVED = "'%s'의 가게 등록 요청이 승인되었습니다.";
        public static final String STORE_REJECTED = "'%s'의 가게 등록 요청이 거부되었습니다.";
        
        public static final String BUSINESS_APPROVED = "'%s'의 사업자 등록 요청이 거부되었습니다.";
        public static final String BUSINESS_REJECTED = "'%s'의 사업자 등록 요청이 거부되었습니다.";
    }

    /**
     * URL 관련 상수
     *
     */
    public static class Url {
        public static final String STORE_DETAIL_URL = "/store/detail?storeId=";
        public static final String RESERV_LIST_URL = "/reserv/list";
        public static final String STORE_LIST_URL = "/store/list";
        public static final String BUSINESS_REQUEST_URL = "/user/business/requestForm";
    }

}
