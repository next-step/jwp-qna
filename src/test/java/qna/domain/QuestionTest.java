package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문을 삭제할경우 삭제히스토리를 반환하며 질문의 deleted값이 true가 된다")
    void delete() throws CannotDeleteException{
        //given
        User loginUser = UserTest.JAVAJIGI;

        //when
        List<DeleteHistory> deleteHistories = Q1.delete(loginUser);

        //then
        assertAll(
                () -> assertThat(Q1.isDeleted()).isTrue(),
                () -> assertThat(deleteHistories).hasSize(1)
        );
    }

    @Test
    //TODO 관리자 추가 예정
    void 작성자가_아닌_유저의_질문_삭제_에러_테스트() {
        //given
        User loginUser = UserTest.SANJIGI;

        //when && then
        assertThatThrownBy(() -> Q1.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 답변이_존재하고_답변의_작성자와_질문의_작성자가_다를_경우_삭제_에러_테스트() {
        //given
        User loginAndQuestionWriterUser = UserTest.JAVAJIGI;
        User answerWriterUser = UserTest.SANJIGI;
        Question question = new Question("자바지기작성_제목","자바지기작성_내용").writeBy(loginAndQuestionWriterUser);
        Answer answer = new Answer(answerWriterUser, question, "산지기_답변");
        question.addAnswer(answer);

        //when && then
        assertThatThrownBy(() -> question.delete(loginAndQuestionWriterUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
