package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.todoapp.MarcinRogozToDoApp.PasswordConfigurationProperties;
import pl.todoapp.MarcinRogozToDoApp.model.UserRepository;
import pl.todoapp.MarcinRogozToDoApp.model.projection.UserReadModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.UserWriteModel;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordConfigurationProperties passwordConfigurationProperties;

    UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final PasswordConfigurationProperties passwordConfigurationProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordConfigurationProperties = passwordConfigurationProperties;
    }
    public UserReadModel findUserInDatabase(UserReadModel userReadModel) {
        var user = userRepository.findByEmail(userReadModel.getEmail());
        if (user != null) {
            return new UserReadModel(userRepository.findByEmail(userReadModel.getEmail()));
        }
        throw new UsernameNotFoundException("User " + userReadModel.getEmail() + " not found!");
    }

    public UserReadModel saveUserInDatabase(UserWriteModel userWriteMode) {
        if (passwordConfigurationProperties.getTemplate().isAllowPasswordHash()){
            userWriteMode.setPassword(passwordEncoder.encode(userWriteMode.getPassword()));
        }
        return new UserReadModel(userRepository.save(userWriteMode.toEntity()));
    }

}
