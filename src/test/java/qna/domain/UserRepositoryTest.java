package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    private User user;
    private Question question;
    private Question question2;

    @BeforeEach
    void setUp() {
        user = UserTest.JAVAJIGI;
        question = QuestionTest.Q1;
        question2 = QuestionTest.Q2;
    }

    @Test
    @DisplayName("question 연관 관계 테스트")
    void findById() {
        final User saveUser = users.save(user);
        final Question saveQuestion = questions.save(question);
        final Question saveQuestion2 = questions.save(question2);

        saveUser.addQuestion(saveQuestion);
        saveUser.addQuestion(saveQuestion2);

        final Optional<User> expected = users.findById(saveUser.getId());
        assertThat(expected.get().getQuestion()).hasSize(2);
        assertThat(expected.get().getQuestion().get(0)).isEqualTo(saveQuestion);

        final Optional<Question> expectedQuestion = questions.findById(saveQuestion.getId());
        assertThat(expectedQuestion.get().getWriter()).isEqualTo(saveUser);
    }
}