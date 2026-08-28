package e_commerce.khilat.service;

import e_commerce.khilat.entity.Category;
import e_commerce.khilat.entity.SubCategory;
import e_commerce.khilat.repository.CategoryRepo;
import e_commerce.khilat.repository.SubCategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepo subCategoryRepository;
    private final CategoryRepo categoryRepository;
    private final CloudflareR2Service cloudflareR2Service;

    public SubCategoryServiceImpl(SubCategoryRepo subCategoryRepository,
                                   CategoryRepo categoryRepository,
                                   CloudflareR2Service cloudflareR2Service) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.cloudflareR2Service = cloudflareR2Service;
    }

    @Override
    public SubCategory addSubCategoryWithImage(Long categoryId, SubCategory subCategory, MultipartFile image) throws Exception {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        subCategory.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudflareR2Service.uploadImage(image, "subcategories");
            subCategory.setImageUrl(imageUrl);
        }

        return subCategoryRepository.save(subCategory);
    }

    @Override
    public List<SubCategory> addMultipleSubCategories(Long categoryId, List<SubCategory> subCategories) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        subCategories.forEach(sc -> sc.setCategory(category));
        return subCategoryRepository.saveAll(subCategories);
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @Override
    public List<SubCategory> getSubCategoriesByCategory(Long categoryId) {
        return subCategoryRepository.findByCategory_Id(categoryId);
    }

    @Override
    public SubCategory updateSubCategoryWithImage(Long id, SubCategory subCategoryDetails, MultipartFile image) throws Exception {
        SubCategory subCategory = subCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + id));

        subCategory.setName(subCategoryDetails.getName());
        subCategory.setDescription(subCategoryDetails.getDescription());

        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudflareR2Service.uploadImage(image, "subcategories");
            subCategory.setImageUrl(imageUrl);
        }

        return subCategoryRepository.save(subCategory);
    }

    @Override
    public void deleteSubCategory(Long id) {
        if (!subCategoryRepository.existsById(id)) {
            throw new RuntimeException("SubCategory not found with id: " + id);
        }
        subCategoryRepository.deleteById(id);
    }
}