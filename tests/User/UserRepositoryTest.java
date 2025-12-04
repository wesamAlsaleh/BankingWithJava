package User;

// JUNIT5 imports:

import Global.Utils.DBPaths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;


public class UserRepositoryTest {

    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

//    @Test
//    @DisplayName("Should generate a user id based on the number from the index file (user_index.txt)")
//    void shouldReturnIncrementedUserIdBasedOnIndexFile() {
//        // the id in the file before doing the test
//        var idInFile = 6;
//
//        // generate the id
//        var generatedId = userRepository.generateUserId();
//
//        // expected id
//        var expectedId = idInFile + 1;
//
//        // must equal
//        assertEquals(generatedId, expectedId);
//    }

    @Test
    @DisplayName("Should generate a user id based on the number from the index file (user_index.txt) dynamically")
    void shouldReturnIncrementedUserIdBasedOnIndexFileDynamic() {
        // paths instance
        DBPaths dbPaths = new DBPaths();

        // container
        long idInFile;

        // read the id in the file
        try (Scanner scanner = new Scanner(new File(dbPaths.getUserIndexPath()))) {
            // read the id from the text
            idInFile = Long.parseLong(scanner.nextLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // generate the id
        var generatedId = userRepository.generateUserId();

        // expected id
        var expectedId = idInFile + 1;

        // must equal
        assertEquals(generatedId, expectedId);
    }

    @Test
    @DisplayName("Should create new file with the user details in a file in the specified path")
    void shouldCreateNewFileWithUserDetailsAndCorrectId() {
        // user instance
        User user = new User(9999L, "Unit", "Test", "unittest@gmail.com", "hashedPassword123", UserRole.Customer, 0, null, LocalDateTime.now(), LocalDateTime.now());

        // generate the user
        userRepository.saveUser(user);
    }

    @Test
    @DisplayName("Should reject saving a user with a duplicate ID")
    void shouldRejectDuplicateUserId() {
        // user instance
        User user = new User(9999L, "Unit", "Test", "unittest@gmail.com", "hashedPassword123", UserRole.Customer, 0, null, LocalDateTime.now(), LocalDateTime.now());

        // generate the user
        userRepository.saveUser(user);
    }

    @Test
    @DisplayName("Should return the user object by his id")
    void shouldReturnUserObjectByHisId() {
        // Arrange
        long id = 9999;
        User VirtualUser = new User(id, "Unit", "Test", "unittest@gmail.com", "hashedPassword123", UserRole.Customer, 0, null, LocalDateTime.now(), LocalDateTime.now());
        userRepository.saveUser(VirtualUser); // Ensure the user is existing

        // Act
        User actualUser = userRepository.getUserById(id);

        // Assert
        assertNotNull(actualUser);
        assertEquals(VirtualUser.getId(), actualUser.getId());
        assertEquals(VirtualUser.getFullName(), actualUser.getFullName());
    }

    @Test
    @DisplayName("Should return null if the user with the provided id is not available")
    void shouldReturnNullIfUserNotFoundByHisId() {
        assertNull(userRepository.getUserById(999999999999999999L));
    }

    @Test
    @DisplayName("Should return the user object by his email")
    void shouldReturnUserObjectByHisEmail() {
        // Arrange
        String email = "unittest@gmail.com";
        User VirtualUser = new User(9999L, "Unit", "Test", "unittest@gmail.com", "hashedPassword123", UserRole.Customer, 0, null, LocalDateTime.now(), LocalDateTime.now());
        userRepository.saveUser(VirtualUser); // Ensure the user is existing

        // Act
        User actualUser = userRepository.getUserByEmail(email);

        // Assert
        assertNotNull(actualUser);
        assertEquals(VirtualUser.getId(), actualUser.getId());
        assertEquals(VirtualUser.getFullName(), actualUser.getFullName());
    }

    @Test
    @DisplayName("Should return null if the user with the provided email is not available")
    void shouldReturnNullIfUserNotFoundByHisEmail() {
        assertNull(userRepository.getUserByEmail("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww@gmail.com"));
    }

    @Test
    @DisplayName("Should increment the login counter of the user")
    void shouldIncrementLoginAttemptsCounter() {
        // Arrange
        long id = 9999L;
        int fraudAttempts = 0;
        int expectedAttemptsCount = fraudAttempts + 1;
        User VirtualUser = new User(id, "Unit", "Test", "unittest@gmail.com", "hashedPassword123", UserRole.Customer, fraudAttempts, null, LocalDateTime.now(), LocalDateTime.now());
        userRepository.saveUser(VirtualUser); // Ensure the user is existing

        // Act
        userRepository.increaseFraudAttemptsCounter(id);

        // Assert
        var updatedUser = userRepository.getUserById(id); // get the updated version of the user
        assertEquals(expectedAttemptsCount, updatedUser.getFraudAttemptsCount());
    }

    @Test
    @DisplayName("Should the lockUntil property be filled with a date and the counter state is set to zero")
    void shouldLockUntilPropertyBeFilledWithADateAndTheCounterIsSetToZero() {
        // Arrange
        long id = 9999L;
        int fraudAttempts = 3;
        User VirtualUser = new User(id, "Unit", "Test", "unittest@gmail.com", "hashedPassword123", UserRole.Customer, fraudAttempts, null, LocalDateTime.now(), LocalDateTime.now());
        userRepository.saveUser(VirtualUser); // Ensure the user is existing

        // Act
        userRepository.increaseFraudAttemptsCounter(id);

        // Assert
        var updatedUser = userRepository.getUserById(id); // get the updated version of the user
        assertNotNull(updatedUser.getLockUntil());
    }

    @AfterEach
    void tearDown() {
        userRepository = null;
    }

}