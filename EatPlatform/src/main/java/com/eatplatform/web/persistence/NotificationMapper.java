package com.eatplatform.web.persistence;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eatplatform.web.domain.NotificationVO;

@Mapper
public interface NotificationMapper {

	List<NotificationVO> selectNotificationsByReceiver(String receiver);
	int insert(NotificationVO notificationVO);
	int update(NotificationVO notificationVO);
	
}