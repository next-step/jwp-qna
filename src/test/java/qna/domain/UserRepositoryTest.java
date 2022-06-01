package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private Question question;
    private Question question2;
    private User saveUser;

    @BeforeEach
    void setUp() {
        User user = UserTest.JAVAJIGI;
        saveUser = users.save(user);
        question = new Question("title1", "contents1").writeBy(saveUser);
        question2 = new Question("title1", "contents1").writeBy(saveUser);
    }

    @Test
    void 유저_ID_찾기_테스트() {
        // given
        // when
        Optional<User> expected = users.findByUserId(saveUser.getUserId());
        // then
        assertThat(expected.get()).isSameAs(saveUser);
    }

    @Test
    void 유저_질문_추가_테스트() {
        // given
        final Question saveQuestion = questions.save(question);
        final Question saveQuestion2 = questions.save(question2);
        saveUser.addQuestion(saveQuestion);
        saveUser.addQuestion(saveQuestion2);
        // when
        final Optional<User> expected = users.findById(saveUser.getId());
        // then
        assertThat(expected.get().getQuestion()).hasSize(2);
        assertThat(expected.get().getQuestion().get(0)).isEqualTo(saveQuestion);
    }
}