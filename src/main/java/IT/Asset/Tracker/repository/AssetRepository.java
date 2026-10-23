
package IT.Asset.Tracker.repository;

import IT.Asset.Tracker.entity.Asset;
import IT.Asset.Tracker.entity.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    boolean existsBySerialNumber(String serialNumber);

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByAssignedTo_Id(Long userId);
}
