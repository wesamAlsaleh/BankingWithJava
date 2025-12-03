package Auth;

// Installed the package as Jar and added as Library from: https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
// !

import org.mindrot.jbcrypt.BCrypt;

public class BCryptService {
    // function to hash the password
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // function to authenticate the user
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        // if the raw password is the same as the hashed password return true
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }


}
