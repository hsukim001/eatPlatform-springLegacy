<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/store/map.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=74230d98e8914fe7ac0b3ba58360da62&libraries=services&autoload=false"></script>
<script>
	$(function(){
	    $(document).ajaxSend(function(e, xhr, opt){
	        var token = $("meta[name='_csrf']").attr("content");
	        var header = $("meta[name='_csrf_header']").attr("content");

	        xhr.setRequestHeader(header, token);
	    });
		kakao.maps.load(() => {
			 
		    let stores = [];
		    let storeAddresses = [];
		    let markers = [];
		    
		    let currentPage = 1;
		    const pageSize = 6;
		    let scrollPage = 1;
		    let totalDataCount = 0;
		    let loadedDataCount = 0;
		    let loading = false;
		    let pageNum = 1;
		    
		    let marker, infowindow, mapContainer, geocoder, overlay;
		    
		    const maxItemsPerPage = 30;

		    function loadStores(pageNum) {
		        if (loading) return;
		        loading = true;
				let keyword
		        
				if($('#keywordinput').val != null && $('#keywordInput').val() != '') {
					keyword = $('#keywordInput').val();
				} else {
					keywrod = '';
				}        
		        
		        $.ajax({
		            url: '/store/map/list',
		            type: 'GET',
		            data: {
		                pageNum: pageNum,
		                keyword: keyword
		            },
		            success: function(response) {
		                stores = response.recentStores;
		                storeCategory = response.storeCategory;
		                storeAddresses = response.storeAddresses;
		                totalDataCount = response.totalStoresCount;

		                console.log("stores 데이터:", stores);
		                console.log("storeAddresses 데이터:", storeAddresses);
		                
		                appendStoresToPage(stores, storeAddresses);
		                updatePagination(totalDataCount, pageNum);
		                markingToloadData(stores, storeAddresses);
		                loading = false;
		            },
		            error: function(xhr, status, error) {
		                console.error('데이터를 불러오지 못했쪄용', status, error);
		                loading = false;
		            }
		        });
		    }

		    loadStores(currentPage);
		    
		    // 현재 리스트 위치 마킹
			async function markingToloadData(stores, storeAddresses) {
			
			    const startIndex = 0;
			    const endIndex = stores.length;
			
			    console.log('startIndex:', startIndex, 'endIndex:', endIndex);
			    console.log('Total stores:', stores.length);
			
			    let positions = [];
			
			    // 마킹할 데이터 추출
			    for (let i = startIndex; i < endIndex && i < stores.length; i++) {
			        const store = stores[i];
			        console.log('Processing store:', store.storeId, store.storeName); 
			        const geocoder = new kakao.maps.services.Geocoder();
			        const storeAddress = storeAddresses[store.storeId];
			        const jibun = storeAddress ? storeAddress.jibunAddress : null;
			        const road = storeAddress ? storeAddress.roadAddress : null;
			        const storeComment = store.storeComment ? store.storeComment : "작성된 소개글이 없습니다.";
			        const storePhone = store ? store.storePhone : null;
			        const businessHour =  store? store.businessHour : null;
		            let [openHour, closeHour] = businessHour.split(" - ");
			        const storeId = store ? store.storeId : null;
			        let searchAddress;
			
			        if (storeAddress && jibun) {
			            console.log('Address:', jibun);
			
			            try {
			                const result = await new Promise((resolve, reject) => {
			                    geocoder.addressSearch(jibun, function(result, status) {
			                        if (status === kakao.maps.services.Status.OK) {
			                            resolve(result);
			                        } else {
			                            reject('주소를 변환할 수 없습니다.');
			                        }
			                    });
			                });
			
			                console.log('Geocoder result:', result); 
			
			                const latitude = result[0].y;  // 위도
			                const longitude = result[0].x; // 경도
			
			                console.log('Latitude:', latitude, 'Longitude:', longitude);
			
			                if (latitude && longitude) {
			                    positions.push({
			                        title: store.storeName,
			                        latlng: new kakao.maps.LatLng(latitude, longitude),
			                        jibun : jibun,
			                        road : road,
			                        storeComment : storeComment,
			                        storePhone : storePhone,
			                        storeId : storeId,
			                        openHour : openHour,
			                        closeHour : closeHour
			                    });
			                } else {
			                    console.log('위도/경도 값이 유효하지 않음');
			                }
			            } catch (error) {
			                console.error('Geocoder error:', error); 
			            }
			        }
			    }
			
			    // 지도에 마커 추가
			    positions.forEach(function(position) {
			        if (position && position.latlng) {
			        	let markerId = "m" + position.storeId;
			            console.log('마커 추가:', position.title, position.latlng);
			            const marker = new kakao.maps.Marker({
			                map: map,
			                position: position.latlng,
			                title: position.title
			            });

			            const storePhone = position.storePhone;
			            const phoneFormat = storePhone ? storePhone.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`) : "전화번호 없음";
			            markers.push(marker); 
		                overlay = new kakao.maps.CustomOverlay({
		                    content: 
		                    	'<div class="detail_text" id="m' + position.storeId + '">' +
		                    		'<p class="title higtlight">' + position.title + '</p>' +
		                    		'<p class="overlay_address">' + 
		                        		'<span class="address_mark">' +
	                       					 '지번' + 
	                       				'</span>' +
	                       				position.jibun +
	                    			'</p>' +
		                    		'<p class="overlay_address">' + 
			                        	'<span class="address_mark">' +
		                       				 '도로명' + 
		                       			'</span>' +
		                       			position.road +
		                    		'</p>' +
		                    		'<p class="overlay_comment">' + position.storeComment + '</p>' +
		                    		'<p class="overlay_phone">' + phoneFormat + '</p>' +
		                    		'<p class=overlay_hour">영업시간 : <span class="overlay_open_hour">' + position.openHour + '</span> -  <span class="overlay_close_hour">' + position.closeHour + '</span> </p>' +
		                    	'</div>',
		                    map: map,
		                    position: marker.getPosition(),
		                    yAnchor: 1.5
		                });
			            
			            kakao.maps.event.addListener(marker, 'click', function() {
							if(position.jibun !== '' && position.jibun !== null) {
								searchAddress = position.jibun;	
							} else {
								searchAddress = position.road;
							}
							geocoder.addressSearch(searchAddress, function(result, status) {

							     if (status === kakao.maps.services.Status.OK) {
								    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
								    
								    map.setCenter(coords);

							        $('.detail_text').hide();
							        $('#m' + position.storeId).show();
							    } 
							});
			            });
			        } else {
			            console.log('유효하지 않은 위치:', position);
			        }
			    });
		    }

		    // 넘버링 페이징 처리
			function updatePagination(totalDataCount, currentPage) {
			    const totalPages = Math.ceil(totalDataCount / maxItemsPerPage);
			    const paginationContainer = $('#pagination');
			    paginationContainer.empty();
			
			    let startPage = Math.max(pageNum - 2, 1); 
			    let endPage = Math.min(pageNum + 2, totalPages); 
			
			    if (totalPages <= 5) {
			        startPage = 1;
			        endPage = totalPages;
			    } else {
			        if (pageNum <= 2) {
			            startPage = 1;
			            endPage = Math.min(5, totalPages);
			        }
			        if (pageNum >= totalPages - 1) {
			            startPage = Math.max(totalPages - 4, 1);
			            endPage = totalPages;
			        }
			    }
			
			    // 페이지 버튼 생성
			    for (let i = startPage; i <= endPage; i++) {
			        const pageButton = $('<button>')
			            .text(i)
			            .on('click', function() {
			                pageNum = i;
			                currentPage = (i - 1) * 5 + 1;
			                loadedDataCount = 0;
			                scrollPage = currentPage;
			                $('#storeList').empty();
			                claerMarker();
			                loadStores(currentPage);
			
			                updatePagination(totalDataCount, pageNum);
			            });
			
			        if (i === pageNum) {
			            pageButton.addClass('active');
			        }
			
			        paginationContainer.append(pageButton);
			    }
			}

			function appendStoresToPage(stores, storeAddresses) {
			    if ((!stores && !storeAddresses) || stores.length == 0 && loadedDataCount == 0 ) {
			        const noDataHtml = '<div class="no-data"><p>등록된 식당이 없습니다.</p></div>';
			        $('#storeList').append(noDataHtml);
			        return;
			    }

			    if (Array.isArray(stores)) {
			        stores.forEach(function(store) {
			            if (loadedDataCount >= maxItemsPerPage) {
			                return;
			            }

			            let business =  store.businessHour;
			            let [openHour, closeHour] = business.split(" - ");
			            const storeAddress = storeAddresses[store.storeId];
			            const storePhone = store.storePhone;
			            const phoneFormat = storePhone ? storePhone.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`) : "전화번호 없음";
			            const storeHtml = 
			                '<div class="store" data-store-id ="m' + store.storeId +'">' +
			                    '<h3>' + '<a href="detail?storeId=' + store.storeId + '">' + store.storeName + '</a>' + '</h3>' +
			                    '<p class="address"> <span class="address_mark">지번</span> <span class="jibun">' + storeAddress.jibunAddress + '</span></p>' +
			                    '<p class="address"> <span class="address_mark">도로명</span> <span class="road">' + storeAddress.roadAddress + '</span></p>' +
			                    '<p class="store_comment">' + (store.storeComment && store.storeComment.trim() !== '' ? store.storeComment : '작성된 소개 글이 없습니다.') + '</p>' +
			                    '<p class="store_hour"><span class="open_time"> ' + openHour + '</span> - <span class="close_time">' + closeHour + '</span> </p>' +  
			                    '<p class="store_phone">' + '<img src="<%=request.getContextPath()%>/resources/img/common/phone.png" alt="전화 아이콘">' + phoneFormat + '</p>' +
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
		        if ($(this).scrollTop() + $(this).height() >= $(this)[0].scrollHeight - 50) {
		            if (loadedDataCount < totalDataCount && !loading && loadedDataCount % 6 === 0) {
		                const nextPage = scrollPage + 1;
		                loadStores(nextPage);
		                scrollPage = nextPage; 
		            }
		        }

		        if (loadedDataCount >= totalDataCount || loadedDataCount >= maxItemsPerPage) {
		            $(document).off('scroll');
		        }
		    });
		    navigator.geolocation.getCurrentPosition((position) => {
		        const latitude = position.coords.latitude;
		        const longitude = position.coords.longitude;
		        initializeMap(latitude, longitude);
		    }, (error) => {
		        console.error("위치 정보를 가져오는 데 실패했습니다.", error);
		        
		        const defaultLatitude = 37.5053493;
		        const defaultLongitude = 127.0267313;
		        initializeMap(defaultLatitude, defaultLongitude);
		    });

		    function initializeMap(latitude, longitude) {
		        mapContainer = document.getElementById('map'),
		        mapOption = { 
		            center: new kakao.maps.LatLng(latitude, longitude), 
		            level: 3
		        };

		        // 지도 생성
		        map = new kakao.maps.Map(mapContainer, mapOption);  
		        console.log(latitude, longitude);
		        // 지오코더
		        geocoder = new kakao.maps.services.Geocoder();

		        $('#storeList').on('click', '.store', function(){
		            let selectedStoreId = $(this).data('storeId');
		            
		            if($(this).find('.road').text() !== '' && $(this).find('.road').text() !== null) {
		                searchAddress = $(this).find('.road').text();    
		            } else {
		                searchAddress = $(this).find('.jibun').text();
		            }

		            geocoder.addressSearch(searchAddress, function(result, status) {
		                if (status === kakao.maps.services.Status.OK) {
		                    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
		                    
		                    map.setCenter(coords);

		                    $('.detail_text').hide();
		                    $('#' + selectedStoreId).show();
		                } 
		            });    
		        });
		    }
			function claerMarker() {
			    markers.forEach(function(marker) {
			        marker.setMap(null);
			    });
			    markers = [];
                $('.detail_text').hide();
			}
			
			function rebootList() {
		        currentPage = 1;
		        loadedDataCount = 0;
		        scrollPage = 1;
		        $('#storeList').empty();
		        loadStores(currentPage);
			}
			
			const tagData = [
			    { icon: 1, text: "전체보기" },
			    { icon: 2, text: "한식" },
			    { icon: 3, text: "중식" },
			    { icon: 4, text: "일식" },
			    { icon: 5, text: "양식" },
			    { icon: 6, text: "아시안" },
			    { icon: 7, text: "치킨" },
			    { icon: 8, text: "피자" },
			    { icon: 9, text: "패스트푸드" },
			    { icon: 10, text: "카페/디저트" }
			];

			const tagContainer = $('#tagList');

			tagData.forEach(tag => {
			    const tagtItem =
			    	'<div id="tag_icon' + tag.icon +'">' +
	            		'<img src="<%=request.getContextPath()%>/resources/img/store/map/tag_icon_' + tag.icon + '.png" alt="태그_' + tag.text + '">' +
	            		'<span>' + tag.text + '</span>' +
	       			 '</li>';
	       			tagContainer.append(tagtItem);
			}); // End tagData.forEach
			
			$('#tag_icon1').click(function(){
		        $('#keywordInput').val("");
		        claerMarker();
				rebootList();
			});
			$('#tagList > div:not(#tag_icon1)').click(function(){
				$('#tagList > div:not(#tag_icon1)').removeClass('selected');
			    $(this).toggleClass('selected');
			    $('#keywordInput').val($(this).find('span').text());
			    claerMarker();
			    rebootList();
			});

		    $('#searchButton').click(function() {	    
				$('#tagList > div:not(#tag_icon1)').removeClass('selected');	
		        let keyword = $('#keywordInput').val().trim().toLowerCase().replace(/\s+/g, "");
		        let found = false;

		        $('#tagList>div:not(#tag_icon1)').find('span').each(function () {
		            let tagText = $(this).text().trim().toLowerCase().replace(/\s+/g, "");

		            if (
		                (tagText === "카페/디저트" && 
		                 (keyword.includes("카페") || keyword.includes("디저트") || keyword.includes("카페디저트") || keyword.includes("카페/디저트"))
		                ) || keyword.includes(tagText)
		            ) {
		                found = true;
		                $(this).closest('div').addClass('selected');
		                return false;
		            }
		        });
		    	claerMarker();
		    	rebootList();
		    });
		    
		    $('#clearButton').click(function() {
		    	$('#keywordInput').val("");
		    });
		    
		    $('#keywordInput').keyup(function(){
		    	if(event.keyCode == 13) {
		    		$('#searchButton').trigger('click');
		    	} else if (event.keyCode == 27) { 
		            $('#clearButton').trigger('click');
		        }
		    });
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
				<div id= search_wrap>
					<div class="search_box">
						<button id="searchButton"></button>
						<input type="text" id="keywordInput" placeholder="검색어 입력">
						<button id="clearButton">
							<span class="xButton">X</span>
						</button>
					</div>
				</div>
				<div id="tagList"></div>
				<div id="storeList"></div>
				<div id="pagination"></div>
			</div>
			<div id="map" class="exceptionHeader"></div>
		</div>
	</div>
</body>
</html>