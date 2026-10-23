
package IT.Asset.Tracker.servics;

import IT.Asset.Tracker.entity.Asset;
import IT.Asset.Tracker.entity.AssetStatus;
import IT.Asset.Tracker.repository.AssetRepository;
import IT.Asset.Tracker.repository.CategoryRepository;
import IT.Asset.Tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetService {

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public AssetService(
            AssetRepository assetRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Asset> getAssetsByStatus(AssetStatus status) {
        return assetRepository.findByStatus(status);
    }

    public Asset createAsset(
            String name, String serialNumber, Long categoryId) {

        if (assetRepository.existsBySerialNumber(serialNumber)) {
            throw new IllegalArgumentException(
                    "Serial number already exists: " + serialNumber);
        }

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RuntimeException("Category not found: " + categoryId));

        Asset asset = new Asset();
        asset.setName(name);
        asset.setSerialNumber(serialNumber);
        asset.setCategory(category);
        asset.setStatus(AssetStatus.AVAILABLE);

        return assetRepository.save(asset);
    }

    public Asset assignAsset(Long assetId, Long userId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new RuntimeException("Asset not found: " + assetId));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Only available assets can be assigned");
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userId));

        asset.setAssignedTo(user);
        asset.setStatus(AssetStatus.ASSIGNED);

        return assetRepository.save(asset);
    }

    public Asset returnAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new RuntimeException("Asset not found: " + assetId));

        if (asset.getStatus() != AssetStatus.ASSIGNED) {
            throw new IllegalStateException(
                    "Only assigned assets can be returned");
        }

        asset.setAssignedTo(null);
        asset.setStatus(AssetStatus.AVAILABLE);

        return assetRepository.save(asset);
    }

    public void deleteAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new RuntimeException("Asset not found: " + assetId));

        assetRepository.delete(asset);
    }
@Transactional(readOnly = true)
public IT.Asset.Tracker.entity.User getUserByUsername(String username) {
    return userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new RuntimeException("User not found: " + username));
}
@Transactional(readOnly = true)
public List<Asset> getMyAssets(Long userId) {
    return assetRepository.findAll().stream()
            .filter(asset -> asset.getAssignedTo() != null
                    && asset.getAssignedTo().getId().equals(userId))
            .collect(java.util.stream.Collectors.toList());
}

}

