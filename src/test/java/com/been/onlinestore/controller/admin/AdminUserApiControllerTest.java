package com.been.onlinestore.controller.admin;

import static com.been.onlinestore.controller.restdocs.FieldDescription.*;
import static com.been.onlinestore.controller.restdocs.RestDocsUtils.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static java.time.LocalDateTime.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import com.been.onlinestore.config.TestSecurityConfig;
import com.been.onlinestore.controller.restdocs.RestDocsSupport;
import com.been.onlinestore.controller.restdocs.TagDescription;
import com.been.onlinestore.domain.constant.RoleType;
import com.been.onlinestore.service.admin.AdminUserService;
import com.been.onlinestore.service.dto.response.UserResponse;

@DisplayName("API 컨트롤러 - 회원 (관리자)")
@Import(TestSecurityConfig.class)
@WebMvcTest(AdminUserApiController.class)
class AdminUserApiControllerTest extends RestDocsSupport {

	@MockBean
	AdminUserService adminUserService;

	@DisplayName("[API][GET] 회원 리스트 조회 + 페이징")
	@Test
	void test_getUsers_withPagination() throws Exception {
		//Given
		String sortName = "name";
		String direction = "asc";
		int pageNumber = 0;
		int pageSize = 10;

		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc(sortName)));
		UserResponse userResponse1 = UserResponse.of(
			1L,
			"soo",
			"$2a$10$wcVfFiEQnqu3WjgyiIsPzuqdYKV9WJ08Wx.4aac0e08CLFpUjvoW6",
			"김철수",
			"soo@mail.com",
			"철수",
			"01011111111",
			RoleType.USER,
			LocalDateTime.now().minusDays(3),
			now()
		);
		UserResponse userResponse2 = UserResponse.of(
			2L,
			"hee",
			"$2a$10$wcVfFiEQnqu3WjgyiIsPzuqdYKV9WJ08Wx.4aac0e08CLFpUjvoW6",
			"김영희",
			"hee@mail.com",
			"영희",
			"01022222222",
			RoleType.USER,
			now().minusDays(30),
			now().minusDays(22)
		);
		List<UserResponse> content = List.of(userResponse1, userResponse2);
		Page<UserResponse> page = new PageImpl<>(content, pageable, content.size());

		given(adminUserService.findUsers(pageable)).willReturn(page);

		//When & Then
		mvc.perform(
				get("/api/admin/users")
					.queryParam("page", String.valueOf(pageNumber))
					.queryParam("size", String.valueOf(pageSize))
					.queryParam("sort", sortName + "," + direction)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.status").value("success"))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].id").value(userResponse1.id()))
			.andExpect(jsonPath("$.data[0].uid").value(userResponse1.uid()))
			.andExpect(jsonPath("$.data[0].name").value(userResponse1.name()))
			.andExpect(jsonPath("$.data[0].password").doesNotExist())
			.andExpect(jsonPath("$.page.number").value(page.getNumber()))
			.andExpect(jsonPath("$.page.size").value(page.getSize()))
			.andExpect(jsonPath("$.page.totalPages").value(page.getTotalPages()))
			.andExpect(jsonPath("$.page.totalElements").value(page.getTotalElements()))
			.andDo(document(
				"admin/user/getUsers",
				adminApiDescription(
					TagDescription.USER,
					"회원 목록 페이징 조회",
					"전체 회원을 페이지 단위로 조회합니다."
				),
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(PAGE_REQUEST_PARAM),
				responseFields(
					STATUS,
					fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
						.description(USER_ID.getDescription()),
					fieldWithPath("data[].uid").type(JsonFieldType.STRING)
						.description(USER_UID.getDescription()),
					fieldWithPath("data[].name").type(JsonFieldType.STRING)
						.description(USER_NAME.getDescription()),
					fieldWithPath("data[].email").type(JsonFieldType.STRING)
						.description(USER_EMAIL.getDescription()),
					fieldWithPath("data[].nickname").type(JsonFieldType.STRING)
						.description(USER_NICKNAME.getDescription()),
					fieldWithPath("data[].phone").type(JsonFieldType.STRING)
						.description(USER_PHONE.getDescription()),
					fieldWithPath("data[].roleType").type(JsonFieldType.STRING)
						.description(USER_ROLE_TYPE.getDescription()),
					fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
						.description(USER_CREATED_AT.getDescription()),
					fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING)
						.description(USER_MODIFIED_AT.getDescription())
				).and(PAGE_INFO)
			));
		then(adminUserService).should().findUsers(pageable);
	}

	@DisplayName("[API][GET] 회원 상세 조회")
	@Test
	void test_getUser() throws Exception {
		//Given
		long id = 1L;
		String uid = "soo";
		UserResponse response = UserResponse.of(
			id,
			uid,
			"$2a$10$wcVfFiEQnqu3WjgyiIsPzuqdYKV9WJ08Wx.4aac0e08CLFpUjvoW6",
			"김철수",
			"soo@mail.com",
			"철수",
			"01011111111",
			RoleType.USER,
			LocalDateTime.now().minusDays(3),
			now()
		);

		given(adminUserService.findUser(id)).willReturn(response);

		//When & Then
		mvc.perform(get("/api/admin/users/{userId}", id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.status").value("success"))
			.andExpect(jsonPath("$.data.id").value(response.id()))
			.andExpect(jsonPath("$.data.uid").value(response.uid()))
			.andExpect(jsonPath("$.data.name").value(response.name()))
			.andExpect(jsonPath("$.data.password").doesNotExist())
			.andDo(document(
				"admin/user/getUser",
				adminApiDescription(
					TagDescription.USER,
					"회원 상세 조회",
					"회원 ID(userId)로 회원의 정보를 조회합니다."
				),
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("userId").description(USER_ID.getDescription())
				),
				responseFields(
					STATUS,
					fieldWithPath("data.id").type(JsonFieldType.NUMBER)
						.description(USER_ID.getDescription()),
					fieldWithPath("data.uid").type(JsonFieldType.STRING)
						.description(USER_UID.getDescription()),
					fieldWithPath("data.name").type(JsonFieldType.STRING)
						.description(USER_NAME.getDescription()),
					fieldWithPath("data.email").type(JsonFieldType.STRING)
						.description(USER_EMAIL.getDescription()),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING)
						.description(USER_NICKNAME.getDescription()),
					fieldWithPath("data.phone").type(JsonFieldType.STRING)
						.description(USER_PHONE.getDescription()),
					fieldWithPath("data.roleType").type(JsonFieldType.STRING)
						.description(USER_ROLE_TYPE.getDescription()),
					fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
						.description(USER_CREATED_AT.getDescription()),
					fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING)
						.description(USER_MODIFIED_AT.getDescription())
				)
			));
		then(adminUserService).should().findUser(id);
	}
}
