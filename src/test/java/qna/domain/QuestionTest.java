package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void getNotDeletedAnswers() {
        Question question = createQuestion();
        Answer answer = createAnswer(question, false);
        Answer deletedAnswer = createAnswer(question, true);

        question.addAnswer(answer);
        question.addAnswer(deletedAnswer);

        assertThat(question.getNotDeletedAnswers()).containsExactly(answer);
    }

    private Question createQuestion() {
        return new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    private Answer createAnswer(Question question, boolean deleted) {
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "answer content1");
        answer.setDeleted(deleted);
        return answer;
    }
}
