package com.been.onlinestore.service.dto.request;

import com.been.onlinestore.domain.User;
import com.been.onlinestore.domain.constant.RoleType;

public record UserServiceRequest() {

	public record SignUp(
		String uid,
		String password,
		String name,
		String email,
		String nickname,
		String phone
	) {

		public static SignUp of(String uid, String password, String name, String email, String nickname, String phone) {
			return new SignUp(uid, password, name, email, nickname, phone);
		}

		public User toEntity(String encodedPassword) {
			return User.of(
				uid,
				encodedPassword,
				name,
				email,
				nickname,
				phone,
				RoleType.USER
			);
		}
	}
}
