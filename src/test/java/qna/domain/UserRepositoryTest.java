package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository users;

	@Test
	@DisplayName("User 저장 테스트")
	void save() {
		User saved = users.save(UserTest.JAVAJIGI);
		assertThat(saved.getUserId()).isEqualTo("javajigi");
		assertThat(saved.getPassword()).isEqualTo("password");
		assertThat(saved.getName()).isEqualTo("name");
		assertThat(saved.getEmail()).isEqualTo("javajigi@slipp.net");
	}

	@Test
	@DisplayName("User 조회 테스트")
	void findById() {
		users.save(UserTest.JAVAJIGI);
		User actual = users.findByUserId("javajigi").get();
		assertThat(actual.getUserId()).isEqualTo("javajigi");
		assertThat(actual.getPassword()).isEqualTo("password");
		assertThat(actual.getName()).isEqualTo("name");
		assertThat(actual.getEmail()).isEqualTo("javajigi@slipp.net");
	}

	@Test
	@DisplayName("User 수정 테스트")
	void update() {
		User saved = users.save(UserTest.JAVAJIGI);
		saved.setName("kwaktaemin");
		saved.setEmail("taminging@kakao.com");
		User updated = users.findByUserId("javajigi").get();
		users.flush();
		assertThat(updated.getUserId()).isEqualTo("javajigi");
		assertThat(updated.getPassword()).isEqualTo("password");
		assertThat(updated.getName()).isEqualTo("kwaktaemin");
		assertThat(updated.getEmail()).isEqualTo("taminging@kakao.com");
	}

	@Test
	@DisplayName("User 삭제 테스트")
	void delete() {
		User saved = users.save(UserTest.JAVAJIGI);
		users.delete(saved);
		users.flush();
		assertThat(users.findByUserId(saved.getUserId()).isPresent()).isFalse();
	}
}
