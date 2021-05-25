package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문 작성자가 아닌 사용자는 질문을 지울 수 없다.")
    @Test
    void deleteFailTest() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class)
                                                            .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("삭제하면 질문이 삭제 상태가 되고, 삭제 내역 목록을 반환한다.")
    @Test
    void deleteSuccessTest() {
        //given
        Question givenQuestion = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer givenAnswer = new Answer(UserTest.JAVAJIGI, givenQuestion, "Answers Contents1");
        givenQuestion.addAnswer(givenAnswer);

        DeleteHistory givenQuestionDeleteHistory = new DeleteHistory(ContentType.QUESTION, givenQuestion.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
        DeleteHistory givenAnswerDeleteHistory = new DeleteHistory(ContentType.ANSWER, givenAnswer.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
        DeleteHistories expectation = new DeleteHistories(Lists.newArrayList(givenQuestionDeleteHistory, givenAnswerDeleteHistory));

        //when
        DeleteHistories delete = givenQuestion.delete(UserTest.JAVAJIGI);
        //then
        assertAll(
            () -> assertThat(givenQuestion.isDeleted()).isTrue(),
            () -> assertThat(delete).isEqualTo(expectation)
        );
    }
}
