package e_commerce.khilat.service;

import e_commerce.khilat.entity.Category;


import java.util.List;

import org.springframework.cache.annotation.CacheEvict;

public interface CategoryService {

	@CacheEvict(value = "categories", allEntries = true)
    Category addCategory(Category category);

    List<Category> addMultipleCategories(List<Category> categories);

    List<Category> getAllCategories();
    
    @CacheEvict(value = "categories", allEntries = true)
    Category updateCategory(Long id, Category category);

    @CacheEvict(value = "categories", allEntries = true)
    void deleteCategory(Long id);
    
    
    
    
    
    
}
