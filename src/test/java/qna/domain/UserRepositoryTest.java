package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	public void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("사용자 1명 저장 성공")
	public void saveUserSuccess() {
		User javajigi = UserTest.JAVAJIGI;
		User save = userRepository.save(javajigi);

		assertAll(() -> {
			assertThat(save.getUserId()).isEqualTo(javajigi.getUserId());
			assertThat(save.equalsNameAndEmail(javajigi)).isTrue();
		});
	}
}