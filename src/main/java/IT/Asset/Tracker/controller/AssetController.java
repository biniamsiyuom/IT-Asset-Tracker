
package IT.Asset.Tracker.controller;

import IT.Asset.Tracker.dto.AssetRequest;
import IT.Asset.Tracker.dto.AssetResponse;
import IT.Asset.Tracker.entity.Asset;
import IT.Asset.Tracker.entity.AssetStatus;
import IT.Asset.Tracker.servics.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Get all assets, optionally filtered by status
    @GetMapping
    public List<AssetResponse> getAllAssets(
            @RequestParam(required = false) AssetStatus status) {

        List<Asset> assets;

        if (status == null) {
            assets = assetService.getAllAssets();
        } else {
            assets = assetService.getAssetsByStatus(status);
        }

        return assets.stream()
                .map(AssetResponse::from)
                .toList();
    }

    // Create a new asset
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponse createAsset(
            @Valid @RequestBody AssetRequest request) {

        Asset asset = assetService.createAsset(
                request.name(),
                request.serialNumber(),
                request.categoryId()
        );

        return AssetResponse.from(asset);
    }

    // Assign an asset to a user
   @PatchMapping("/{assetId}/assign/{userId}")
    public AssetResponse assignAsset(
            @PathVariable Long assetId,
            @PathVariable Long userId) {

        Asset asset = assetService.assignAsset(assetId, userId);
        return AssetResponse.from(asset);
    }

    // Return an assigned asset
    @PatchMapping("/{assetId}/return")
    public AssetResponse returnAsset(
            @PathVariable Long assetId) {

        Asset asset = assetService.returnAsset(assetId);
        return AssetResponse.from(asset);
    }

    // Get assets assigned to the logged-in user
    @GetMapping("/my-assets")
    public List<AssetResponse> getMyAssets(
            Authentication authentication) {

        var user = assetService.getUserByUsername(
                authentication.getName());

        return assetService.getMyAssets(user.getId())
                .stream()
                .map(AssetResponse::from)
                .toList();
    }

    // Delete an asset
    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}
