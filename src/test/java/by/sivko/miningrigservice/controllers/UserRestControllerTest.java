package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.MiningRigServiceApplication;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.security.Principal;

@TestPropertySource("classpath:application-test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MiningRigServiceApplication.class)
@WebAppConfiguration
public class UserRestControllerTest {

    private static final int SUCCESSES_STATUS = 200;
    private static final int RESPONSE_BAD_STATUS = 400;
    private static final int SUCCESSES_CREATED_STATUS = 201;
    private static final int RESPONSE_CONFLICT_BAD_STATUS = 409;
    private static final String USER_NAME = "USER_NAME";
    private static final String WRONG_USER_NAME = "WRONG_USER_NAME";
    private static final String REQUEST_PARAM_USERNAME = "username";
    private static final String PATH_USER = "/user";
    private static final String REQUEST_PARAM_USERNAME_VALUE = "user";
    private static final String REQUEST_PARAM_EMAIL = "email";
    private static final String REQUEST_PARAM_EMAIL_VALUE = "user@user.us";
    private static final String REQUEST_PARAM_PASSWORD = "password";
    private static final String REQUEST_PARAM_PASSWORD_VALUE = "password";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String NEW_REQUEST_PARAM_PASSWORD_VALUE = "NEW_REQUEST_PARAM_PASSWORD_VALUE";
    private static final String PASSWORD_LESS_FIVE_LETTERS = "five";


    @Mock
    UserService userService;

    @Mock
    Principal principal;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    @InjectMocks
    UserRestController userRestController;

    MockMvc mockMvc;

    User userReturnMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userReturnMock = new User("user", "user", "user");
        User userReturnMock2 = new User("user2", "user2", "user2");
        Mockito
                .when(userService.saveUser(Mockito.any(User.class)))
                .thenReturn(userReturnMock);
        Mockito
                .when(userService.findUserByUsername(USER_NAME))
                .thenReturn(userReturnMock);
        Mockito
                .when(userService.findUserByEmail(USER_EMAIL))
                .thenReturn(userReturnMock);
        Mockito
                .when(userService.findUserByUsername(WRONG_USER_NAME))
                .thenReturn(null);
        Mockito
                .when(principal.getName())
                .thenReturn(USER_NAME);
    }

    @Test
    public void createNewUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_USER)
                .param(REQUEST_PARAM_USERNAME, REQUEST_PARAM_USERNAME_VALUE)
                .param(REQUEST_PARAM_EMAIL, REQUEST_PARAM_EMAIL_VALUE)
                .param(REQUEST_PARAM_PASSWORD, REQUEST_PARAM_PASSWORD_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(SUCCESSES_CREATED_STATUS));
    }

    @Test
    public void createExistUserByUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_USER)
                .param(REQUEST_PARAM_USERNAME, REQUEST_PARAM_USERNAME_VALUE)
                .param(REQUEST_PARAM_EMAIL, USER_EMAIL)
                .param(REQUEST_PARAM_PASSWORD, REQUEST_PARAM_PASSWORD_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(RESPONSE_CONFLICT_BAD_STATUS));
    }

    @Test
    public void createExistUserByEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_USER)
                .param(REQUEST_PARAM_USERNAME, USER_NAME)
                .param(REQUEST_PARAM_EMAIL, REQUEST_PARAM_EMAIL_VALUE)
                .param(REQUEST_PARAM_PASSWORD, REQUEST_PARAM_PASSWORD_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(RESPONSE_CONFLICT_BAD_STATUS));
    }

    @Test
    public void changeUserPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH_USER)
                .principal(principal)
                .param(REQUEST_PARAM_PASSWORD, NEW_REQUEST_PARAM_PASSWORD_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(SUCCESSES_STATUS));
    }

    @Test
    public void changeUserPasswordEmptyPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH_USER)
                .principal(principal)
                .param(REQUEST_PARAM_PASSWORD, ""))
                .andExpect(MockMvcResultMatchers.status().is(RESPONSE_CONFLICT_BAD_STATUS));
    }

    @Test
    public void changeUserPasswordLengthLessFiveLetters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH_USER)
                .principal(principal)
                .param(REQUEST_PARAM_PASSWORD, PASSWORD_LESS_FIVE_LETTERS))
                .andExpect(MockMvcResultMatchers.status().is(RESPONSE_CONFLICT_BAD_STATUS));
    }

    @Test
    public void getAllUsers() throws Exception {

    }

    @Test
    public void getUser() throws Exception {

    }

}