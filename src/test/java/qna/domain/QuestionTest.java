package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);

        answerRepository.save(AnswerTest.A1);
    }

    @Test
    void saveAndFind() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        flushAndClear();

        Question question1 = questionRepository.findById(1L).get();
        Question question2 = questionRepository.findById(2L).get();

        assertAll(
                () -> assertThat(question1.getId()).isEqualTo(1L),
                () -> assertThat(question2.getId()).isEqualTo(2L)
        );
    }

    @DisplayName("연관관계 편의 메서드 테스트")
    @Test
    void questionAddAnswers() {
        Q1.addAnswer(AnswerTest.A1);
        questionRepository.save(Q1);
        flushAndClear();

        Question question = questionRepository.findById(1L).get();

        assertThat(question.getAnswers()).contains(AnswerTest.A1);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
