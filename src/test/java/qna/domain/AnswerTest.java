package qna.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeAll
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    private static Stream<Arguments> provideAnswers() {
        return Stream.of(
            Arguments.of(A1),
            Arguments.of(A2)
        );
    }

    private static Stream<Arguments> provideQuestionIdsAndAnswers() {
        return Stream.of(
            Arguments.of(Q1.getId(), A1),
            Arguments.of(Q1.getId(), A2)
        );
    }

    @DisplayName("save메서드에 삽입을 원하는 Answer객체를 인자로 호출하면, 삽입된 객체를 반환한다.")
    @Test
    void saveTest() {
        Answer expected = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        Answer actual = answerRepository.save(expected);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findById 메서드를 이용하여 Answer 객체의 id를 입력 받으면 해당 Answer 객체를 리턴한다.")
    @ParameterizedTest
    @MethodSource("provideAnswers")
    void findByIdTest(Answer expected) {
        Answer actual = answerRepository.findById(expected.getId()).get();
        assertAll(
            () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
            () -> assertThat(actual.getQuestion()).isEqualTo(expected.getQuestion()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalseTest 메서드를 이용하여 Question 객체의 id를 입력 받으면, 해당 조건에 맞는 Answer 객체 콜렉션을 리턴한다.")
    @ParameterizedTest
    @MethodSource("provideQuestionIdsAndAnswers")
    void findByQuestionIdAndDeletedFalseTest(Long questionId, Answer expected) {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        assertThat(answers).contains(expected);
    }

    @DisplayName("Answer객체의 id와 deleted가 false인 객체를 입력으로 하는 findByIdAndDeletedFalse메서드를 호출하면, 해당 조건에 맞는 Optional<Answer> 객체를 리턴한다.")
    @ParameterizedTest
    @MethodSource("provideAnswers")
    void findByIdAndDeletedFalseTest(Answer expected) {
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(expected.getId());
        assertThat(actual).isEqualTo(Optional.of(expected));
    }
}
