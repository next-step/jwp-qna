package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 질문_답변_추가() {
        // given
        Question question = Q1;
        Answer answer = AnswerTest.A1;
        // when
        question.addAnswer(answer);
        // then
        assertThat(answer.getQuestion()).isSameAs(Q1);
    }

    @Test
    void 작성자_업데이트() {
        // given
        Question question = Q1;
        User writer = question.getWriter();
        // when
        question.updateWriter(UserTest.SANJIGI);
        // then
        assertThat(writer.containQuestion(question)).isTrue();
    }

    @Test
    void 질문_삭제_다른_작성자_오류() {
        // given
        User loginUser = UserTest.SANJIGI;
        Question question = Q1;
        //when
        //then
        assertThatThrownBy(() -> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_삭제_테스트() throws CannotDeleteException {
        // given
        User loginUser = UserTest.JAVAJIGI;
        Question question = Q1;
        //when
        question.delete(loginUser);
        //then
        assertThat(question.isDeleted()).isTrue();
    }
}