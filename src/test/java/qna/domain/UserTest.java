package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("사용자")
public class UserTest {
	@DisplayName("사용자를 생성할 수 있다.")
	@ParameterizedTest
	@CsvSource(value = {"y2o2u2n@gmail.com,youn,password,y2o2u2n"})
	void of(String email, String name, String password, String userId) {
		// given & when
		User user = User.of(email, name, password, userId);

		// then
		assertAll(
			() -> assertThat(user).isNotNull(),
			() -> assertThat(user.getEmail()).isEqualTo(Email.of(email)),
			() -> assertThat(user.getName()).isEqualTo(UserName.of(name)),
			() -> assertThat(user.getPassword()).isEqualTo(UserPassword.of(password)),
			() -> assertThat(user.getUserId()).isEqualTo(UserId.of(userId))
		);
	}
}
