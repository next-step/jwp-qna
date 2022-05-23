package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    @BeforeEach
    void setUp() {
        UserTest.JAVAJIGI.setId(null);
        userRepository.save(UserTest.JAVAJIGI);
        A1.toQuestion(questionRepository.save(QuestionTest.Q1));
    }


    @DisplayName("create_at, deleted 필드 값을 null 을 가질수 없다.")
    @Test
    void createTest() {
        Answer expected = answerRepository.save(A1);
        assertAll(
                () -> assertThat(expected.getCreatedAt()).isNotNull(),
                () -> assertThat(expected.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("저장값 비교하기")
    @Test
    void identityTest() {
        Answer expected = answerRepository.save(A1);
        Answer answer = answerRepository.findById(expected.getId()).get();
        assertAll(
                () -> assertThat(expected.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(expected.getId()).isEqualTo(answer.getId()),
                () -> assertThat(expected.getUpdatedAt()).isEqualTo(answer.getUpdatedAt()),
                () -> assertThat(expected.getCreatedAt()).isEqualTo(answer.getCreatedAt()),
                () -> assertThat(expected.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(expected.getQuestion()).isEqualTo(answer.getQuestion())
        );
    }


    @DisplayName("변경하기 ")
    @Test
    void updateTest(){
        Answer savedAnswer = answerRepository.save(A1);
        savedAnswer.setContents("Answers change");
        Optional<Answer> isSavedAnswer = answerRepository.findById(savedAnswer.getId());
        assertThat(isSavedAnswer.isPresent()).isTrue();
        assertThat(isSavedAnswer.get().getContents()).isEqualTo(savedAnswer.getContents());
    }

    @DisplayName("답변과 사용자 간의 다대일 단방향 테스트")
    @Test
    void manyToOneAnswerAndUserTest() {
        Answer savedAnswer = answerRepository.save(A1);
        assertThat(savedAnswer.getWriter()).isSameAs(UserTest.JAVAJIGI);
    }

    @DisplayName("Answer 와 Question 간의 다대일 관계 테스트")
    @Test
    void manyToOneAnswerAndQuestionTest() {
        Answer savedAnswer = answerRepository.save(A1);
        assertThat(savedAnswer.getQuestion()).isSameAs(A1.getQuestion());
    }
}
