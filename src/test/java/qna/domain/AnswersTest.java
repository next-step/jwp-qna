package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {
    @Test
    @DisplayName("작성자의 질문에 다른유저의 답변이 존재하면 false를 리턴한다")
    void isDeletable_삭제불가능한경우() {
        //given
        List<Answer> answerList = Arrays.asList(AnswerTest.A1,AnswerTest.A2);
        Answers answers = new Answers(answerList);

        //when && then
        assertThat(answers.isDeletable(UserTest.JAVAJIGI)).isFalse();
    }

    @Test
    @DisplayName("작성자의 질문에 작성자의 답변만 존재하면 true를 리턴한다")
    void isDeletable_삭제가능한경우() {
        //given
        List<Answer> answerList = Arrays.asList(AnswerTest.A1,AnswerTest.A1);
        System.out.println("answerList.size() = " + answerList.size());
        Answers answers = new Answers(answerList);

        //when && then
        assertThat(answers.isDeletable(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변이 없는경우 true를 리턴한다")
    void isDeletable_답변이없는경우() {
        //given
        List<Answer> answerList = new ArrayList<>();
        System.out.println("answerList.size() = " + answerList.size());
        Answers answers = new Answers(answerList);

        //when && then
        assertThat(answers.isDeletable(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변 삭제에 성공하면 DeleteHistorys를 반환한다.")
    void delete() {
        //given
        List<Answer> answerList = Arrays.asList(AnswerTest.A1,AnswerTest.A1);
        System.out.println("answerList.size() = " + answerList.size());
        Answers answers = new Answers(answerList);

        //when
        DeleteHistories deleteHistories = answers.delete(UserTest.JAVAJIGI);

        //then
        assertThat(deleteHistories).isNotNull();
    }
}