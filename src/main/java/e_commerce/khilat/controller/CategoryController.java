package e_commerce.khilat.controller;

import e_commerce.khilat.entity.Category;
import e_commerce.khilat.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {
	

	 @Autowired
	 private  CategoryService categoryService;

	@GetMapping("getAllCategories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

}
