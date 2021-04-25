package pl.todoapp.MarcinRogozToDoApp.model.projection;

import pl.todoapp.MarcinRogozToDoApp.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserReadModel {

    private final String email;

    private final String password;

    private Set<GroupReadModel> groups;

    public UserReadModel(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.groups =
                user.getTaskGroups().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toSet());
    }

    public UserReadModel(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<GroupReadModel> getGroups() {
        return groups;
    }
}
