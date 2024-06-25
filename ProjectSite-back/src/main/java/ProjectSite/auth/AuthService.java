package ProjectSite.auth;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AuthService extends NotificationBroadcasterSupport implements AuthServiceMBean {
    private final UserRep userRepository;
    private final AtomicInteger emailCount = new AtomicInteger(0);
    private long sequenceNumber = 1;
    private User currUser = null;


    // Используем LinkedHashMap для кеша с ограниченным размером
    private final Map<String, User> userCache = new LinkedHashMap<String, User>(100, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, User> eldest) {
            return size() > 100; // Ограничиваем размер кеша до 100 записей
        }
    };

    public AuthService(UserRep userRepository) {
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
    public long getUserCount() {
        return userRepository.count();
    }

    @Override
    public void getUserInfo() {
        Notification notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Логин: " + currUser.getLogin() + ", Пол: " + currUser.getSex() + ", Вес: " + currUser.getWeight(),
                "sequenceNumber",
                "int",
                sequenceNumber - 1,
                sequenceNumber
        );
        sendNotification(notification);
    }

    @Override
    public void countFemalePercent() {
        Notification notification = new Notification(
                "gender.women",
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Процент женского пола относительно общего количества: " + Math.round(userRepository.countUsersBySex("women") / userRepository.countUsersBySexNotNull() * 100) + "%"
        );
        sendNotification(notification);
    }

    @Override
    public void controlEmailTimes() {
        try {
            Notification notification = new Notification(
                    "email",
                    this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "Email используется более 3 раз: " + currUser.getEmail()
            );
            sendNotification(notification);
        } catch (NullPointerException e) {
            Notification notification = new Notification(
                    "email",
                    this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "Нет авторизации"
            );
            sendNotification(notification);
        }
    }

    @Override
    public void controlWeight() {
        try {
            if (Integer.parseInt(currUser.getWeight()) > 500) {
                Notification notification = new Notification(
                        "weight",
                        this,
                        sequenceNumber++,
                        System.currentTimeMillis(),
                        "Вес превышает 500 кг: " + currUser.getWeight()
                );
                sendNotification(notification);
            }
        } catch (NullPointerException e) {
            Notification notification = new Notification(
                    "weight",
                    this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "Пользователь не указал вес"
            );
            sendNotification(notification);
        }
    }

    public static List<String> myList = new ArrayList<>();

    public void register(String login, String password) {
        if (userRepository.findByLogin(login) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login has been taken");
        }

        User user = new User(login, getHash(password), null, null, null);
        currUser = user;
        userRepository.save(user);

        // Используем кеширование пользователей с ограничением
        cacheUser(user);

        for(int i = 0; i < 1000000; i++) {
            cacheUser(new User(login + i, getHash(password), null, null, null));
        }
    }

    // Метод кеширования пользователей с ограничением размера
    private void cacheUser(User user) {
        userCache.put(user.getLogin(), user);
    }

    public void addParams(String login, String username, String sex, String weight, String email) {
        User user = userRepository.findByLogin(login);
        currUser = user;
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (!username.isEmpty()) {
            user.setLogin(username);
        }
        if (!sex.isEmpty()) {
            boolean isFemale = "women".equalsIgnoreCase(sex);
            if (isFemale && !sex.equalsIgnoreCase(user.getSex())) {
                countFemalePercent();
            }
            user.setSex(sex);
        }
        if (!weight.isEmpty()) {
            user.setWeight(weight);
            System.out.println(Integer.parseInt(weight));
            controlWeight();
        }
        if (!email.isEmpty()) {
            user.setEmail(email);
            if (userRepository.countByEmail(email) >= 3) {
                emailCount.set(userRepository.countByEmail(email));
                controlEmailTimes();
            }
        }
        userRepository.save(user);

        // Используем кеширование пользователей с ограничением
        cacheUser(user);
    }

    public String check(String authorization) {
        if (authorization == null || !authorization.startsWith("Basic"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Authorization header");

        String login, password;

        try {
            String base64 = authorization.substring("Basic".length()).trim();
            String[] credentials = new String(Base64.getDecoder().decode(base64)).split(":", 2);
            if (credentials.length < 2)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Authorization header");
            login = credentials[0];
            password = credentials[1];
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base64");
        }

        User user = userRepository.findByLogin(login);
        currUser = user;
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login");
        }

        if (!getHash(password).equals(user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", user.getLogin());
        jsonObject.put("weight", user.getWeight());
        jsonObject.put("sex", user.getSex());

        return jsonObject.toString();
    }

    public String getHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder hexBuilder = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexBuilder.append('0');
                hexBuilder.append(hex);
            }
            return hexBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getParams(String login) {
        User user = userRepository.findByLogin(login);
        return user.getWeight();
    }
}
