package project.service.encryption;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptionServiceImpl implements EncryptionService {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public EncryptionServiceImpl() {
    }

    public boolean stringsMatch(String encodedString, String givenPassword) {
        return bCryptPasswordEncoder.matches(givenPassword, encodedString);
    }

    public String encodeString(String plainString) {
        return bCryptPasswordEncoder.encode(plainString);
    }
}
