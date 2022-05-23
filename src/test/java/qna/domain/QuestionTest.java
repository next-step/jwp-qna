package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);


    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같지 않은 경우 삭제할 수 없다.")
    void deleteAuth() {
        Question q1 = new Question("질문", "질문").writeBy(UserTest.SANJIGI);

        assertThatThrownBy(() -> q1.isAbleDelete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }


    @Test
    @DisplayName("답변이 없는 경우 삭제가 가능하다.")
    void successDeleteNoAnswer() {
        User actionUser = UserTest.SANJIGI;
        Question q1 = new Question("답변없음", "답변없음").writeBy(actionUser);
        Question q2 = new Question("답변있음", "답변있음").writeBy(actionUser);
        q2.addAnswer(AnswerTest.A1);

        assertAll(
            ()-> assertThat(q1.isAbleDelete(actionUser)).isTrue(),
            ()-> assertThat(q2.isAbleDelete(actionUser)).isFalse()
        );
    }


}
