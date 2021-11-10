package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswersTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	private User user1;
	private User user2;
	private Question question;

	@BeforeEach
	void setUp() {
		user1 = userRepository.save(UserTest.JAVAJIGI);
		user2 = userRepository.save(UserTest.SANJIGI);
		question = questionRepository.save(QuestionTest.Q1.writeBy(user1));
	}

	@DisplayName("Answers 에서 전체 삭제(deleteAll())를 하면 모든 Answer 는 삭제상태(deleted=true) 로 설정된다.")
	@Test
	void deleteAll1() throws CannotDeleteException {
		// given
		Answer answer1 = new Answer(user1, question, "Answers Contents1");
		Answer answer2 = new Answer(user1, question, "Answers Contents2");

		List<Answer> answerList = new ArrayList<>();
		answerList.add(answer1);
		answerList.add(answer2);

		answerRepository.saveAll(answerList);

		em.flush();
		em.clear();

		// when
		List<Answer> findAnswers = answerRepository.findAll();
		Answers answers = Answers.of(findAnswers);
		answers.deleteAll(user1);

		// then
		assertTrue(answers.isAllDeleted());
	}

	@DisplayName("로그인한 유저와 Answers 의 모든 Answer 의 작성자가 다르면 삭제를 할 수 없다.")
	@Test
	void deleteAll2() {
		// given
		Answer answer1 = new Answer(user1, question, "Answers Contents1");
		Answer answer2 = new Answer(user1, question, "Answers Contents2");

		List<Answer> answerList = new ArrayList<>();
		answerList.add(answer1);
		answerList.add(answer2);

		answerRepository.saveAll(answerList);

		em.flush();
		em.clear();

		// when & then
		assertThatThrownBy(() -> answer1.delete(user2)).isInstanceOf(CannotDeleteException.class);
	}
}