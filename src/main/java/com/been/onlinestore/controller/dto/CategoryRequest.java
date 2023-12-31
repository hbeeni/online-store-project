package com.been.onlinestore.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.been.onlinestore.service.dto.request.CategoryServiceRequest;

public record CategoryRequest() {

	public record Create(
		@NotBlank @Size(max = 20)
		String name,

		@Size(max = 100)
		String description
	) {

		public CategoryServiceRequest.Create toServiceRequest() {
			return CategoryServiceRequest.Create.of(
				name,
				description
			);
		}
	}

	public record Update(
		@NotBlank @Size(max = 20)
		String name,

		@Size(max = 100)
		String description
	) {

		public CategoryServiceRequest.Update toServiceRequest() {
			return CategoryServiceRequest.Update.of(
				name,
				description
			);
		}
	}
}
