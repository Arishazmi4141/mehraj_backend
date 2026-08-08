package e_commerce.khilat.service;

import e_commerce.khilat.entity.Category;


import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {

	@CacheEvict(value = "categories", allEntries = true)
    Category addCategory(Category category);

    List<Category> addMultipleCategories(List<Category> categories);

    List<Category> getAllCategories();
    
    @CacheEvict(value = "categories", allEntries = true)
    Category updateCategory(Long id, Category category);

    @CacheEvict(value = "categories", allEntries = true)
    void deleteCategory(Long id);

    // 🔥 New: category create/update with image (Cloudflare R2)
    @CacheEvict(value = "categories", allEntries = true)
    Category addCategoryWithImage(Category category, MultipartFile image) throws Exception;

    @CacheEvict(value = "categories", allEntries = true)
    Category updateCategoryWithImage(Long id, Category category, MultipartFile image) throws Exception;

    Category getCategoryById(Long id);
    
    
    
    
    
    
}