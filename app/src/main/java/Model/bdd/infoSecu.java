package Model.bdd;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class infoSecu {
    private String psswrd;
    private String email;

    public infoSecu(String psswrd, String email) {
        this.psswrd = psswrd;
        this.email = this.hashEmail(email);
    }

    public String hashEmail(String email) {
        try {
            // Get the MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Hash the email as bytes and encode it into a hexadecimal string
            byte[] hashBytes = md.digest(email.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hashBytes);

            // Convert to hex format
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Pad with leading zeros if needed to ensure full 64-character length
            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
