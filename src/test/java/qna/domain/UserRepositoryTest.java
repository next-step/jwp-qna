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
		userRepository.saveAll(Arrays.asList(JAVAJIGI,SANJIGI));
	}

	@Test
	@DisplayName("단건의 유저 이메일을 변경되는지 확인")
	void give_User_when_changeEmail_then_changedEqualsEmail() {
		// given
		Optional<User> byUserId = userRepository.findByUserId(JAVAJIGI.getUserId());
		User user = byUserId.get();

		// when
		final String updateEmail = "seunghoo@naver.com";
		user.setEmail(updateEmail);

		// then
		Assertions.assertThat(user.getEmail()).isEqualTo(updateEmail);
	}

	@Test
	@DisplayName("이름으로 유저이름 조회시 동일한 이름 데이터가 조회 되는지 확인")
	void when_findUserName_then_sameUserName() {
		// when
		User user = userRepository.findByName("javajigi").get();

		// then
		Assertions.assertThat(user.getName()).isEqualTo(JAVAJIGI.getName());
	}

}
