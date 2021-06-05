package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.AnswerTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
	@Autowired
	private final DeleteHistoryRepository deleteHistories;
	private final UserRepository users;

	@Autowired
	public DeleteHistoryRepositoryTest(DeleteHistoryRepository deleteHistories, UserRepository users) {
		this.deleteHistories = deleteHistories;
		this.users = users;
		users.save(UserTest.JAVAJIGI);
		users.save(UserTest.SANJIGI);
	}


	@Test
	@DisplayName("저장")
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
		DeleteHistory actual = deleteHistories.save(expected);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	@DisplayName("특정 유저 삭제 내역 조회")
	void findByDeletedUser() {
		DeleteHistory history1 = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), UserTest.SANJIGI, LocalDateTime.now());
		DeleteHistory history2= new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.SANJIGI, LocalDateTime.now());
		deleteHistories.save(history1);
		deleteHistories.save(history2);

		List<DeleteHistory> histories = deleteHistories.findByDeletedUser(UserTest.SANJIGI);
		assertThat(histories.size()).isEqualTo(2);
	}
}