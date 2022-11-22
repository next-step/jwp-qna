package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {

    @Test
    void add_answer_test() {
        // given
        Answers answers = new Answers(Answer.create(User.create("deokmoon", "1234", "deokmoon", "deokmoon@gmail.com")
                , Question.create("hello", "world"), "contents"));
        // when
        answers.addAnswer(Answer.create(User.create("deokmoon", "1234", "deokmoon", "deokmoon@gmail.com")
                , Question.create("hello2", "world2"), "contents"));
        // then
        assertThat(answers.NumberOfAnswer()).isEqualTo(2);
    }

    @Test
    void remove_answer_test() {
        // given
        Answer answer = Answer.create(User.create("deokmoon", "1234", "deokmoon", "deokmoon@gmail.com")
                , Question.create("hello", "world"), "contents");
        Answers answers = new Answers(answer);
        // when
        answers.removeAnswer(answer);
        // then
        assertThat(answers.contains(answer)).isFalse();
    }
}