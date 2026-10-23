
package IT.Asset.Tracker.dto;

import IT.Asset.Tracker.entity.Category;

public record CategoryResponse(
    Long id,
    String name
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName()
        );
    }
}

