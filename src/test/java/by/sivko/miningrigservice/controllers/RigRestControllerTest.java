package by.sivko.miningrigservice.controllers;

import by.sivko.miningrigservice.MiningRigServiceApplication;
import by.sivko.miningrigservice.models.rigs.Rig;
import by.sivko.miningrigservice.models.user.User;
import by.sivko.miningrigservice.services.rig.RigService;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource("classpath:application-test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MiningRigServiceApplication.class)
@WebAppConfiguration
public class RigRestControllerTest {

    private static final String MOCK_PRINCIPAL_NAME_FOR_FIRST_USER = "MOCK_PRINCIPAL_NAME_FOR_FIRST_USER";
    private static final String MOCK_PRINCIPAL_NAME_FOR_SECOND_USER = "MOCK_PRINCIPAL_NAME_FOR_SECOND_USER";
    private static final String FIRST_USER_USERNAME = "FIRST_USER_USERNAME";
    private static final String FIRST_USER_PASSWORD = "FIRST_USER_PASSWORD";
    private static final String FIRST_USER_EMAIL = "FIRST_USER_EMAIL";
    private static final String SECOND_USER_USERNAME = "SECOND_USER_USERNAME";
    private static final String SECOND_USER_PASSWORD = "SECOND_USER_PASSWORD";
    private static final String SECOND_USER_EMAIL = "SECOND_USER_EMAIL";
    private static final String FIRST_RIG_NAME = "FIRST_RIG_NAME";
    private static final String FIRST_RIG_PASSWORD;
    private static final String SECOND_RIG_NAME = "SECOND_RIG_NAME";
    private static final String SECOND_RIG_PASSWORD = "SECOND_RIG_PASSWORD";

    private static final int HTTP_RESPONSE_CONFLICT_CODE = 409;
    private static final int HTTP_RESPONSE_SUCCESS_CODE = 200;

    private static final String PATH_RIGS_USER = "/rigs";

    static {
        FIRST_RIG_PASSWORD = "FIRST_RIG_PASSWORD";
    }

    @Autowired
    WebApplicationContext webApplicationContext;

    @Mock
    UserService mockUserService;

    @Mock
    RigService mockRigService;

    @Mock
    Principal mockPrincipalForFirstUser;

    @Mock
    Principal mockPrincipalForSecondUser;

    @Autowired
    @InjectMocks
    RigRestController rigRestController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        User firstUser = new User(FIRST_USER_USERNAME, FIRST_USER_PASSWORD, FIRST_USER_EMAIL);
        User secondUser = new User(SECOND_USER_USERNAME, SECOND_USER_PASSWORD, SECOND_USER_EMAIL);
        Rig firstRig = new Rig(FIRST_RIG_NAME, FIRST_RIG_PASSWORD, firstUser);
        Rig secondRig = new Rig(SECOND_RIG_NAME, SECOND_RIG_PASSWORD, firstUser);
        List<Rig> rigArrayList = new ArrayList<>(Arrays.asList(firstRig, secondRig));
        firstUser.setUserRigList(rigArrayList);
        Mockito
                .when(mockPrincipalForFirstUser.getName())
                .thenReturn(MOCK_PRINCIPAL_NAME_FOR_FIRST_USER);
        Mockito
                .when(mockPrincipalForSecondUser.getName())
                .thenReturn(MOCK_PRINCIPAL_NAME_FOR_SECOND_USER);
        Mockito
                .when(mockUserService.getUserRigsByUsername(MOCK_PRINCIPAL_NAME_FOR_FIRST_USER))
                .thenReturn(firstUser.getUserRigList());
        Mockito
                .when(mockUserService.getUserRigsByUsername(MOCK_PRINCIPAL_NAME_FOR_SECOND_USER))
                .thenReturn(secondUser.getUserRigList());

    }

    @Test
    public void getRigsOfUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_RIGS_USER)
                .principal(mockPrincipalForFirstUser))
                .andExpect(MockMvcResultMatchers.status().is(HTTP_RESPONSE_SUCCESS_CODE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(FIRST_RIG_NAME)))
                .andExpect(jsonPath("$[1].name", is(SECOND_RIG_NAME)));
        verify(mockPrincipalForFirstUser, times(1)).getName();
        verify(mockUserService, times(1)).getUserRigsByUsername(Mockito.any(String.class));
        verifyNoMoreInteractions(mockPrincipalForFirstUser);
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void getRigsOfUserEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_RIGS_USER))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CONFLICT.value()));
        verify(mockPrincipalForSecondUser, times(1)).getName();
        verify(mockUserService, times(1)).getUserRigsByUsername(Mockito.any(String.class));
        verifyNoMoreInteractions(mockPrincipalForSecondUser);
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void createRig() throws Exception {
    }

    @Test
    public void getRig() throws Exception {
    }

    @Test
    public void removeRig() throws Exception {
    }

    @Test
    public void changeRigPassword() throws Exception {
    }

}