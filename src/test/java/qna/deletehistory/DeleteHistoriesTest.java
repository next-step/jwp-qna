package qna.deletehistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.question.Question;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.answer.AnswerTest.A1;
import static qna.answer.AnswerTest.A2;
import static qna.user.UserTest.JAVAJIGI;

class DeleteHistoriesTest {

    private static Stream<Arguments> createDeleteHistories() {
        Question question = new Question("title1", "contents1", JAVAJIGI);
        question.addAnswer(A1);
        question.addAnswer(A2);
        return Stream.of(
                Arguments.of(DeleteHistories.fromDeleteHistoriesByQuestion(question))
        );
    }

    @ParameterizedTest
    @MethodSource("createDeleteHistories")
    @DisplayName("삭제 이력 리스트 객체 생성")
    public void createDeleteHistoriesTest(DeleteHistories expected) {
        //given
        Question actual = new Question("title1", "contents1", JAVAJIGI);
        //when
        actual.addAnswer(A1);
        actual.addAnswer(A2);
        //then
        assertThat(DeleteHistories.fromDeleteHistoriesByQuestion(actual)).isEqualTo(expected);
    }

}