package pl.edu.pjatk.foodbook.userservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.edu.pjatk.foodbook.userservice.exception.UserAlreadyExistsException;
import pl.edu.pjatk.foodbook.userservice.repository.UserRepository;
import pl.edu.pjatk.foodbook.userservice.repository.model.Role;
import pl.edu.pjatk.foodbook.userservice.rest.dto.request.CreateUserInput;
import pl.edu.pjatk.foodbook.userservice.rest.service.UserService;

import static org.mockito.Mockito.any;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                      .webAppContextSetup(context)
                      .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        modelMapper = new ModelMapper();
    }

    @Test
    public void shouldReturnCreatedWhenSavingNewUser() throws Exception {
        // given
        CreateUserInput createUserInput = CreateUserInput.builder()
                                            .username("theLegend27")
                                            .email("theLegend27@gmail.com")
                                            .realName("Roy Jones Jr")
                                            .password("TheRoy88$")
                                            .role(Role.USER.name())
                                            .build();
        byte[] content = objectMapper.writeValueAsBytes(createUserInput);
        mockMvc.perform(
                // when
                MockMvcRequestBuilders
                    .post("/api/v1")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenSavingNewUser() throws Exception {
        // given missing username, wrong format email, invalid password, too short real name
        CreateUserInput createUserInput = CreateUserInput.builder()
                                            .email("theLegend27@gmail.com")
                                            .realName("Roy")
                                            .password("theroy")
                                            .role(Role.USER.name())
                                            .build();
        byte[] content = objectMapper.writeValueAsBytes(createUserInput);
        mockMvc.perform(
                // when
                MockMvcRequestBuilders
                    .post("/api/v1")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenUserAlreadyExists() throws Exception {
        // given
        CreateUserInput createUserInput = CreateUserInput.builder()
                                            .username("theLegend27")
                                            .email("theLegend27@gmail.com")
                                            .realName("Roy Jones Jr")
                                            .password("TheRoy88$")
                                            .role(Role.USER.name())
                                            .build();
        Mockito.doThrow(UserAlreadyExistsException.class)
            .when(userService)
            .saveUser(any());
        byte[] content = objectMapper.writeValueAsBytes(createUserInput);
        mockMvc.perform(
                // when
                MockMvcRequestBuilders
                    .post("/api/v1")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}