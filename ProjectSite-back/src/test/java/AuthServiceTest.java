//import ProjectSite.auth.AuthService;
//import ProjectSite.auth.User;
//import ProjectSite.auth.UserRep;
//import org.json.JSONObject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import javax.management.MalformedObjectNameException;
//import java.util.Base64;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class AuthServiceTest {
//
//    private UserRep userRepository;
//    private AuthService authService;
//
//    @BeforeEach
//    public void setup() {
//        userRepository = Mockito.mock(UserRep.class);
//        authService = new AuthService(userRepository);
//    }
//
//    @Test
//    public void testRegister() {
//        String login = "testLogin";
//        String password = "testPassword";
//
//        when(userRepository.findByLogin(login)).thenReturn(null);
//
//        authService.register(login, password);
//
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    public void testRegisterWithExistingUser() {
//        String login = "testLogin";
//        String password = "testPassword";
//
//        when(userRepository.findByLogin(login)).thenReturn(new User());
//
//        assertThrows(ResponseStatusException.class, () -> authService.register(login, password));
//    }
//
//    @Test
//    public void testAddParams() {
//        String login = "testLogin";
//        String username = "testUsername";
//        String sex = "testSex";
//        String weight = "testWeight";
//        String email = "testEmail";
//
//        User user = new User(login, "testPassword", null, null, null);
//        when(userRepository.findByLogin(login)).thenReturn(user);
//
//        authService.addParams(login, username, sex, weight, email);
//
//        verify(userRepository, times(1)).save(user);
//        assertEquals(username, user.getLogin());
//        assertEquals(sex, user.getSex());
//        assertEquals(weight, user.getWeight());
//    }
//
//    @Test
//    public void testGetParams() {
//        String login = "testLogin";
//        String weight = "1";
//        String sex = "testSex";
//        String email = "testEmail";
//
//        User user = new User(login, "testPassword", weight, sex, email);
//        when(userRepository.findByLogin(login)).thenReturn(user);
//
//        String result = authService.getParams(login);
//        System.out.println(result);
//
//        assertEquals(weight, result);
//    }
//
//    @Test
//    public void testCheck() {
//        String login = "testLogin";
//        String password = "testPassword";
//        String passwordHash = authService.getHash(password);
//        String weight = "1";
//        String sex = "testSex";
//        String email = "testEmail";
//
//        User user = new User(login, passwordHash, weight, sex, email);
//        when(userRepository.findByLogin(login)).thenReturn(user);
//
//        String authorization = "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes());
//        String result = authService.check(authorization);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", user.getLogin());
//        jsonObject.put("weight", user.getWeight());
//        jsonObject.put("sex", user.getSex());
//        assertEquals(jsonObject.toString(), result);
//    }
//
//    @Test
//    public void testCheckWithInvalidAuthorization() {
//        String invalidAuthorization = "Invalid";
//
//        assertThrows(ResponseStatusException.class, () -> authService.check(invalidAuthorization));
//    }
//
//    @Test
//    public void testCheckWithInvalidPassword() {
//        String login = "testLogin";
//        String password = "invalidPassword";
//
//        User user = new User(login, authService.getHash("testPassword"), "1", "testSex", "email");
//        when(userRepository.findByLogin(login)).thenReturn(user);
//
//        String authorization = "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes());
//
//        assertThrows(ResponseStatusException.class, () -> authService.check(authorization));
//    }
//
//
//}
