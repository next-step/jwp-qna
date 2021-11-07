package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

@DisplayName("답변들")
class AnswersTest {

    @Test
    @DisplayName("답변 삭제")
    void delete() {
        //given
        Answer javajigisAnswer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        Answer javajigisAnswer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "contents2");
        Answers answers = Answers.from(Arrays.asList(javajigisAnswer1, javajigisAnswer1));

        //when
        List<DeleteHistory> delete = answers.delete(UserTest.JAVAJIGI);

        //then
        assertAll(
            () -> assertThat(javajigisAnswer1.isDeleted()).isTrue(),
            () -> assertThat(javajigisAnswer2.isDeleted()).isTrue(),
            () -> assertThat(delete)
                .hasSize(2)
                .extracting("contentType", "deletedByUser")
                .contains(
                    tuple(ContentType.ANSWER, UserTest.JAVAJIGI),
                    tuple(ContentType.ANSWER, UserTest.JAVAJIGI)
                )
        );
    }

    @Test
    @DisplayName("다른 사람의 답변이 있는 경우 삭제하면 CannotDeleteException")
    void delete_containsOtherUsers_thrownCannotDeleteException() {
        //given
        Answer javajigisAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        Answer sanjigisAnswer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "contents2");
        Answers answers = Answers.from(Arrays.asList(javajigisAnswer, sanjigisAnswer));

        //when
        ThrowingCallable deleteCall = () -> answers.delete(UserTest.JAVAJIGI);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(deleteCall)
            .withMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
