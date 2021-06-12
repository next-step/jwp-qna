package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static qna.domain.AnswerTest.A1_CONTENT;
import static qna.domain.AnswerTest.A2_CONTENT;

public class QuestionTest {
    public static final String Q1_TITLE = "title1";
    public static final String Q1_CONTENT = "contents1";

    public static final String Q2_TITLE = "title2";
    public static final String Q2_CONTENT = "contents2";

    public static final Question Q1 = new Question(Q1_TITLE, Q1_CONTENT).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(Q2_TITLE, Q2_CONTENT).writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("게시판을 올린 사용자가 맞는지 검증한다.")
    void validateIsOwner_test() {
        assertThrows(CannotDeleteException.class,
                () -> Q1.deleteAllAndAddHistories(UserTest.SANJIGI)
        );
    }

    @Test
    @DisplayName("게시판이 삭제되고 히스토리에 저장된다.")
    void deleteAndAddHistory_test() throws CannotDeleteException {
        //given
        Question question = new Question(Q1_TITLE, Q1_CONTENT).writeBy(UserTest.JAVAJIGI);

        //when
        List<DeleteHistory> histories = question.deleteAllAndAddHistories(UserTest.JAVAJIGI);

        //then
        assertAll(
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(histories).hasSize(1)
        );
    }

    @Test
    @DisplayName("게시판이 삭제되고 히스토리에 저장된다..")
    void deleteAllAnswersAndAddHistories_test() throws CannotDeleteException {
        //given
        Answer firstAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, A1_CONTENT);;
        Answer secondAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, A2_CONTENT);;

        Question question = new Question(Q1_TITLE, Q1_CONTENT).writeBy(UserTest.JAVAJIGI);
        question.addAnswer(firstAnswer);
        question.addAnswer(secondAnswer);

        //when
        List<DeleteHistory> histories = question.deleteAllAndAddHistories(UserTest.JAVAJIGI);

        //then
        assertAll(
                () -> assertThat(histories).hasSize(3),
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(firstAnswer.isDeleted()).isTrue(),
                () -> assertThat(secondAnswer.isDeleted()).isTrue()
        );
    }

}
