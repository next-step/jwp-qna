package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {
	private User JAVAJIGI;
	private User SANJIGI;
	private Question Q1;
	private Question Q2;
	private Answer A1;
	private Answer A2;
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		JAVAJIGI = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
		SANJIGI = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
		Q1 = questionRepository.save(new Question("title1", "contents1").writeBy(JAVAJIGI));
		Q2 = questionRepository.save(new Question("title2", "contents2").writeBy(SANJIGI));
		A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
		A2 = new Answer(SANJIGI, Q1, "Answers Contents2");
	}

	@Test
	@DisplayName("답변을 저장한다.")
	void save() {
		Answer actual = answerRepository.save(A1);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(A1.getContents())
		);
	}

	@Test
	@DisplayName("답변이 작성된 질문 가져오기")
	void findAnswerWithQuestion() {
		final Answer actual = answerRepository.save(A1);
		final Question question = questionRepository.findById(actual.getQuestion().getId()).get();
		assertThat(actual.getQuestion()).isEqualTo(question);
	}

	@Test
	@DisplayName("답변을 작성한 유저 가져오기")
	void findAnswerWithUser() {
		final Answer actual = answerRepository.save(A1);
		final User user = userRepository.findByUserId(actual.getWriter().getUserId()).get();
		assertThat(actual.getWriter()).isEqualTo(user);
	}

	@Test
	@DisplayName("질문 ID의 삭제되지 않은 답변 목록을 가져온다.")
	void findByQuestionIdAndDeletedFalse() {
		final Answer answer = answerRepository.save(A1);
		final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(
			answer.getQuestion().getId());

		assertThat(actual).contains(answer);
	}

	@Test
	@DisplayName("삭제되지 않은 주어진 id의 질문을 가져온다.")
	void findByIdAndDeletedFalse() {
		final Answer answer = answerRepository.save(A1);
		final Answer actual = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

		assertThat(actual).isEqualTo(answer);
	}
}
