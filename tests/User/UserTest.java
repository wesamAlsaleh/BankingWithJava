package User;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Notes: assertNotNull (null -> fail)

public class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User(
                1L,
                "Unit",
                "Test",
                "unittest@gmail.com",
                "hashedPassword123",
                UserRole.Customer,
                0,
                null,
//                 LocalDateTime.now().plusMinutes(1),
//                 LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Should convert User object to user record string")
    public void shouldConvertUserObjectToUserRecord() {
        assertNotNull(user.getUserRecord());
    }

    @Test
    @DisplayName("When the user is locked it should return true")
    public void whenUserIsLockedReturnTrue() {
        assertTrue(user.isLocked());
    }

    @Test
    @DisplayName("When the user is not locked it should return false")
    public void whenUserIsNotLockedReturnFalse() {
        assertFalse(user.isLocked());
    }

    @Test
    @DisplayName("When the user is locked before and the period is done it should return false")
    public void whenUserIsLockedBeforeReturnFalseIfItsAfterCurrentTime() {
        assertFalse(user.isLocked());
    }
}