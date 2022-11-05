package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Test
    @DisplayName("사용자가 null이면 UnAuthorized exception발생")
    void test1() {
        assertThatThrownBy(()-> new Answer(null, Q1, "some contents"))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }
    @Test
    @DisplayName("Question이 null이면 NotFound exception발생")
    void test2() {
        assertThatThrownBy(()-> new Answer(JAVAJIGI, null, "some contents"))
                .isExactlyInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @MethodSource("writerSource")
    @DisplayName("작성자가 같다면  True,아니라면 False 를 반환함")
    void test3(User user, boolean result) {
        assertThat(A1.isOwner(user)).isEqualTo(result);
    }

    @Test
    @DisplayName("Question Id가 정상적으로 설정됨")
    void test4() {
        A1.toQuestion(Q2);

        assertThat(A1.getQuestionId()).isEqualTo(Q1.getId());
    }

    @Test
    @DisplayName("Contents가 정상적으로 설정됨")
    void test7() {
        A1.updateContents("changed");

        assertThat(A1.getContents()).isEqualTo("changed");
    }

    @Test
    @DisplayName("deleted가 정상적으로 설정됨")
    void test8() {
        A1.markDeleted(true);

        assertThat(A1.isDeleted()).isTrue();
    }

    private static Stream<Arguments> writerSource(){
        return Stream.of(
                Arguments.of(JAVAJIGI,true),
                Arguments.of(SANJIGI,false)
        );
    }
}
