package User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
    private int fraudAttemptsCount;
    private LocalDateTime lockUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // default constructor
    public User(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserRole.Customer;
        this.fraudAttemptsCount = 0;
        this.lockUntil = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // custom constructor
    public User(Long id, String firstName, String lastName, String email, String password, UserRole role, int fraudAttemptsCount, LocalDateTime lockUntil, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fraudAttemptsCount = fraudAttemptsCount;
        this.lockUntil = lockUntil;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // custom construct to update the counter
    public User(Long id, String firstName, String lastName, String email, String password, UserRole role, int fraudAttemptsCount, LocalDateTime lockUntil) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fraudAttemptsCount = fraudAttemptsCount;
        this.lockUntil = lockUntil;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public int getFraudAttemptsCount() {
        return fraudAttemptsCount;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // setters
    public void setFraudAttemptsCount(int fraudAttemptsCount) {
        this.fraudAttemptsCount = fraudAttemptsCount;
    }

    public void setLockUntil(LocalDateTime lockUntil) {
        this.lockUntil = lockUntil;
    }

    // function to help writing the user record in the text file
    public String getUserRecord() {
        return String.format(
                "First_Name:%s, Last_Name:%s, Email:%s, Password: %s, Role: %s, fraud_Attempts_Count: %s, LockUntil: %s, Created_At: %s, Updated_At: %s",
                firstName,
                lastName,
                email,
                password,
                role,
                fraudAttemptsCount,
                lockUntil,
                createdAt,
                updatedAt
        );
    }

    // function to check if the user is locked
    public boolean isLocked() {
        // if the field is null return false
        if (this.lockUntil == null) {
            return false;
        }

        // if the lock time is after the current time return true otherwise false
        return getLockUntil().isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", fraudAttemptsCount=" + fraudAttemptsCount +
                ", lockUntil=" + lockUntil +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

