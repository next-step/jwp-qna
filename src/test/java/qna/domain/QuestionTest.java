package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    private Question questionWithSameAnswer;
    private Question questionWithDiffrentAnswer;

    @BeforeEach
    void setUp() {
        questionWithSameAnswer =  new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        questionWithDiffrentAnswer = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        questionWithSameAnswer.addAnswer(new Answer(1l, UserTest.JAVAJIGI, questionWithSameAnswer, "Answers Contents1"));
        questionWithSameAnswer.addAnswer(new Answer(2l, UserTest.JAVAJIGI, questionWithSameAnswer, "Answers Contents1"));

        questionWithDiffrentAnswer.addAnswer(new Answer(1l, UserTest.JAVAJIGI, questionWithSameAnswer, "Answers Contents1"));
        questionWithDiffrentAnswer.addAnswer(new Answer(2l, UserTest.SANJIGI, questionWithSameAnswer, "Answers Contents1"));
    }

    @DisplayName("Question 생성")
    @Test
    public void newQuestion(){
        assertThat(Q1.getTitle()).isEqualTo("title1");
        assertThat(Q1.getContents()).isEqualTo("contents1");
        assertThat(Q1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @DisplayName("삭제 테스트")
    @Test
    public void delete() {
        //when
        questionWithSameAnswer.deleted(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> questionWithSameAnswer.deleted(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);

        //then
        assertThat(questionWithSameAnswer.isDeleted()).isTrue();
    }

    @DisplayName("댓글 작성자가 다를때 에러 발생")
    @Test
    public void deleteOtherAnswerWriter() {
        assertThatThrownBy(() -> questionWithDiffrentAnswer.deleted(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
