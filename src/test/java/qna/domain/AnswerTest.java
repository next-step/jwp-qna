package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("Answer 도메인 생성 테스트")
    void generate01(){
        // given && when
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        em.flush();
        em.clear();

        // then
        Optional<Answer> findA1 = answerRepository.findById(a1.getId());
        Optional<Answer> findA2 = answerRepository.findById(a2.getId());

        assertAll(
            () -> assertTrue(findA1.isPresent()),
            () -> assertEqualsAnswer(a1, findA1.get()),
            () -> assertTrue(findA2.isPresent())
        );
    }

    private void assertEqualsAnswer(Answer expect, Answer actual) {
        assertAll(
            () -> assertEquals(expect.getId(), actual.getId()),
            () -> assertEquals(expect.getWriterId(), actual.getWriterId()),
            () -> assertEquals(expect.getQuestionId(), actual.getQuestionId()),
            () -> assertEquals(expect.getContents(), actual.getContents()),
            () -> assertEquals(expect.isDeleted(), actual.isDeleted())
        );
    }
}
