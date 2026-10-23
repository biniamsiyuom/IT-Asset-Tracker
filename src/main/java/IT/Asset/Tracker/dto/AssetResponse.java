
package IT.Asset.Tracker.dto;

import IT.Asset.Tracker.entity.Asset;
import IT.Asset.Tracker.entity.AssetStatus;
import java.time.LocalDateTime;

public record AssetResponse(
    Long id,
    String name,
    String serialNumber,
    AssetStatus status,
    Long categoryId,
    String categoryName,
    Long assignedToId,
    String assignedToUsername,
    LocalDateTime createdAt
) {
    public static AssetResponse from(Asset asset) {
        return new AssetResponse(
            asset.getId(),
            asset.getName(),
            asset.getSerialNumber(),
            asset.getStatus(),
            asset.getCategory().getId(),
            asset.getCategory().getName(),
            asset.getAssignedTo() == null
                ? null
                : asset.getAssignedTo().getId(),
            asset.getAssignedTo() == null
                ? null
                : asset.getAssignedTo().getUsername(),
            asset.getCreatedAt()
        );
    }
}
