package com.been.onlinestore.controller.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.been.onlinestore.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("categories")
@Controller
public class CategoryController {

	private final ProductService productService;

	@GetMapping("/{categoryId}")
	public String getAllProductsInCategory(@PathVariable Long categoryId,
		@PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		Model model
	) {
		model.addAttribute("products", productService.findProductsInCategoryForUser(categoryId, pageable));
		return "user/products";
	}
}
