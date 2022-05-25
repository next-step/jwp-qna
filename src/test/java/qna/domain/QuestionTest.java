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
                () -> assertThat(Q1.mismatchOwner(UserTest.JAVAJIGI)).isFalse(),
                () -> assertThat(Q1.mismatchOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("Question에 Answer 추가 테스트")
    void Question에_Answer추가(){
        Q1.addAnswer(AnswerTest.A1);
        assertThat(Q1.getAnswers().contains(AnswerTest.A1));
    }

    @Test
    @DisplayName("Question 삭제 테스트: 정상")
    void Question_삭제(){
        Q1.delete(Q1.getWriter());
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question 삭제 테스트: 작성자가 맞지 않아 실패")
    void Question(){
        assertThatThrownBy(() -> {
            Q1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    public static Question generateQuestion(User user, boolean deleted) {
        Question question = new Question("title1", "contents1").writeBy(user);
        if(deleted){
            deleteQuestion(question, user);
        }
        return question;
    }

    private static void deleteQuestion(Question question, User user) {
        try {
            question.delete(user);
        } catch (CannotDeleteException e) {
            e.printStackTrace();
        }
    }
}
