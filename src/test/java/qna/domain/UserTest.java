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
		User saveA1 = saveJavajigi();
		User saveA2 = saveSanjigi();

		users.findByEmail("javajigi");

		assertThat((String)users.findByEmail("javajigi").get(0)[0]).isEqualTo("javajigi");
	}

	private User saveJavajigi() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		return users.save(JAVAJIGI);
	}

	private User saveSanjigi() {
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		return users.save(SANJIGI);
	}
}
