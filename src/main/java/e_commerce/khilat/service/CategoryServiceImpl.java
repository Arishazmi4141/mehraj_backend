package e_commerce.khilat.service;

import e_commerce.khilat.entity.Category;
import e_commerce.khilat.repository.CategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;  


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepository;
    private final CloudflareR2Service cloudflareR2Service;

    public CategoryServiceImpl(CategoryRepo categoryRepository, CloudflareR2Service cloudflareR2Service) {
        this.categoryRepository = categoryRepository;
        this.cloudflareR2Service = cloudflareR2Service;
    }
    
    

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

   
    @Override
    public List<Category> addMultipleCategories(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category updateCategory(Long id, Category categoryDetails) {
        // 1. Find the existing category
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // 2. Update the fields (assuming Category has a 'name' field)
        category.setName(categoryDetails.getName());
        // Add other fields here if necessary, e.g., category.setDescription(...)
        
        // 3. Save and return
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        // Check if exists before deleting to avoid empty result data access exceptions
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category addCategoryWithImage(Category category, MultipartFile image) throws Exception {
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudflareR2Service.uploadImage(image);
            category.setImageUrl(imageUrl);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategoryWithImage(Long id, Category categoryDetails, MultipartFile image) throws Exception {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        // Naya image aaya to hi purani url replace karo, warna existing image url wahi rahega
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudflareR2Service.uploadImage(image);
            category.setImageUrl(imageUrl);
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
}