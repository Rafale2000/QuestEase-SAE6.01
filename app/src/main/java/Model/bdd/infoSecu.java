package Model.bdd;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class infoSecu {
    private int id;
    private String psswrd;
    private String email;

    public infoSecu(int I, String psswrd, String email) {
        this.id = I;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPsswrd() {
        return psswrd;
    }

    public void setPsswrd(String psswrd) {
        this.psswrd = psswrd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
