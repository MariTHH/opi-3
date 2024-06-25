package ProjectSite.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.management.*;
import java.lang.management.ManagementFactory;
@Service
public class UserMonitor extends NotificationBroadcasterSupport implements UserMonitorMBean {
    private final UserRep userRepository;
        private int threshold = 50;
    private long sequenceNumber = 1;

    public UserMonitor(UserRep userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{AttributeChangeNotification.ATTRIBUTE_CHANGE};
        String name = AttributeChangeNotification.class.getName();
        String description = "Notification";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }


    @Override
    public void incrementUserCount() {
        if (userRepository.count() > threshold) {
            Notification notification = new Notification(
                    "UserCountExceeded",
                    this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "Количество пользователей превысило порог: " + userRepository.count()
            );
            sendNotification(notification);
        }
    }


}

