package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question createQuestion(User writer, boolean deleted) {
        Question question = new Question("title1", "contents1").writeBy(writer);
        question.setDeleted(deleted);
        return question;
    }

    @Test
    void getNotDeletedAnswers() {
        Question question = createQuestion(UserTest.JAVAJIGI, false);
        Answer answer = createAnswer(question, false);
        Answer deletedAnswer = createAnswer(question, true);

        question.addAnswer(answer);
        question.addAnswer(deletedAnswer);

        assertThat(question.getNotDeletedAnswers()).containsExactly(answer);
    }

    private Answer createAnswer(Question question, boolean deleted) {
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "answer content1");
        answer.setDeleted(deleted);
        return answer;
    }
}
