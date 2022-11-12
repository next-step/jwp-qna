package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("작성자가 일치하는지 테스트")
    public void isOwnerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(createUser("DEVELOPYO"));
        assertThat(question.isOwner(user)).isTrue();
    }

    @Test
    @DisplayName("질문글에 추가한 답변의 질문글 값이 질문글과 동일 객체인지 테스트")
    public void addAnswerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        question.addAnswer(answer);

        assertThat(answer.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("답변 글쓴이들이 질문글 작성자와 동일한지 테스트 : 다른경우 false 리턴")
    public void isAnswersOwnerFalse() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        question.addAnswer(createAnswer(question.getWriter(), question));
        question.addAnswer(createAnswer(createUser("JAVAJIGI"), question));
        assertThat(question.isAnswersOwner(question.getWriter())).isFalse();
    }

    @Test
    @DisplayName("답변 글쓴이들이 질문글 작성자와 동일한지 테스트 : 같은 경우 true 리턴")
    public void isAnswersOwnerTrue() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        question.addAnswer(createAnswer(question.getWriter(), question));
        question.addAnswer(createAnswer(createUser("DEVELOPYO"), question));
        assertThat(question.isAnswersOwner(question.getWriter())).isTrue();
    }
}
