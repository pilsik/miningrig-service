package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.dto.UserDto;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.user.UserService;
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
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return fieldErrors.toString();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@Valid UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
        User userExistsLogin = userService.findUserByUsername(user.getUsername());
        if (userExistsLogin != null) {
            return new ResponseEntity<>("A user with this name already exists", HttpStatus.CONFLICT);
        }
        User userExistsEmail = userService.findUserByUsername(user.getEmail());
        if (userExistsEmail != null) {
            return new ResponseEntity<>("A user with this email already exists", HttpStatus.CONFLICT);
        }
        userService.saveUser(user);
        return new ResponseEntity<>("User has been registered successfully", HttpStatus.OK);
    }

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

}
