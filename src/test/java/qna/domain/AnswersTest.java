package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswersTest {

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Test
    @DisplayName("Answers 사이즈 반환 테스트")
    void answer_size_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        question1.addAnswer(AnswerTest.A1);
        question1.addAnswer(AnswerTest.A2);

        assertThat(question1.getAnswers().getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("빈 answers 확인 테스트")
    void isEmpty_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        assertThat(question1.getAnswers().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Answer writer 가 loginUser 가 아닌 경우 테스트")
    void check_answer_writer_test() {
        final User loginUser = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(loginUser));

        final User answerWriter = users.save(new User(2L, "user2", "qwerty", "P2", "P2@test.com"));
        question1.addAnswer(new Answer(answerWriter, question1, "Answers Contents1"));
        question1.addAnswer(new Answer(answerWriter, question1, "Answers Contents2"));

        assertThat(question1.getAnswers().isIdenticalWriter(loginUser)).isFalse();
    }
}
