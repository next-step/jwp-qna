package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    @Test
    void 유저_ID_찾기_테스트() {
        // given
        User saveUser = users.save(new User("lcjltj","password", "chanjun", "lcjltj@gmail.com"));
        // when
        Optional<User> expected = users.findByUserId(saveUser.getUserId());
        // then
        assertThat(expected.get()).isSameAs(saveUser);
    }

    @Test
    void 유저_질문_추가_테스트() {
        // given
        User saveUser = users.save(new User("lcjltj","password", "chanjun", "lcjltj@gmail.com"));
        Question question = new Question("title1", "contents1").writeBy(saveUser);
        final Question saveQuestion = questions.save(question);
        saveUser.addQuestion(saveQuestion);
        // when
        List<User> list = users.findAll();
        final Optional<User> expected = users.findById(question.getWriter().getId());
        // then
        assertThat(expected.get().getQuestion()).hasSize(1);
        assertThat(expected.get().getQuestion().get(0)).isEqualTo(saveQuestion);
    }
}