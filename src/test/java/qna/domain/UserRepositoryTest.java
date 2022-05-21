package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 사용자_생성() {
        User saved = userRepository.save(createTestUser());
        assertThat(saved).isNotNull();
    }

    @Test
    void 사용자_조회() {
        User save = userRepository.save(createTestUser());

        Optional<User> find = userRepository.findByUserId(save.getUserId());

        assertThat(find.get()).isNotNull();
    }

    @Test
    void 사용자_수정() {
        User user = userRepository.save(createTestUser());

        user.setName("han");
        user.setEmail("han@slipp.net");
        userRepository.save(user);

        User actual = userRepository.findByUserId(user.getUserId()).get();
        assertThat(actual.getName()).isEqualTo("han");
        assertThat(actual.getEmail()).isEqualTo("han@slipp.net");
    }

    @Test
    void 사용자_삭제() {
        User user = userRepository.save(createTestUser());

        userRepository.delete(user);

        Optional<User> actual = userRepository.findByUserId(user.getUserId());
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    void 사용자_질문_리스트_조회() {
        final User user = userRepository.save(createTestUser());
        questionRepository.save(new Question("제목", "내용").writeBy(user));

        final User actual = userRepository.findById(user.getId()).get();

        assertThat(actual.getQuestions()).hasSize(1);
    }

    private User createTestUser() {
        return new User("donghee.han", "password", "donghee", "donghee.han@slipp.net");
    }
}
