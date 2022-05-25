package qna.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Long id = 0L;

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        UserTest.JAVAJIGI.setId(null);
        UserTest.SANJIGI.setId(null);
        question = new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents());
        answer = new Answer(userRepository.save(UserTest.SANJIGI), question, A1.getContents());
        question = questionRepository.save(question);
    }

    @DisplayName("create_at, deleted 필드 값을 null 을 가질수 없다.")
    @Test
    void createTest() {
        Answer expected = answerRepository.save(new Answer());
        assertAll(
                () -> assertThat(expected.getCreatedAt()).isNotNull(),
                () -> assertThat(expected.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("저장값 비교 하기")
    @Test
    void identityTest() {
        Answer savedAnswer = answerRepository.save(answer);
        assertThat(answerRepository.findById(savedAnswer.getId()).get()).isEqualTo(savedAnswer);
    }

    @DisplayName("값 변경 하기")
    @Test
    void updateTest() {
        Answer savedAnswer = answerRepository.save(answer);
        savedAnswer.setContents("Answers change");
        Answer answer = answerRepository.findById(savedAnswer.getId()).get();
        assertThat(answer.getContents()).isEqualTo(savedAnswer.getContents());
    }

    @DisplayName("답변과 사용자 간의 다대일 단방향 테스트")
    @Test
    void manyToOneAnswerAndUserTest() {
        Answer savedAnswer = answerRepository.save(answer);
        assertThat(savedAnswer.getWriter()).isSameAs(userRepository.findById(answer.getWriter().getId()).get());
    }

    @DisplayName("Answer 와 Question 간의 다대일 관계 테스트")
    @Test
    void manyToOneAnswerAndQuestionTest() {
        assertThat(answerRepository.findById(answer.getId()).get().getQuestion()).isEqualTo(question);
    }
}
