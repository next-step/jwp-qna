package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Question 을 생성하여 저장한다.")
    @Test
    void save() {
        // when
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        em.flush();
        em.clear();

        // then
        Optional<Question> findQ1 = questionRepository.findById(q1.getId());
        Optional<Question> findQ2 = questionRepository.findById(q2.getId());

        assertTrue(findQ1.isPresent());
        assertEqualsQuestion(q1, findQ1.get());

        assertTrue(findQ2.isPresent());
        assertEqualsQuestion(q2, findQ2.get());
    }

    @DisplayName("삭제되지 않은 Question 목록을 조회한다.")
    @Test
    void find1() {
        // given
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        em.flush();
        em.clear();

        // when
        List<Question> notDeletedQuestions = questionRepository.findByDeletedFalse();

        // then
        assertThat(notDeletedQuestions.size()).isEqualTo(2);
    }

    @DisplayName("ID 로 삭제되지 않은 Question 을 조회한다.")
    @Test
    void find2() {
        // given
        Question q1 = questionRepository.save(Q1);

        em.flush();
        em.clear();

        // when
        Optional<Question> notDeleteQuestion = questionRepository.findByIdAndDeletedFalse(q1.getId());

        // then
        assertTrue(notDeleteQuestion.isPresent());
        assertEqualsQuestion(q1, notDeleteQuestion.get());
    }

    private void assertEqualsQuestion(Question expect, Question actual) {
        assertAll(
            () -> assertEquals(expect.getId(), actual.getId()),
            () -> assertEquals(expect.getTitle(), actual.getTitle()),
            () -> assertEquals(expect.getContents(), actual.getContents()),
            () -> assertEquals(expect.getWriterId(), actual.getWriterId())
        );
    }
}
