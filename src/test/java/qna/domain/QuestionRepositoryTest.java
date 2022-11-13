package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository target;

    @Autowired
    private UserRepository users;

    private Question question;

    private User userFromRepo(final Long id, final String userId) {
        return users.save(new User(id, userId, "pwd", "name", "test@gmail.com"));
    }

    @BeforeEach
    void setUp() {
        question = new Question("JPA", "JPA Content")
            .writeBy(userFromRepo(1L, "javajigi"));
    }

    @Test
    void find() {
        final Question saved = target.save(question);
        final Question actual = target.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @Test
    void update() {
        final Question saved = target.save(question);
        final User newUser = userFromRepo(2L, "sanjigi");

        Question updated = target.findById(saved.getId()).get();
        updated.writeBy(newUser);

        final Question actual = target.findById(saved.getId()).get();
        assertThat(actual.isOwner(newUser)).isTrue();
        assertThat(actual).isSameAs(updated);
    }

    @Test
    void delete() {
        final Question saved = target.save(question);

        target.deleteById(saved.getId());
        assertThat(target.findById(saved.getId()).orElse(null)).isNull();
    }

}
