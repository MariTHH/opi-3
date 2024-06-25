package ProjectSite.auth;

public interface AuthServiceMBean {
    long getUserCount();
    void getUserInfo();
    void countFemalePercent();
    void controlEmailTimes();
    void controlWeight();
}
