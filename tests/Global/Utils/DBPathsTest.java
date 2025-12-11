package Global.Utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBPathsTest {

    DBPaths dbPaths;

    @BeforeEach
    void setUp() {
        dbPaths = new DBPaths();
    }

    @Test
    @DisplayName("Should return the relative path of the user index file ")
    void getDbPath() {
        // Act
        var path = dbPaths.getUserIndexPath();
        System.out.println(path);

        // Assert
        assertNotNull(path);
    }

    @AfterEach
    void tearDown() {
        dbPaths = null;
    }
}