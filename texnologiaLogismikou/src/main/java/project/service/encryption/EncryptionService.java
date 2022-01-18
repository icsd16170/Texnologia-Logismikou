package project.service.encryption;

public interface EncryptionService {

     boolean stringsMatch(String encodedString, String plainString);

     String encodeString(String plainString);
}
