package com.eatplatform.web.persistence;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.NotificationVO;

@Mapper
public interface NotificationMapper {

	List<NotificationVO> findUnreadNotificationsByUsername(String userId);
	int insert(NotificationVO notificationVO);
	int update(String url);
	
}