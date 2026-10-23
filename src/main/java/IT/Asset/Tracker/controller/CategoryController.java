
package IT.Asset.Tracker.controller;

import IT.Asset.Tracker.dto.CategoryRequest;
import IT.Asset.Tracker.dto.CategoryResponse;
import IT.Asset.Tracker.entity.Category;
import IT.Asset.Tracker.servics.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request) {

        Category category =
                categoryService.createCategory(request.name());

        return CategoryResponse.from(category);
    }
}

