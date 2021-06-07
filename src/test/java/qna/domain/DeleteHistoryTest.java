package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
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

	@BeforeEach
	void setUp() {
		deleteHistories.deleteAll();
		deleteHistories.flush();
		users.deleteAll();
		users.flush();
	}

	@Test
	@DisplayName("jpa between 조회")
	void select_between() {
		User JAVAJIGI = makeJavajigi();
		users.save(JAVAJIGI);
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, JAVAJIGI,
			LocalDateTime.of(2021, 6, 2, 22, 30));

		User SANJIGI = makeSanjigi();
		users.save(SANJIGI);
		DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, SANJIGI,
			LocalDateTime.of(2021, 6, 2, 23, 10));

		DeleteHistory saveDH1 = deleteHistories.save(DH1);
		DeleteHistory saveDH2 = deleteHistories.save(DH2);

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
		User JAVAJIGI = makeJavajigi();
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, users.save(JAVAJIGI),
			LocalDateTime.of(2021, 6, 2, 22, 30));
		DeleteHistory saveDH1 = deleteHistories.save(DH1);
		deleteHistories.flush();

		User SANJIGI = makeSanjigi();
		DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, users.save(SANJIGI),
			LocalDateTime.of(2021, 6, 2, 23, 10));
		DeleteHistory saveDH2 = deleteHistories.save(DH2);
		deleteHistories.flush();

		assertThat(
			deleteHistories.findByIdLessThan(saveDH2.getId()).get(0)).isEqualTo(saveDH1
		);
	}

	private User makeSanjigi() {
		return new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
	}

	private User makeJavajigi() {
		return new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	}
}
