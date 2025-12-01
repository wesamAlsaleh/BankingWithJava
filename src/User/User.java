package User;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
    private int fraudAttemptsCount;
    private LocalDateTime lockUntil;


}
