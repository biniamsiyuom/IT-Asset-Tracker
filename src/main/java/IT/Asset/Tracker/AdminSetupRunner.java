
package IT.Asset.Tracker;

import IT.Asset.Tracker.entity.User;
import IT.Asset.Tracker.entity.UserRole;
import IT.Asset.Tracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSetupRunner {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User(
                    "admin",
                    passwordEncoder.encode("ChangeMe@12345"),
                    UserRole.ROLE_ADMIN
                );

                userRepository.save(admin);

                System.out.println(
                    "Admin account created successfully."
                );
            } else {
                System.out.println(
                    "Admin account already exists."
                );
            }
        };
    }
}
