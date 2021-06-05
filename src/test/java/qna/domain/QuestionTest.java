package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void 질문에_답변_객제_존재_여부_확인() {
        Question question = Q1;
        Answer answer1 = new Answer(100L, UserTest.JAVAJIGI, question, "contents1");
        Answer answer2 = new Answer(101L, UserTest.JAVAJIGI, question, "contents2");
        question.addAnswer(answer1);

        assertThat(question.isContainAnswer(answer1)).isTrue();
        assertThat(question.isContainAnswer(answer2)).isFalse();
    }

    @Test
    void 질문_및_답변_삭제_히스토리_생성() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, question, "contents");
        question.addAnswer(answer1);

        List<DeleteHistory> deleteHistories = question.createDeleteHistories(UserTest.JAVAJIGI);
        assertThat(deleteHistories.size()).isEqualTo(2);
    }
}
