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
public class QuestionRepositoryTest {
	private User JAVAJIGI;
	private User SANJIGI;
	private Question Q1;
	private Question Q2;
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		JAVAJIGI = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
		SANJIGI = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
		Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Q2 = new Question("title2", "contents2").writeBy(SANJIGI);
	}

	@Test
	@DisplayName("질문을 저장한다.")
	void save() {
		Question actual = questionRepository.save(Q1);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle())
		);
	}

	@Test
	@DisplayName("질문을 작성한 유저 가져오기")
	void findQuestionWithUser() {
		final Question actual = questionRepository.save(Q1);
		final User user = userRepository.findByUserId(actual.getWriter().getUserId()).get();
		assertThat(actual.getWriter()).isEqualTo(user);
	}

	@Test
	@DisplayName("작성자 ID의 질문을 가져온다.")
	void findByWriterId() {
		final Question question = questionRepository.save(Q1);
		final List<Question> actual = questionRepository.findByWriterId(JAVAJIGI.getId());

		assertThat(actual.get(0)).isEqualTo(question);
	}

	@Test
	@DisplayName("삭제되지 않은 질문 목록을 가져온다.")
	void findByDeletedFalse() {
		final Question question1 = questionRepository.save(Q1);
		final Question question2 = questionRepository.save(Q2);
		final List<Question> actual = questionRepository.findByDeletedFalse();

		assertThat(actual).contains(question1, question2);
	}

	@Test
	@DisplayName("삭제되지 않은 주어진 id의 질문을 가져온다.")
	void find() {
		final Question question = questionRepository.save(Q1);
		final Question actual = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

		assertThat(actual).isEqualTo(question);
	}
}
