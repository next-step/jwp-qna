package qna.domain;

import static qna.domain.UserTest.*;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
	}

	@Test
	@DisplayName("유저 이메일이 변경되는지 확인")
	void give_User_when_changeEmail_then_changedEqualsEmail() {
		// given
		Optional<User> byUserId = userRepository.findByUserId(JAVAJIGI.getUserId());
		User expectedUser = byUserId.get();

		// when
		final String updateEmail = "seunghoo@naver.com";
		expectedUser.setEmail(updateEmail);

		// then
		Assertions.assertThat(expectedUser.getEmail()).isEqualTo(updateEmail);
	}

	@Test
	@DisplayName("이름으로 조회시 데이터가 조회 되는지 확인")
	void when_findUserName_then_sameUserName() {
		// when
		User expectedUser = userRepository.findByName("javajigi").get();

		// then
		Assertions.assertThat(expectedUser.getName()).isEqualTo(JAVAJIGI.getName());
	}

	@Test
	@DisplayName("유저 비밀번호를 변경시 변경되는지 확인")
	void given_user_whenChangePassword_then_isTrue() {

		//given
		User user = userRepository.findByName("javajigi").get();
		String changePassword = "changePassword";

		// when
		user.setPassword(changePassword);
		User expectedUser = userRepository.findByUserId(user.getUserId()).get();

		// then
		Assertions.assertThat(expectedUser.getPassword().equals(changePassword)).isTrue();
	}
}
