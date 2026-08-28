package e_commerce.khilat.controller;

import e_commerce.khilat.entity.SubCategory;
import e_commerce.khilat.service.SubCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("getAllSubCategories")
    public List<SubCategory> getAllSubCategories() {
        return subCategoryService.getAllSubCategories();
    }

    // Admin panel product-add form mein: category select karte hi yeh call hoga
    @GetMapping("byCategory/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategory(@PathVariable Long categoryId) {
        return subCategoryService.getSubCategoriesByCategory(categoryId);
    }
}