package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 질문_작성자와_현재_로그인_사용자가_다른_경우_에러_정상_발생_여부() {
        Question question = Q1;
        User loginUser = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        assertThatThrownBy(() -> question.checkPossibleDelete(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 질문에_대한_답변_존재_시_질문자_답변자가_하나라도_다른_경우_에러_정상_발생_여부() {
        User user1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");;
        User user2 = new User(2L, "id", "password", "name", "email");
        Question question = QuestionTest.Q1;

        Answer answer1 = new Answer(1L, user1, question, "contents");
        Answer answer2 = new Answer(1L, user2, question, "contents");
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        assertThatThrownBy(() -> question.checkPossibleDelete(user1))
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
}
