package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository users;
	private User instanceJavajigi;
	private User instanceSanjigi;

	@BeforeEach
	void initialize() {
		instanceJavajigi = new User(1L, "javajigi", "password1", "name1",
			"javajigi@slipp.net");
		instanceSanjigi = new User(2L, "sanjigi", "password2", "name2",
			"sanjigi@slipp.net");
	}

	@DisplayName("User 저장 : save()")
	@Test
	void save() {
		//given

		//when
		User actualJavajigi = users.save(instanceJavajigi);
		User actualSanjigi = users.save(instanceSanjigi);

		//then
		assertAll(
			() -> assertThat(actualJavajigi).isNotNull(),
			() -> assertThat(actualSanjigi).isNotNull()
		);
	}

	@DisplayName("User 조회 : findById()")
	@Test
	void findById() {
		//given
		User expectedJavajigi = users.save(instanceJavajigi);
		User expectedSanjigi = users.save(instanceSanjigi);

		//when
		User actualJavajigi = users.findById(expectedJavajigi.id()).get();
		User actualSanjigi = users.findById(expectedSanjigi.id()).get();

		//then
		assertAll(
			() -> assertThat(actualJavajigi.equals(expectedJavajigi)).isTrue(),
			() -> assertThat(actualSanjigi.equals(expectedSanjigi)).isTrue()
		);
	}

	@DisplayName("User 조회 : findByUserId()")
	@Test
	void findByUserId() {
		//given
		User expectedJavajigi = users.save(instanceJavajigi);
		User expectedSanjigi = users.save(instanceSanjigi);

		//when
		User actualJavajigi = users.findByUserId(expectedJavajigi.userId()).get();
		User actualSanjigi = users.findByUserId(expectedSanjigi.userId()).get();

		//then
		assertAll(
			() -> assertThat(actualJavajigi.equals(expectedJavajigi)).isTrue(),
			() -> assertThat(actualSanjigi.equals(expectedSanjigi)).isTrue()
		);
	}

	@DisplayName("User 수정 : update()")
	@Test
	void setUserId() {
		//given
		User expectedJavajigi = users.save(instanceJavajigi);
		User modifiedJavajigi = 유저_이메일_및_이름_수정(expectedJavajigi);

		//when
		expectedJavajigi.update(expectedJavajigi, modifiedJavajigi);
		User actual = users.findById(modifiedJavajigi.id()).get();

		//then
		assertThat(actual.equals(modifiedJavajigi)).isTrue();
	}

	private User 유저_이메일_및_이름_수정(User targetUser) {
		User modifiedUser = targetUser;
		modifiedUser.changeEmail("test@test.com");
		modifiedUser.changeName("테스터");
		return modifiedUser;
	}

	@DisplayName("User 삭제 : delete()")
	@Test
	void delete() {
		//given
		User actualJavajigi = users.save(instanceJavajigi);

		//when
		User javajigiBeforeDelete = users.findByUserId(actualJavajigi.userId()).get();
		users.delete(actualJavajigi);
		Optional<User> javajigiAfterDelete = users.findByUserId(actualJavajigi.userId());

		//then
		assertAll(
			() -> assertThat(javajigiBeforeDelete).isNotNull(),
			() -> assertThat(javajigiAfterDelete.isPresent()).isFalse()
		);
	}
}
