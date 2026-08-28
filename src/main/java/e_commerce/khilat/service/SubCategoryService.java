package e_commerce.khilat.service;

import e_commerce.khilat.entity.SubCategory;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;

public interface SubCategoryService {

    @CacheEvict(value = "subcategories", allEntries = true)
    SubCategory addSubCategoryWithImage(Long categoryId, SubCategory subCategory, MultipartFile image) throws Exception;

    List<SubCategory> addMultipleSubCategories(Long categoryId, List<SubCategory> subCategories);

    List<SubCategory> getAllSubCategories();

    List<SubCategory> getSubCategoriesByCategory(Long categoryId);

    @CacheEvict(value = "subcategories", allEntries = true)
    SubCategory updateSubCategoryWithImage(Long id, SubCategory subCategory, MultipartFile image) throws Exception;

    @CacheEvict(value = "subcategories", allEntries = true)
    void deleteSubCategory(Long id);
}