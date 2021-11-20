package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Question soft delete 성공(질문자 일치 및 답변자 없음) 테스트")
    public void QuestionSoftDeleteTest() throws Exception {
        //given
        //when
        Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question soft delete 실패(질문자 불일치) 테스트")
    public void QuestionSoftDeleteNotSameUserTest() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("질문을 삭제할 권한이 없습니다.");
    }


    @Test
    @DisplayName("Question soft delete 성공(답변자 있음) 테스트")
    public void QuestionSoftDeleteSameAnswerUserSuccessTest() throws CannotDeleteException {
        //given
        Q1.addAnswer(AnswerTest.A1);
        //when
        Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question soft delete 실패(답변자 있음) 테스트")
    public void QuestionSoftDeleteSameAnswerUserFailTest()  {
        //given
        //when
        Q1.addAnswer(AnswerTest.A2);
        //then
        assertThatThrownBy(() -> Q1.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("Question And Answer soft delete (질문 삭제시 답변도 삭제) 테스트")
    public void QuestionAndAnswerSoftDeleteSuccessTest() throws CannotDeleteException {
        //given
        Q1.addAnswer(AnswerTest.A1);
        //when
        Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(AnswerTest.A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("DeleteHistory 생성 테스트")
    public void createDeleteHistoryTest() throws CannotDeleteException {
        //given
        Q1.addAnswer(AnswerTest.A1);
        //when
        List<DeleteHistory> deleteHistoryList = Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(deleteHistoryList).hasSize(2);
    }

    @Test
    @DisplayName("Question delete 질문 1개 삭제 상태일 때 테스트")
    public void deleteQuestionAnswerDeletedYnTest() throws CannotDeleteException {
        //given
        AnswerTest.A2.delete(UserTest.SANJIGI);
        Q1.addAnswer(AnswerTest.A2);
        Q1.addAnswer(AnswerTest.A1);
        //when
        List<DeleteHistory> deleteHistoryList = Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(deleteHistoryList).hasSize(2);
    }
}
