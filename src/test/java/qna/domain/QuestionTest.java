package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import qna.CannotDeleteException;

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
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(UserTest.JAVAJIGI);
        user2 = userRepository.save(UserTest.SANJIGI);

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
        questionRepository.save(Q1);
        questionRepository.save(Q2);

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

    @DisplayName("로그인한 유저가 Question 작성자가 아니면 Question 을 삭제할 수 없다.")
    @Test
    void delete1() {
        // when & then
        assertThatThrownBy(() -> Q1.delete(user2)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("로그인한 유저가 Question 작성자이면서 Answer 가 없으면 삭제할 수 있다.")
    @Test
    void delete2() throws CannotDeleteException {
        // when
        Q1.delete(user1);

        // then
        assertTrue(Q1.isDeleted());
    }

    @DisplayName("로그인한 유저가 Question 작성자이면서 모든 Answer 의 작성자인 경우 삭제할 수 있다.")
    @Test
    void delete3() throws CannotDeleteException {
        // given
        Question question = questionRepository.save(Q1);
        Answer answer1 = answerRepository.save(new Answer(user1, question, "Answers Contents1"));
        Answer answer2 = answerRepository.save(new Answer(user1, question, "Answers Contents2"));

        question.addAnswer(answer1);
        question.addAnswer(answer2);

        // when
        Q1.delete(user1);

        // then
        assertTrue(Q1.isDeleted());
    }

    @DisplayName("로그인한 유저가 Question 의 모든 Answer 의 작성자가 아니면 Question 을 삭제할 수 없다.")
    @Test
    void delete4() throws CannotDeleteException {
        // given
        Question question = questionRepository.save(Q1);
        Answer answer1 = answerRepository.save(new Answer(user1, question, "Answers Contents1"));
        Answer answer2 = answerRepository.save(new Answer(user2, question, "Answers Contents2"));

        question.addAnswer(answer1);
        question.addAnswer(answer2);

        // when
        assertThatThrownBy(() -> Q1.delete(user2)).isInstanceOf(CannotDeleteException.class);
    }
}
