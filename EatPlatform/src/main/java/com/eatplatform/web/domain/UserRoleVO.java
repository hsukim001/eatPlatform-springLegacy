package com.eatplatform.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRoleVO {
	private int roleId;
	private String userId;
	private String roleName;
}
