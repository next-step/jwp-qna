package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@DataJpaTest
@DisplayName("답변")
public class AnswerTest {

    public static final Answer A1 =
        Answer.of(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

    public static final Answer A2 =
        Answer.of(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Answer.of(UserTest.SANJIGI, QuestionTest.Q1, "Contents"));
    }

    @Test
    @DisplayName("작성자 없이 객체화하면 UnAuthorizedException")
    void instance_nullWriter_thrownUnAuthorizedException() {
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> Answer.of(null, QuestionTest.Q1, "Contents"));
    }

    @Test
    @DisplayName("질문 없이 객체화하면 NotFoundException")
    void instance_nullQuestion_thrownNotFoundException() {
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> Answer.of(UserTest.JAVAJIGI, null, "Contents"));
    }

    private static Stream<Arguments> isNotOwner() {
        return Stream.of(Arguments.of(UserTest.SANJIGI, true),
            Arguments.of(UserTest.JAVAJIGI, false));
    }

    @ParameterizedTest(name = "{displayName}[{index}] it is {1} that answerWrittenJavajigi is not Owner by {0}")
    @MethodSource
    @DisplayName("본인의 답변이 아닌지 판단")
    void isNotOwner(User writer, boolean expected) {
        //given
        Answer answerWrittenJavajigi = Answer.of(UserTest.JAVAJIGI, QuestionTest.Q1, "Contents");

        //when
        boolean isNotOwner = answerWrittenJavajigi.isNotOwner(writer);

        //then
        assertThat(isNotOwner)
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        //given
        User expectedWriter = UserTest.SANJIGI;
        Answer answer = Answer.of(expectedWriter, QuestionTest.Q1, "Contents");

        //when
        DeleteHistory history = answer.delete();

        //then
        assertAll(
            () -> assertThat(answer.isDeleted()).isTrue(),
            () -> assertThat(history).isNotNull()
        );
    }
}
