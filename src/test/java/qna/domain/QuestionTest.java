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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(UserTest.JAVAJIGI);
        User user2 = userRepository.save(UserTest.SANJIGI);

        Q1.writeBy(user1);
        Q2.writeBy(user2);
    }

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
        assertEquals(q1, findQ1.get());

        assertTrue(findQ2.isPresent());
        assertEquals(q2, findQ2.get());
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
        assertEquals(q1, notDeleteQuestion.get());
    }
}
