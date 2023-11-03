package com.been.onlinestore.controller.admin;

import com.been.onlinestore.controller.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin-api/categories")
@RestController
public class AdminCategoryApiController {

    @PostMapping
    public ResponseEntity<?> addCategory() {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
