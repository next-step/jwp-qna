package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question(1L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("자신이 작성한 답변만 존재하는 경우 자신이 작성한 질문을 삭제상태로 변경한다.")
    void delete_question() throws CannotDeleteException {
        //given
        Answer answer = AnswerTest.A1;
        Q1.addAnswer(answer);
        assertThat(Q1.isDeleted()).isFalse();

        //when
        DeleteHistories deleteHistories = Q1.delete(UserTest.JAVAJIGI);

        //then
        assertThat(answer.isDeleted()).isTrue();
        assertThat(Q1.isDeleted()).isTrue();
        assertThat(deleteHistories.getDeleteHistories().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("자신이 아닌 다른 답변자가 존재하는 경우 자신이 작성한 질문을 삭제할 수 없다.")
    void question_with_answer_by_other_writer_not_deletable() {
        //given
        Answer answer = AnswerTest.A4;
        Q2.addAnswer(answer);
        assertThat(Q2.isDeleted()).isFalse();

        //when
        assertThatThrownBy(() -> Q2.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("본인이 작성하지 않은 질문은 삭제할 수 없다.")
    void answer_by_other_writers_not_deletable() {
        assertThatThrownBy(() -> Q3.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

}
