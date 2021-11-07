package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

	@BeforeEach
	void setUp() {
		User user1 = userRepository.save(UserTest.JAVAJIGI);
		User user2 = userRepository.save(UserTest.SANJIGI);
		Question question = questionRepository.save(QuestionTest.Q1.writeBy(user1));

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
        assertEqualsAnswer(a1, findA1.get());

		assertTrue(findA2.isPresent());
        assertEqualsAnswer(a2, findA2.get());
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
        assertEqualsAnswer(a1, notDeletedAnswer.get());
    }

    private void assertEqualsAnswer(Answer expect, Answer actual) {
		assertAll(
			() -> assertEquals(expect.getId(), actual.getId()),
			() -> assertEquals(expect.getWriter(), actual.getWriter()),
			() -> assertEquals(expect.getContents(), actual.getContents()),
			() -> assertEquals(expect.getQuestion(), actual.getQuestion()),
			() -> assertEquals(expect.isDeleted(), actual.isDeleted())
		);
	}
}
