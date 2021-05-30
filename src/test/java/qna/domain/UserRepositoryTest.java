package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

	private static final String EMPTY_ENTITY_MESSAGE = "찾는 Entity가 없습니다.";

	@Autowired
	private UserRepository users;

	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		// when
		User javajigi = users.save(JAVAJIGI);

		// then
		assertAll(() -> {
			assertThat(javajigi.getId()).isNotNull();
			assertThat(javajigi.getUserId()).isEqualTo(JAVAJIGI.getUserId());
			assertThat(javajigi.getPassword()).isEqualTo(JAVAJIGI.getPassword());
			assertThat(javajigi.getName()).isEqualTo(JAVAJIGI.getName());
			assertThat(javajigi.getEmail()).isEqualTo(JAVAJIGI.getEmail());
		});
	}

	@Test
	@DisplayName("findById 테스트")
	void findByIdTest() {
		// given
		User javajigi = users.save(JAVAJIGI);

		// when
		assertThat(users.findById(javajigi.getId())
						  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.isSameAs(javajigi); // then
	}

	@Test
	@DisplayName("user_id 컬럼으로 User 조회 테스트")
	void findByUserIdTest() {
		// given
		User javajigi = users.save(JAVAJIGI);

		// when
		assertThat(users.findByUserId(javajigi.getUserId())
						.orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.isSameAs(javajigi); // then
	}
}
