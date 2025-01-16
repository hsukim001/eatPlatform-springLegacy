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
	    
	    let marker, infowindow, overlay;
	    
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
	                appendStoresToPage(response.recentStores, response.storeAddresses);
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

	    // 넘버링 페이징 처리
	    function updatePagination(totalDataCount, currentPage) {
		    const totalPages = Math.ceil(totalDataCount / maxItemsPerPage);
		    const paginationContainer = $('#pagination');
		    paginationContainer.empty();
		
		    let paginationNum = Math.floor(currentPage / 6) + 1;
		
		    let startPage = Math.floor((paginationNum - 1) / 5) * 5 + 1;
		    let endPage = startPage + 4;
		
		    if (endPage > totalPages) {
		        endPage = totalPages;
		    }
		
		    // 페이지 수가 적으면 모든 페이지를 표시
		    if (totalPages <= 5) {
		        for (let i = 1; i <= totalPages; i++) {
		            const pageButton = $('<button>')
		                .text(i)
		                .on('click', function() {
		                    currentPage = ((i-1)* 5) + 1;
		                    loadedDataCount = 0;
		                    scrollPage = currentPage;
		                    $('#storeList').empty();
		                    loadStores(currentPage);
		                });
		            paginationContainer.append(pageButton);
		        }
		    } else {
		        // 마지막 페이지 근처의 예외 처리
		        if (paginationNum <= 3) {
		            let startRange = Math.max(paginationNum - 2, 1);
		            let endRange = Math.min(paginationNum + 2, totalPages);
		            for (let i = startRange; i <= endRange; i++) {
		                const pageButton = $('<button>')
		                    .text(i)
		                    .on('click', function() {
		                        currentPage = ((i-1)* 5) + 1;
		                        loadedDataCount = 0;
		                        scrollPage = currentPage;
		                        $('#storeList').empty();
		                        loadStores(currentPage);
		                    });
		                paginationContainer.append(pageButton);
		            }
		        } else {
		            // 마지막 페이지 근처 처리
		            let startRange = Math.max(paginationNum - 2, 1);
		            let endRange = Math.min(paginationNum + 2, totalPages);
		
		            // 마지막 페이지 근처에서 버튼 범위를 조정
		            if (totalPages - paginationNum < 2) {
		                startRange = totalPages - 4 > 0 ? totalPages - 4 : 1;
		                endRange = totalPages;
		            }
		
		            for (let i = startRange; i <= endRange; i++) {
		                const pageButton = $('<button>')
		                    .text(i)
		                    .on('click', function() {
		                        currentPage = ((i-1)* 5) + 1;
		                        loadedDataCount = 0;
		                        scrollPage = currentPage;
		                        $('#storeList').empty();
		                        loadStores(currentPage);
		                    });
		                paginationContainer.append(pageButton);
		            }
		        }
		
		        // 마지막 페이지와 그 직전 페이지의 버튼을 처리
		        if (currentPage === totalPages * 6 - 6) {
		            // 마지막 페이지에서 버튼 범위 표시: 4, 5, 6, 7, 8
		            let rangeStart = Math.max(totalPages - 4, 1);
		            let rangeEnd = totalPages;
		            for (let i = rangeStart; i <= rangeEnd; i++) {
		                const pageButton = $('<button>')
		                    .text(i)
		                    .on('click', function() {
		                        currentPage = ((i-1)* 5) + 1;
		                        loadedDataCount = 0;
		                        scrollPage = currentPage;
		                        $('#storeList').empty();
		                        loadStores(currentPage);
		                    });
		                paginationContainer.append(pageButton);
		            }
		        }
		    }
		}



	    function appendStoresToPage(stores, storeAddresses) {
	        if (Array.isArray(stores)) {
	            stores.forEach(function(store) {
	                if (loadedDataCount >= maxItemsPerPage) {
	                    return;
	                }

	                const storeAddress = storeAddresses[store.storeId]; // storeId를 키로 주소 가져오기
	                const storeHtml = 
	                    '<div class="store" data-store-id =' + store.storeId +'>' +
	                        '<h3>' + store.storeName + '</h3>' +
	                        '<p> <span class="address_mark">지번 </span> <span class="jibun">' + storeAddress.jibunAddress + '</span></p>' + 
	                        '<p> <span class="address_mark">도로명 </span> <span class="road">' + storeAddress.roadAddress + '</span></p>' + 	                       
	                        '<p class="store_comment">' + (store.storeComment && store.storeComment.trim() !== '' ? store.storeComment : '작성된 소개 글이 없습니다.') + '</p>' + 
	                        '<p class="store_phone">' + store.storePhone + '</p>' + 
	                        '<button><a href="detail?storeId=' + store.storeId + '">상세페이지 이동</a></button>' + 
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

			$('#storeList').on('click', '.store', function(){
				
				let selectedStoreName = $(this).find('h3').text();
				let selectedJibunAddress = $(this).find('.jibun').text();
				let selectedRoadAddress = $(this).find('.road').text();
				let selectedStoreComment = $(this).find('.store_comment').text();
				let selectedStorePhone = $(this).find('.store_phone').text();
				
				if($(this).find('.road').text() !== '' && $(this).find('.road').text() !== null) {
					searchAddress = $(this).find('.road').text();	
				} else {
					searchAddress = $(this).find('.jibun').text();
				}
				// 주소 검색
				geocoder.addressSearch(searchAddress, function(result, status) {

				    // 정상적으로 검색이 완료됐으면 
				     if (status === kakao.maps.services.Status.OK) {
				        
				        if(marker) {
				        	marker.setMap(null);
				        	overlay.setMap(null);
				        }



				        let content =
				        	'<div class="detail_text">' + 
			    				'<p class="store_name higtlight">' +
		           					selectedStoreName +
		            			'<p/>' +
		                        '<p class="store_address">' + 
		                        	'<span class="address_mark">' +
		                       			 '지번' + 
		                       		'</span>' +
		                        	'<span class="jibun">' + 
		                        		selectedJibunAddress +
		                        	'</span>' +
		                       	'</p>' + 
		                        '<p class="store_address">' + 
		                       		'<span class="address_mark">' +
		                        		'도로명' + 
		                        	'</span>' +
		                       		'<span class="road">' + 
		                       			selectedRoadAddress + 
		                       		'</span>' +
		                       	'</p>' + 
		                       	'<p class="store_comment">' + 
		                       		selectedStoreComment + 
		                       	'</p>' + 
		                       	'<p class="store_phone">' + 
		                       		selectedStorePhone + 
		                       	'</p>' + 
				        	'</div>';


					    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
					    
					    // 결과값으로 받은 위치를 마커로 표시합니다
				        marker = new kakao.maps.Marker({
				            map: map,
					        position: coords
					    });
					     
					    map.setCenter(coords);
				        overlay = new kakao.maps.CustomOverlay({
				           	 content: content,
				           	 map: map,
				           	 position: marker.getPosition()       
				         });
				          	
				        kakao.maps.event.addListener(marker, 'click', function() {
				        	overlay.setMap(map);
				        });

				        function closeOverlay() {
				          	overlay.setMap(null);     
				        }
				            	
				    } 
				});    
			});
			
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