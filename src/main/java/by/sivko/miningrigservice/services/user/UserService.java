package by.sivko.miningrigservice.services.user;

import by.sivko.miningrigservice.models.rigs.Rig;
import by.sivko.miningrigservice.models.user.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    public List<User> getAllUsers();
    public User findUserByUsername(String username);
    public User findUserByEmail(String email);
    public User saveUser(User user);
    public Set<Rig> getUserRigsByUsername(String username);
    public void changeUserPassword(User user, String password);
}
