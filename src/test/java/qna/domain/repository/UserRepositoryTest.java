package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.User;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository users;
	private User 자바지기;
	private User 산지기;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
	}

	@Test
	void 저장() {
		//given

		//when
		User 영속화된_자바지기= users.save(자바지기);
		User 영속화된_산지기 = users.save(산지기);

		//then
		assertAll(
			() -> assertThat(영속화된_자바지기).isNotNull(),
			() -> assertThat(영속화된_산지기).isNotNull()
		);
	}

	@Test
	void 아이디로_조회() {
		//given
		User 영속화된_자바지기 = users.save(자바지기);
		User 영속화된_산지기 = users.save(산지기);

		//when
		User 조회한_자바지기 = users.findById(영속화된_자바지기.id()).get();
		User 조회한_산지기 = users.findById(영속화된_산지기.id()).get();

		//then
		assertAll(
			() -> assertThat(조회한_자바지기.equals(영속화된_자바지기)).isTrue(),
			() -> assertThat(조회한_산지기.equals(영속화된_산지기)).isTrue()
		);
	}

	@Test
	void 유저아이디로_조회() {
		//given
		User 영속화된_자바지기 = users.save(자바지기);
		User 영속화된_산지기 = users.save(산지기);

		//when
		User 조회한_자바지기 = users.findByUserId(영속화된_자바지기.userId()).get();
		User 조회한_산지기 = users.findByUserId(영속화된_산지기.userId()).get();

		//then
		assertAll(
			() -> assertThat(조회한_자바지기.equals(영속화된_자바지기)).isTrue(),
			() -> assertThat(조회한_산지기.equals(영속화된_산지기)).isTrue()
		);
	}

	@Test
	void 이메일_및_이름_수정() {
		//given
		User 영속화된_자바지기 = users.save(자바지기);
		User 수정된_자바지기 = 유저_이메일_및_이름_수정(영속화된_자바지기);

		//when
		영속화된_자바지기.update(영속화된_자바지기, 수정된_자바지기);
		User 수정후_조회한_자바지기 = users.findById(수정된_자바지기.id()).get();

		//then
		assertThat(수정후_조회한_자바지기.equals(수정된_자바지기)).isTrue();
	}

	private User 유저_이메일_및_이름_수정(User targetUser) {
		User 수정된유저 = targetUser;
		수정된유저.changeEmail("test@test.com");
		수정된유저.changeName("테스터");
		return 수정된유저;
	}

	@Test
	void 물리삭제() {
		//given
		User 영속화된_자바지기 = users.save(자바지기);

		//when
		User 조회한_자바지기 = users.findByUserId(영속화된_자바지기.userId()).get();
		users.delete(영속화된_자바지기);
		Optional<User> 삭제후_조회한_자바지기 = users.findByUserId(영속화된_자바지기.userId());

		//then
		assertAll(
			() -> assertThat(조회한_자바지기).isNotNull(),
			() -> assertThat(삭제후_조회한_자바지기.isPresent()).isFalse()
		);
	}
}
