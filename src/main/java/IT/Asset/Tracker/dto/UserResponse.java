
package IT.Asset.Tracker.dto;

import IT.Asset.Tracker.entity.User;
import IT.Asset.Tracker.entity.UserRole;

public record UserResponse(
    Long id,
    String username,
    UserRole role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getRole()
        );
    }
}
