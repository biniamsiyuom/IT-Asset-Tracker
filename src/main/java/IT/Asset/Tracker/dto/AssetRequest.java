
package IT.Asset.Tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AssetRequest(
        @NotBlank(message = "Asset name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Serial number is required")
        @Size(max = 100)
        String serialNumber,

        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be positive")
        Long categoryId
) {
}

