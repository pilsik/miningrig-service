package by.sivko.miningrigservice.services.user;

import by.sivko.miningrigservice.dao.user.UserProfileRepository;
import by.sivko.miningrigservice.dao.user.UserRepository;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.models.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        UserProfile userRole = this.userProfileRepository.findByType("ROLE_USER");
        user.setUserProfiles(new HashSet<UserProfile>(Arrays.asList(userRole)));
        this.userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
