package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
	@Autowired
	private UserRepository users;

	@Test
	@DisplayName("jpql 사용)")
	void select_name_by_email_using_jpql() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

		User saveA1 = users.save(JAVAJIGI);
		User saveA2 = users.save(SANJIGI);

		users.findByEmail("javajigi");

		assertThat((String)users.findByEmail("javajigi").get(0)[0]).isEqualTo("javajigi");
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByUserId)")
	void use_written_method_findByUserId() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

		User saveA1 = users.save(JAVAJIGI);
		User saveA2 = users.save(SANJIGI);

		assertThat(users.findByUserId(saveA1.getUserId()).get()).isEqualTo(
			users.findById(saveA1.getId()).get());
		assertThat(users.findByUserId(saveA2.getUserId()).get()).isEqualTo(
			users.findById(saveA2.getId()).get());
	}
}
