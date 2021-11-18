package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Question soft delete 성공(질문자 일치 및 답변자 없음) 테스트")
    public void QuestionRepositorySoftDeleteTest() throws Exception {
        //given
        //when
        Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question soft delete 실패(질문자 불일치) 테스트")
    public void QuestionRepositorySoftDeleteNotSameUserTest() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("질문을 삭제할 권한이 없습니다.");
    }


    @Test
    @DisplayName("Question soft delete 성공(답변자 있음) 테스트")
    public void QuestionRepositorySoftDeleteSameAnswerUserSuccessTest() throws CannotDeleteException {
        //given
        Q1.addAnswer(AnswerTest.A1);
        //when
        Q1.delete(UserTest.JAVAJIGI);
        //then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question soft delete 실패(답변자 있음) 테스트")
    public void QuestionRepositorySoftDeleteSameAnswerUserFailTest() throws CannotDeleteException {
        //given
        //when
        Q1.addAnswer(AnswerTest.A2);
        //then
        assertThatThrownBy(() -> Q1.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
