<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/store/map.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script
	src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script
	src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=74230d98e8914fe7ac0b3ba58360da62&libraries=services"></script>
<script>
	$(function(){
	    $(document).ajaxSend(function(e, xhr, opt){
	        var token = $("meta[name='_csrf']").attr("content");
	        var header = $("meta[name='_csrf_header']").attr("content");

	        xhr.setRequestHeader(header, token);
	    });
	    
	    let currentPage = 1;
	    const pageSize = 6;
	    let scrollPage = 1;
	    let totalDataCount = 0;
	    let loadedDataCount = 0;
	    let loading = false;
	    const maxItemsPerPage = 30;

	    function loadStores(pageNum) {
	        if (loading) return;
	        loading = true;

	        $.ajax({
	            url: '/web/store/map/list',
	            type: 'GET',
	            data: {
	                pageNum: pageNum,
	                keyword: $('#keywordInput').val()
	            },
	            success: function(response) {
	                appendStoresToPage(response.recentStores);
	                totalDataCount = response.totalStoresCount;
	                updatePagination(totalDataCount, pageNum);
	                loading = false;
	            },
	            error: function(xhr, status, error) {
	                console.error('Data load failed', status, error);
	                loading = false;
	            }
	        });
	    }

	    loadStores(currentPage);

	    function updatePagination(totalDataCount, currentPage) {
	        const totalPages = Math.ceil(totalDataCount / maxItemsPerPage);

	        $('#pagination').empty();

	        for (let i = 1; i <= totalPages; i++) {
	            const pageButton = $('<button>')
	                .text(i)
	                .on('click', function() {
	                    if (i === 1) {
	                        currentPage = 1;
	                        loadedDataCount = 0;
	                        scrollPage = 1;
	                    } else {
	                        currentPage = (i - 1) * 6;
	                        loadedDataCount = (currentPage - 2) * pageSize;
	                        scrollPage = currentPage;
	                    }
	                    $('#storeList').empty();
	                    loadStores(currentPage);
	                });

	            $('#pagination').append(pageButton);
	        }

	        $('#currentPage').text(currentPage);
	    }

	    function appendStoresToPage(stores) {
	        if (Array.isArray(stores)) {
	            stores.forEach(function(store) {
	                if (loadedDataCount >= maxItemsPerPage) {
	                    return;
	                }

	                const storeHtml = 
	                    '<div class="store">' +
	                        '<h3>' + store.storeName + '</h3>' +
	                        '<p>' + (store.description && store.description.trim() !== '' ? store.description : '작성된 소개 글이 없습니다.') + '</p>' +
	                    '</div>';

	                $('#storeList').append(storeHtml);
	                loadedDataCount += 1;
	            });

	            if (loadedDataCount >= totalDataCount || loadedDataCount >= maxItemsPerPage) {
	                $(document).off('scroll');
	            }
	        } else {
	            console.error('Invalid stores data:', stores);
	        }
	    }

	    $('#storeList').on('scroll', function() {
	        if (($(this).scrollTop() + $(this).height() >= $(this)[0].scrollHeight - 50) && loadedDataCount % 6 == 0) {
	            if (loadedDataCount < totalDataCount && !loading && loadedDataCount < maxItemsPerPage) {
	                const nextPage = scrollPage + 1;

	                loadStores(nextPage);
	                scrollPage = nextPage;
	            }
	        }

	        if (loadedDataCount >= totalDataCount || loadedDataCount >= maxItemsPerPage) {
	            $(document).off('scroll');
	        }
	    });

	    $('#searchButton').click(function() {
	        currentPage = 1;
	        loadedDataCount = 0;
	        scrollPage = 1;
	        $('#storeList').empty();
	        loadStores(currentPage);
	    });
	    
	    $('#keywordInput').keyup(function(){
	    	if(event.keyCode == 13) {
	    		$('#searchButton').trigger('click');
	    	}
	    });

		
		navigator.geolocation.getCurrentPosition((position) => {
			const latitude = position.coords.latitude;
			const longitude = position.coords.longitude;
			console.log("위도 : " + latitude);
			console.log("경도 : " + longitude);
			console.log(position);

			// 위치 정보를 받은 후 지도 설정
			let mapContainer = document.getElementById('map'),
			    mapOption = { 
			        center: new kakao.maps.LatLng(latitude, longitude), // 지도의 초기 좌표
			        level: 3 // 줌 레벨
			    };

			// 지도 생성
			let map = new kakao.maps.Map(mapContainer, mapOption); 
			
			// 지오코더
			var geocoder = new kakao.maps.services.Geocoder();
			
			// 마커
			var markerPosition  = new kakao.maps.LatLng(latitude, longitude); 

			// 마커 생성
			var marker = new kakao.maps.Marker({
			    position: markerPosition
			});
			
			// 주소 검색
			geocoder.addressSearch('전남 완도군 청산면 청계리 429', function(result, status) {

			    // 정상적으로 검색이 완료됐으면 
			     if (status === kakao.maps.services.Status.OK) {

			        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

			        // 결과값으로 받은 위치를 마커로 표시합니다
			        var marker = new kakao.maps.Marker({
			            map: map,
			            position: coords
			        });

			        // 인포윈도우로 장소에 대한 설명을 표시합니다
			        var infowindow = new kakao.maps.InfoWindow({
			            content: '<div style="width:150px;text-align:center;padding:6px 0;">우리회사</div>'
			        });
			        infowindow.open(map, marker);

			        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
			        map.setCenter(coords);
			    } 
			});    
			// 마커 표시
			marker.setMap(map);
		}, (error) => {
			console.error("위치 정보를 가져오는 데 실패했습니다.", error);
		});
	});
</script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<div id="mapController" class="exceptionHeader">
				<input type="text" id="keywordInput" placeholder="Search...">
				<button id="searchButton">Search</button>
				<div id="storeList"></div>
				<div id="pagination"></div>
			</div>
			<div id="map" class="exceptionHeader"></div>
		</div>
	</div>
</body>
</html>