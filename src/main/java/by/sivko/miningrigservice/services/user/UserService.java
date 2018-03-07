package by.sivko.miningrigservice.services.user;

import by.sivko.miningrigservice.models.Rig;
import by.sivko.miningrigservice.models.user.User;

import java.util.Set;

public interface UserService {
    public User findUserByUsername(String username);
    public User findUserByEmail(String email);
    public void saveUser(User user);
    public Set<Rig> getUserRigsByUsername(String username);
}
