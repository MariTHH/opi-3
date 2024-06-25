package ProjectSite;
import ProjectSite.auth.AuthService;
import ProjectSite.auth.User;
import ProjectSite.auth.UserMonitor;
import ProjectSite.auth.UserRep;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.management.*;
import java.lang.management.ManagementFactory;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        SpringApplication.run(Application.class, args);


    }
}
