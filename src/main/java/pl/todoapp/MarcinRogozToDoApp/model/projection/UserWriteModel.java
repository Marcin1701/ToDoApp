package pl.todoapp.MarcinRogozToDoApp.model.projection;

import pl.todoapp.MarcinRogozToDoApp.model.TaskGroup;
import pl.todoapp.MarcinRogozToDoApp.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserWriteModel {

    private String email;

    private String password;

    private Set<GroupWriteModel> groups;

    public UserWriteModel() {}

    public UserWriteModel(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.groups =
                user.getTaskGroups()
                        .stream()
                        .map(GroupWriteModel::new)
                        .collect(Collectors.toSet());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<GroupWriteModel> getGroups() {
        return groups;
    }

    public void setGroups(final Set<GroupWriteModel> groups) {
        this.groups = groups;
    }

    public User toEntity() {
        return new User(email, password, groups.stream().map(TaskGroup::new).collect(Collectors.toSet()));
    }
}
