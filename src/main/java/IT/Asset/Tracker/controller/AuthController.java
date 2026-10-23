
package IT.Asset.Tracker.controller;

import IT.Asset.Tracker.dto.UserRegistrationRequest;
import IT.Asset.Tracker.dto.UserResponse;
import IT.Asset.Tracker.entity.User;
import IT.Asset.Tracker.entity.UserRole;
import IT.Asset.Tracker.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody UserRegistrationRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                UserRole.ROLE_STAFF
        );

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }
}

