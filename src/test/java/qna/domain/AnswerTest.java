package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

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
public class AnswerTest {
	public static Answer A1;
	public static Answer A2;

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

		A1 = new Answer(user1, question, "Answers Contents1");
		A2 = new Answer(user2, question, "Answers Contents2");
	}

	@DisplayName("Answer 을 생성하여 저장한다.")
	@Test
	void save() {
		// when
		Answer a1 = answerRepository.save(A1);
		Answer a2 = answerRepository.save(A2);

		em.flush();
		em.clear();

		// then
		Optional<Answer> findA1 = answerRepository.findById(a1.getId());
		Optional<Answer> findA2 = answerRepository.findById(a2.getId());

		assertTrue(findA1.isPresent());
		assertEquals(a1, findA1.get());

		assertTrue(findA2.isPresent());
		assertEquals(a2, findA2.get());
	}

    @DisplayName("Question ID 로 삭제되지 않은 Answer 목록을 조회한다.")
    @Test
    void find1() {
        // given
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        em.flush();
        em.clear();

        // when
        List<Answer> notDeletedAnswers = answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestion().getId());

        // then
        assertThat(notDeletedAnswers.size()).isEqualTo(2);
    }

    @DisplayName("ID 로 삭제되지 않은 Answer 를 조회한다.")
    @Test
    void find2() {
        // given
        Answer a1 = answerRepository.save(A1);

        em.flush();
        em.clear();

        // when
        Optional<Answer> notDeletedAnswer = answerRepository.findByIdAndDeletedFalse(a1.getId());

        // then
        assertTrue(notDeletedAnswer.isPresent());
		assertEquals(a1, notDeletedAnswer.get());
    }

	@DisplayName("Question 의 작성자와 Answer 의 작성자가 같으면 Answer 를 삭제할 수 있다.")
	@Test
	void delete1() throws CannotDeleteException {
		// given
		Answer answer1 = answerRepository.save(new Answer(user1, question, "Answers Contents1"));
		Answer answer2 = answerRepository.save(new Answer(user1, question, "Answers Contents2"));

		question.addAnswer(answer1);
		question.addAnswer(answer2);

		// when
		answer1.delete(user1);

		// then
		assertTrue(answer1.isDeleted());
	}

	@DisplayName("Question 의 작성자와 Answer 의 작성자가 다르면 Answer 를 삭제할 수 없다.")
	@Test
	void delete2() {
		// given
		Answer answer1 = answerRepository.save(new Answer(user2, question, "Answers Contents1"));
		question.addAnswer(answer1);

		// when & then
		assertThatThrownBy(() -> answer1.delete(user2)).isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("로그인한 유저와 Answer 의 작성자가 다르면 Answer 를 삭제할 수 없다.")
	@Test
	void delete3() {
		// given
		Answer answer1 = answerRepository.save(new Answer(user1, question, "Answers Contents1"));
		question.addAnswer(answer1);

		// when & then
		assertThatThrownBy(() -> answer1.delete(user2)).isInstanceOf(CannotDeleteException.class);
	}
}
