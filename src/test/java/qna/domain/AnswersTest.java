package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {

    @Test
    void add_answer_test() {
        Answers answers = new Answers(Answer.create(User.create("deokmoon"), Question.create("hello", "world")));
        // when
        answers.addAnswer(Answer.create(User.create("deokmoon"), Question.create("hello2", "world2")));
        // then
        assertThat(answers.NumberOfAnswer()).isEqualTo(2);
    }

    @Test
    void remove_answer_test() {
        Answer answer = Answer.create(User.create("deokmoon"), Question.create("hello", "world"));
        Answers answers = new Answers(answer);
        // when
        answers.removeAnswer(answer);
        // then
        assertThat(answers.contains(answer)).isFalse();
    }
}