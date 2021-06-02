package qna.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.user.UserTest.JAVAJIGI;
import static qna.domain.user.UserTest.SANJIGI;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

	@Autowired
	private UserRepository users;

	private User expected;

	@BeforeEach
	void setUp() {
		expected = users.save(JAVAJIGI);

		assertThat(expected).isNotNull();
	}


	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		// then
		assertAll(() -> {
			assertThat(expected.getId()).isNotNull();
			assertThatCode(() -> expected.checkOwner(JAVAJIGI)).doesNotThrowAnyException();
			assertThat(expected.equalsNameAndEmail(JAVAJIGI)).isTrue();
		});
	}

	@Test
	@DisplayName("update 테스트")
	void updateTest() {
		// when
		expected.update(expected, SANJIGI);

		// then
		assertThat(users.findById(expected.getId()))
			.isPresent()
			.get()
			.extracting(value -> value.equalsNameAndEmail(SANJIGI))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("findById 테스트")
	void findByIdTest() {
		// when
		assertThat(users.findById(expected.getId()))
			.isPresent()
			.get()
			.isSameAs(expected); // then
	}

	@Test
	@DisplayName("user_id 컬럼으로 User 조회 테스트")
	void findByUserIdTest() {
		// when
		assertThat(users.findByUserId(expected.getUserId()))
			.isPresent()
			.get()
			.isSameAs(expected); // then
	}

	@Test
	@DisplayName("삭제 테스트")
	void deleteTest() {
		// when
		users.delete(expected);

		// then
		assertThat(users.findById(expected.getId()))
			.isNotPresent();
	}
}
