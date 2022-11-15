package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("저장 후 조회시, 동등성과 동일성을 보장해야 한다")
    @Test
    void find() {
        final Question saved = target.save(question);
        final Question actual = target.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @DisplayName("저장 후 갱신시, 영속선 컨텍스트 캐시로 인해 동등성과 동일성을 보장해야 한다")
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

    @DisplayName("저장 후 삭제 가능 해야 한다")
    @Test
    void delete() {
        final Question saved = target.save(question);

        target.deleteById(saved.getId());
        assertThat(target.findById(saved.getId()).orElse(null)).isNull();
    }

}
