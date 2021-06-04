package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 질문_작성자와_현재_로그인_사용자가_다른_경우_에러_정상_발생_여부() {
        Question question = Q1;
        User loginUser = UserTest.SANJIGI;
        assertThatThrownBy(() -> question.checkValidSameUserByQuestionWriter(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 질문에_대한_답변_존재_시_질문자_답변자가_하나라도_다른_경우_에러_정상_발생_여부() {
        Question question = QuestionTest.Q1;
        question.addAnswer(AnswerTest.A1);
        question.addAnswer(AnswerTest.A2);

        assertThatThrownBy(() -> question.checkValidPossibleDeleteAnswers(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
