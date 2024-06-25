package ProjectSite.auth;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRep extends JpaRepository<User, Long> {
    User findByLogin(String login);
    double countUsersBySexNotNull();
    double countUsersBySex(String sex);
    int countByLogin(String login);
    int countByEmail(String email);
}
