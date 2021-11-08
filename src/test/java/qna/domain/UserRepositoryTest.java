package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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
		User save = userRepository.save(UserTest.JAVAJIGI);
		assertAll(() -> {
			assertThat(save.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
			assertThat(save.equalsNameAndEmail(UserTest.JAVAJIGI)).isTrue();
		});
	}

	@Test
	@DisplayName("사용자 1명 찾기 성공")
	public void findUserSuccess() {
		userRepository.save(UserTest.JAVAJIGI);
		Optional<User> optionalUser = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId());

		assertAll(() -> {
			assertThat(optionalUser.isPresent()).isTrue();
			User user = optionalUser.get();
			assertThat(user.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
			assertThat(user.equalsNameAndEmail(UserTest.JAVAJIGI)).isTrue();
		});

	}

	@Test
	@DisplayName("사용자 javajigi -> sanjigi update 성공")
	public void updateUserSuccess() {
		User save = userRepository.save(UserTest.JAVAJIGI);

		save.update(UserTest.JAVAJIGI, UserTest.SANJIGI);
		User updated = userRepository.save(save);

		assertAll(() -> {
			assertThat(updated.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
			assertThat(updated.equalsNameAndEmail(UserTest.SANJIGI)).isTrue();
		});
	}

	@Test
	@DisplayName("사용자 1명 삭제 성공")
	public void deleteUserSuccess() {
		User save = userRepository.save(UserTest.JAVAJIGI);
		userRepository.delete(save);

		Optional<User> optionalUser = userRepository.findByUserId(save.getUserId());

		assertThat(optionalUser.isPresent()).isFalse();

	}

}