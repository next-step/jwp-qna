package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Question 작성자 테스트")
    void Question_작성자(){
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("Question에 Answer 추가 테스트")
    void Question에_Answer추가(){
        Q1.addAnswer(AnswerTest.A1);
        assertThat(Q1.getUnmodifiableAnswers().contains(AnswerTest.A1));
    }

    @Test
    @DisplayName("Question 삭제 테스트: 정상")
    void Question_삭제() throws CannotDeleteException {
        Q1.delete(Q1.getWriter());
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question 삭제 테스트: 작성자가 맞지 않아 실패")
    void Question() throws CannotDeleteException {
        assertThatThrownBy(() -> {
            Q1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
