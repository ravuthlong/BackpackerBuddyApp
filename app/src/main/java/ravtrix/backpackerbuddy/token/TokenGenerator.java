package ravtrix.backpackerbuddy.token;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Ravinder on 3/26/17.
 */

public class TokenGenerator {

    private SecureRandom random = new SecureRandom();

    public String getSecureToken() {
        return new BigInteger(130, random).toString(32);
    }
}
