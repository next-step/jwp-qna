package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

	public static final User JAVAJIGI = new User("javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = new User("sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Autowired
	private UserRepository users;

	@DisplayName("User 저장 : save()")
	@Test
	void save() {
		//given

		//when
		User actual = users.save(JAVAJIGI);
		User actual2 = users.save(SANJIGI);

		//then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual2).isNotNull()
		);
	}

	@DisplayName("User 조회 : findById()")
	@Test
	void findById() {
		//given
		User expected = users.save(JAVAJIGI);
		User expected2 = users.save(SANJIGI);

		//when
		User actual = users.findById(expected.id()).get();
		User actual2 = users.findById(expected2.id()).get();

		//then
		assertAll(
			() -> assertThat(actual.equals(expected)).isTrue(),
			() -> assertThat(actual2.equals(expected2)).isTrue()
		);
	}

	@DisplayName("User 조회 : findByUserId()")
	@Test
	void findByUserId() {
		//given
		User expected = users.save(JAVAJIGI);
		User expected2 = users.save(SANJIGI);

		//when
		User actual = users.findByUserId(expected.userId()).get();
		User actual2 = users.findByUserId(expected2.userId()).get();

		//then
		assertAll(
			() -> assertThat(actual.equals(expected)).isTrue(),
			() -> assertThat(actual2.equals(expected2)).isTrue()
		);
	}

	@DisplayName("User 수정 : setUserId()")
	@Test
	void setUserId() {
		//given
		User expected = users.save(JAVAJIGI);
		User modifiedUser = 유저_이메일_및_이름_수정(expected);

		//when
		expected.update(expected, modifiedUser);
		User actual = users.findById(modifiedUser.id()).get();

		//then
		assertThat(actual.equals(modifiedUser)).isTrue();
	}

	private User 유저_이메일_및_이름_수정(User expected) {
		User modifiedUser = expected;
		String modifiedEmail = "test@test.com";
		String modifiedName = "테스터";
		modifiedUser.changeEmail(modifiedEmail);
		modifiedUser.changeName(modifiedName);
		return modifiedUser;
	}

	@DisplayName("User 삭제 : delete()")
	@Test
	void delete() {
		//given
		User expected = users.save(JAVAJIGI);
		User beforeDeleteUser = users.findByUserId(expected.userId()).get();

		//when
		users.delete(expected);
		Optional<User> afterDeleteUserOptional = users.findByUserId(expected.userId());

		//then
		assertAll(
			() -> assertThat(beforeDeleteUser).isNotNull(),
			() -> assertThat(afterDeleteUserOptional.isPresent()).isFalse()
		);
	}
}
