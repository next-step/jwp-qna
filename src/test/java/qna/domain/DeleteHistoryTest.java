package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@Autowired
	UserRepository users;

	@Test
	@DisplayName("삭제를 조회하고 삭제한 사람을 조회한다.")
	void select_not_deleted_question_with_writer() {
		DeleteHistory saveDH1 = saveDeleteHistory1(saveJavajigi());

		assertThat(saveDH1.getDeleter().getUserId()).isEqualTo("javajigi");
	}

	@Test
	@DisplayName("jpa between 조회")
	void select_between() {
		DeleteHistory saveDH1 = saveDeleteHistory1(saveJavajigi());
		DeleteHistory saveDH2 = saveDeleteHistory2(saveSanjigi());

		assertThat(
			deleteHistories.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 22, 40)).size()).isEqualTo(
			1);
		assertThat(
			deleteHistories.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 23, 40)).size()).isEqualTo(
			2);
	}

	@Test
	@DisplayName("jpa less than 조회")
	void select_less_than() {
		DeleteHistory saveDH1 = saveDeleteHistory1(saveJavajigi());
		DeleteHistory saveDH2 = saveDeleteHistory2(saveSanjigi());

		assertThat(
			deleteHistories.findByIdLessThan(saveDH2.getId()).get(0)).isEqualTo(saveDH1
		);
	}

	private User saveJavajigi() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		return users.save(JAVAJIGI);
	}

	private User saveSanjigi() {
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		return users.save(SANJIGI);
	}

	private DeleteHistory saveDeleteHistory1(User user) {
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, users.save(user),
			LocalDateTime.of(2021, 6, 2, 22, 30));
		return deleteHistories.save(DH1);
	}

	private DeleteHistory saveDeleteHistory2(User user) {
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, users.save(user),
			LocalDateTime.of(2021, 6, 2, 23, 10));
		return deleteHistories.save(DH1);
	}
}
