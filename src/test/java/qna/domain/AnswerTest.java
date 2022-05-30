package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @DisplayName("삭제시 DeleteHistory 객체를 반환한다.")
    @Test
    void remove() throws CannotDeleteException {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        Answer savedAnswer = answerRepository.save(new Answer(savedUser, QuestionTest.Q1, "test content"));
        assertThat(savedAnswer.delete(savedUser)).isEqualTo(new DeleteHistory(ContentType.ANSWER,savedAnswer.getId(),savedUser, LocalDateTime.now()));
    }

    @DisplayName("삭제시 사용자가 다르면 에러를 발생한다.")
    @Test
    void removeDifferenceUser() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        Answer savedAnswer = answerRepository.save(new Answer(savedUser, QuestionTest.Q1, "test content"));
        assertThatThrownBy(() -> savedAnswer.delete(UserTest.SANJIGI))
                .isExactlyInstanceOf(CannotDeleteException.class);
    }
}
