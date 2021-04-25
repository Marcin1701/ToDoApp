package pl.todoapp.MarcinRogozToDoApp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.todoapp.MarcinRogozToDoApp.model.User;
import pl.todoapp.MarcinRogozToDoApp.model.UserRepository;

import java.util.Optional;

@Repository
public interface SqlUserRepository extends UserRepository, JpaRepository<User, Integer> {

    @Override
    User findByEmail(String email);

}
