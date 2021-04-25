package pl.todoapp.MarcinRogozToDoApp.model;

import java.util.Optional;

public interface UserRepository {

    User save(User entity);

    User findByEmail(String email);
}
