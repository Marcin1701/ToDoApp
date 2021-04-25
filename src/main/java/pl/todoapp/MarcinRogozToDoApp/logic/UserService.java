package pl.todoapp.MarcinRogozToDoApp.logic;

import org.springframework.stereotype.Service;
import pl.todoapp.MarcinRogozToDoApp.model.UserRepository;
import pl.todoapp.MarcinRogozToDoApp.model.projection.UserReadModel;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserReadModel findUserInDatabase(UserReadModel userReadModel) {
        return new UserReadModel(userRepository.findByEmail(userReadModel.getEmail()));
    }


}
