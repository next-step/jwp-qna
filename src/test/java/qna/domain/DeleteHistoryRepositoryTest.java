package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@Autowired
	private QuestionRepository questions;

	@Autowired
	private AnswerRepository answers;

	@Autowired
	private UserRepository users;

	private Question question;
	private Answer answer;
	private User writer;

	@BeforeEach
	void setUp() {
		writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
		question = new Question("title1", "contents1").writeBy(writer);
		answer =  new Answer(writer, question, "내용");

		users.save(writer);
		questions.save(question);
		answers.save(answer);
	}


	@Test
	@DisplayName("DeleteHistory 저장")
	void save() {
		DeleteHistory questionDeleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now());
		DeleteHistory answerDeleteHistory = new DeleteHistory(ContentType.ANSWER, answer.getId(), writer, LocalDateTime.now());
		DeleteHistory actualQuestionDeleteHistory = deleteHistories.save(questionDeleteHistory);
		DeleteHistory actualAnswerDeleteHistory = deleteHistories.save(answerDeleteHistory);
		assertThat(deleteHistories.findById(actualQuestionDeleteHistory.getId()).isPresent()).isTrue();
		assertThat(deleteHistories.findById(actualAnswerDeleteHistory.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("DeleteHistory 조회")
	void findByID() {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now());
		DeleteHistory saved = deleteHistories.save(deleteHistory);
		assertThat(deleteHistories.findById(saved.getId()).isPresent()).isTrue();
		assertThat(deleteHistories.findById(saved.getId()).get()).isSameAs(saved);
	}

	@Test
	@DisplayName("DeleteHistory 삭제")
	void delete() {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now());
		DeleteHistory saved = deleteHistories.save(deleteHistory);
		long savedId = saved.getId();
		deleteHistories.delete(saved);
		deleteHistories.flush();
		assertThat(deleteHistories.findById(savedId).isPresent()).isFalse();
	}

}
