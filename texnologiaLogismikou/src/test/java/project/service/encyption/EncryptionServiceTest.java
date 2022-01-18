package project.service.encyption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.service.encryption.EncryptionService;
import project.service.encryption.EncryptionServiceImpl;

class EncryptionServiceTest {

	private final EncryptionService testee = new EncryptionServiceImpl();

	@Test
	void passwordIsValid_validPassword_returnsTrue() {
		String givenPassword = "password123A";
		String savedPassword = "$2a$10$jpghdpb5Z7K47t5UxDtHwu9Lkube95xm2BqynFDx/rpAhcx5IbeK2";
		Assertions.assertTrue(testee.stringsMatch(savedPassword, givenPassword));
	}

	@Test
	void passwordIsValid_validPassword_returnsFalse() {
		String givenPassword = "invalid";
		String savedPassword = "$2a$10$jpghdpb5Z7K47t5UxDtHwu9Lkube95xm2BqynFDx/rpAhcx5IbeK2";
		Assertions.assertFalse(testee.stringsMatch(savedPassword, givenPassword));
	}
}
