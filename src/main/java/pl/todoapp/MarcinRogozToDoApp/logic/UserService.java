package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.todoapp.MarcinRogozToDoApp.model.UserRepository;
import pl.todoapp.MarcinRogozToDoApp.model.projection.UserReadModel;
import pl.todoapp.MarcinRogozToDoApp.model.projection.UserWriteModel;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserReadModel findUserInDatabase(UserReadModel userReadModel) {
        var user = userRepository.findByEmail(userReadModel.getEmail());
        if (user != null) {
            return new UserReadModel(userRepository.findByEmail(userReadModel.getEmail()));
        }
        throw new UsernameNotFoundException("User " + userReadModel.getEmail() + " not found!");
    }

    UserReadModel saveUserInDatabase(UserWriteModel userWriteMode) {
        return new UserReadModel(userRepository.save(userWriteMode.toEntity()));
    }

}
