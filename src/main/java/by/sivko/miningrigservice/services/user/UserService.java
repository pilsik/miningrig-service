package by.sivko.miningrigservice.services.user;

import by.sivko.miningrigservice.models.user.User;

public interface UserService {
    public User findUserByUsername(String username);
    public User findUserByEmail(String email);
    public void saveUser(User user);
}
