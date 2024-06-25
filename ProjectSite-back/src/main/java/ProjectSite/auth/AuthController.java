package ProjectSite.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;
    private final UserMonitor userMonitor;

    @Autowired
    public AuthController(AuthService authService, UserMonitor userMonitor) {
        this.authService = authService;
        this.userMonitor = userMonitor;
    }

    @PostMapping("/api/register")
    public void register(@RequestParam("login") String login, @RequestParam("password") String password) throws InterruptedException {
        authService.register(login, password);
    }

    @PostMapping("/api/login")
    public String login(@RequestHeader("Authorization") String authorization) {
        return authService.check(authorization);
    }

    @PostMapping("/api/params")
    public void params(@RequestParam("login") String login, @RequestParam("username") String username, @RequestParam("sex") String sex, @RequestParam("weight") String weight,@RequestParam("email") String email ) {
        authService.addParams(login, username, sex, weight, email);
    }

    @PostMapping("/api/getParams")
    public String param(@RequestParam("login") String login) {
        return authService.getParams(login);
    }
}
