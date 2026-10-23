
package IT.Asset.Tracker.servics;

import IT.Asset.Tracker.entity.Category;
import IT.Asset.Tracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with ID: " + id));
    }

    public Category createCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                    "Category already exists: " + name);
        }

        Category category = new Category(name.trim());
        return categoryRepository.save(category);
    }
}
