package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@DisplayName("Answer 테스트")
class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private static Stream<Arguments> testAnswers() {
        return Stream.of(Arguments.of(A1), Arguments.of(A2));
    }

    @DisplayName("Save 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @MethodSource("testAnswers")
    void save_확인(Answer expectedResult) {
        Answer result = answerRepository.save(expectedResult);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectedResult.getId()),
                () -> assertThat(result.getWriterId()).isEqualTo(expectedResult.getWriterId()),
                () -> assertThat(result.getQuestionId()).isEqualTo(expectedResult.getQuestionId()),
                () -> assertThat(result.getContents()).isEqualTo(expectedResult.getContents()),
                () -> assertThat(result.isDeleted()).isEqualTo(expectedResult.isDeleted())
        );
    }
}
