package qna.deletehistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.question.Answers;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.answer.AnswerTest.A1;
import static qna.answer.AnswerTest.A2;
import static qna.question.QuestionTest.Q1;

class DeleteHistoriesTest {

    private static Stream<Arguments> createDeleteHistories() {
        return Stream.of(
                Arguments.of(DeleteHistories.fromAnswers(new Answers(Arrays.asList(A1, A2))))
        );
    }

    @ParameterizedTest
    @MethodSource("createDeleteHistories")
    @DisplayName("삭제 이력 리스트 객체 생성")
    public void createDeleteHistoriesTest(DeleteHistories actual) {
        assertThat(actual).isEqualTo(DeleteHistories.fromAnswers(new Answers(Arrays.asList(A1, A2))));
    }

    @ParameterizedTest
    @MethodSource("createDeleteHistories")
    @DisplayName("삭제된 질문 추가")
    public void addDeleteQuestionTest(DeleteHistories actual) {
        actual.addDeleteQuestion(Q1);
        assertThat(actual.getDeleteHistories()).hasSize(3);
    }

}