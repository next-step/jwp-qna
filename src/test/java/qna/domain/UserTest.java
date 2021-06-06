package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Autowired
	private UserRepository users;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		모든_레포지토리_데이터_삭제();
	}

	private void 모든_레포지토리_데이터_삭제() {
		users.deleteAll();
	}

	@DisplayName("User 저장 : save()")
	@Test
	@Order(1)
	void save() {
		//given

		//when
		User actual = users.save(JAVAJIGI);
		User actual2 = users.save(SANJIGI);

		//then
		assertAll(
			() -> assertThat(actual.id()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(JAVAJIGI)).isTrue(),
			() -> assertThat(actual.password()).isEqualTo(JAVAJIGI.password()),
			() -> assertThat(actual.userId()).isEqualTo(JAVAJIGI.userId()),
			() -> assertThat(actual2.id()).isNotNull(),
			() -> assertThat(actual2.equalsNameAndEmail(SANJIGI)).isTrue(),
			() -> assertThat(actual2.password()).isEqualTo(SANJIGI.password()),
			() -> assertThat(actual2.userId()).isEqualTo(SANJIGI.userId())
		);
	}

	@DisplayName("User 조회 : findById()")
	@Test
	@Order(2)
	void findById() {
		//given
		User expected = users.save(JAVAJIGI);
		User expected2 = users.save(SANJIGI);

		//when
		User actual = users.findById(expected.id()).get();
		User actual2 = users.findById(expected2.id()).get();

		//then
		assertAll(
			() -> assertThat(actual.id()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(expected)).isTrue(),
			() -> assertThat(actual.password()).isEqualTo(expected.password()),
			() -> assertThat(actual.userId()).isEqualTo(expected.userId()),
			() -> assertThat(actual2.id()).isNotNull(),
			() -> assertThat(actual2.equalsNameAndEmail(expected2)).isTrue(),
			() -> assertThat(actual2.password()).isEqualTo(expected2.password()),
			() -> assertThat(actual2.userId()).isEqualTo(expected2.userId())
		);
	}

	@DisplayName("User 조회 : findByUserId()")
	@Test
	@Order(3)
	void findByUserId() {
		//given
		User expected = users.save(JAVAJIGI);
		User expected2 = users.save(SANJIGI);

		//when
		User actual = users.findByUserId(expected.userId()).get();
		User actual2 = users.findByUserId(expected2.userId()).get();

		//then
		assertAll(
			() -> assertThat(actual.id()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(expected)).isTrue(),
			() -> assertThat(actual.password()).isEqualTo(expected.password()),
			() -> assertThat(actual.userId()).isEqualTo(expected.userId()),
			() -> assertThat(actual2.id()).isNotNull(),
			() -> assertThat(actual2.equalsNameAndEmail(expected2)).isTrue(),
			() -> assertThat(actual2.password()).isEqualTo(expected2.password()),
			() -> assertThat(actual2.userId()).isEqualTo(expected2.userId())
		);
	}

	@DisplayName("User 수정 : setUserId()")
	@Test
	@Order(4)
	void setUserId() {
		//given
		User expected = users.save(JAVAJIGI);
		User modifiedUser = expected;
		String modifiedEmail = "test@test.com";
		String modifiedName = "테스터";
		modifiedUser.changeEmail(modifiedEmail);
		modifiedUser.changeName(modifiedName);

		//when
		expected.update(expected, modifiedUser);
		User actual = users.findById(modifiedUser.id()).get();

		//then
		assertThat(actual.equals(modifiedUser)).isTrue();
	}

	@DisplayName("User 삭제 : delete()")
	@Test
	@Order(5)
	void delete() {
		//given
		User expected = users.save(JAVAJIGI);
		User beforeDeleteUser = users.findByUserId(expected.userId()).get();

		//when
		users.delete(expected);
		users.flush();
		Optional<User> afterDeleteUserOptional = users.findByUserId(expected.userId());

		//then
		assertAll(
			() -> assertThat(beforeDeleteUser).isNotNull(),
			() -> assertThat(afterDeleteUserOptional.isPresent()).isFalse()
		);
	}
}
