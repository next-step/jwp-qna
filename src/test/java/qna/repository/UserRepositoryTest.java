package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.User;

@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	UserRepository users;

	@Test
	@DisplayName("저장")
	void save() {
		User expected = new User("dohchoi91", "password", "name", "dohchoi91@slipp.net");
		User actual = users.save(expected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
	}

	@Test
	@DisplayName("유저 아이디를 이용한 조회")
	void findByUserId() {
		User expected = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		users.save(expected);
		User actual = users.findByUserId("javajigi").orElse(null);

		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
		);
	}
}