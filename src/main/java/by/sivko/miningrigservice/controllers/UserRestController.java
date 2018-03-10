package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.controllers.exceptions.AlreadyExistsException;
import by.sivko.miningrigservice.controllers.exceptions.NotExistException;
import by.sivko.miningrigservice.controllers.exceptions.PasswordException;
import by.sivko.miningrigservice.dto.UserDto;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.user.UserService;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;

@RestController
public class UserRestController {

    private static final int MIN_PASSWORD_LENGTH = 5;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@Valid UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
        User userExistsLogin = userService.findUserByUsername(user.getUsername());
        if (userExistsLogin != null) {
            throw new AlreadyExistsException("A user with this name already exists");
            //return new ResponseEntity<>("A user with this name already exists", HttpStatus.CONFLICT);
        }
        User userExistsEmail = userService.findUserByUsername(user.getEmail());
        if (userExistsEmail != null) {
            throw new AlreadyExistsException("A user with this email already exists");
            //return new ResponseEntity<>("A user with this email already exists", HttpStatus.CONFLICT);
        }
        userService.saveUser(user);
        return new ResponseEntity<>("User has been registered successfully", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity<Void> changeUserPassword(Principal principal, String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new PasswordException("Password must have al least 5 characters");
        }
        User user = this.userService.findUserByUsername(principal.getName());
        this.userService.changeUserPassword(user, password);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        if (userList.isEmpty()) {
            throw new NotExistException(String.format("Don't exist any user"));
        } else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new NotExistException(String.format("User with name [%s] not exists", username));
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    /*
   @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new UserDto());
        modelAndView.setViewName("registration");
        return modelAndView;
    }
    */
}
