package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.DomainTestFactory.*;

class AnswersTest {

    @Test
    @DisplayName("isOwner : 모든 답변의 작성자 중 인자값의 작성자와 한명이라도 일치하지 않으면 false 를 리턴한다")
    public void isAnswersOwnerFalse() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        question.addAnswer(createAnswer(question.getWriter(), question));
        question.addAnswer(createAnswer(createUser("JAVAJIGI"), question));

        assertThat(question.getAnswers().isOwner(question.getWriter())).isFalse();
    }

    @Test
    @DisplayName("isOwner : 모든 답변의 작성자가 인자의 작성자와 일치하면 true를 리턴한다")
    public void isAnswersOwnerTrue() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        question.addAnswer(createAnswer(question.getWriter(), question));
        question.addAnswer(createAnswer(createUser("DEVELOPYO"), question));
        assertThat(question.getAnswers().isOwner(question.getWriter())).isTrue();
    }

    @Test
    @DisplayName("삭제된 답변을 삭제할 경우 예외를 던진다")
    public void deleteTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        answer.delete(user);
        List<Answer> answerList = new ArrayList<>();
        answerList.add(answer);
        Answers answers = new Answers(answerList);

        assertThatThrownBy(() -> answers.delete(user)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제되지 않은 답변을 삭제할 경우 삭제이력을 리턴한다")
    public void deleteTest2() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        Answer answer = createAnswer(user, question);
        List<Answer> answerList = new ArrayList<>();
        answerList.add(answer);
        Answers answers = new Answers(answerList);
        assertThat(answers.getAnswers()).hasSize(1);
        assertThat(answers.delete(user).getDeleteHistories()).isNotEmpty();
    }
}
