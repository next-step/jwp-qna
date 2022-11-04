package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest extends NewEntityTestBase {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        repository.saveAll(Arrays.asList(Q1, Q2));
    }

    @Test
    @DisplayName("조회 후 하나를 Deleted True로 설정하면 Delete False인 것이 하나만 조회됨")
    void findByDeletedFalse() {

        List<Question> all = repository.findAll();
        all.get(0).setDeleted(true);

        List<Question> byDeletedFalse = repository.findByDeletedFalse();

        assertThat(byDeletedFalse.size()).isEqualTo(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> found = repository.findByIdAndDeletedFalse(Q1.getId());

        assertThat(found.get().getContents()).isEqualTo(Q1.getContents());
    }

    @Test
    @DisplayName("column 길이 제약조건을 어기면 DataIntegrityViolationException 이 발생함")
    void test4() {
        String stringLengthOver = prepareContentsOverLength(101);

        Question question = new Question(null, stringLengthOver, "contents").writeBy(NEWUSER1);

        assertThatThrownBy(() -> repository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement; SQL [n/a]");
    }

    @Test
    @DisplayName("신규 Question 엔티티와 신규 User 엔티티를 연결하여 저장하면 모두 Id가 생김")
    void test5() {
        Question save = repository.save(Q1);

        assertAll(
                () -> assertThat(save.getWriter().getId()).isPositive(),
                () -> assertThat(save.getId()).isPositive()
        );
    }

    @Test
    @DisplayName("Question을 지우면 User가 함께 지워진다! (Question -> User의 Cascade가 ALL인 상태")
    void test6() {
        Question save = repository.save(Q1);

        Long questionId = save.getId();
        Long userId = save.getWriter().getId();

        repository.delete(save);
        repository.flush();

        Optional<Question> question = repository.findById(questionId);
        Optional<User> user = userRepository.findById(userId);


        assertAll(
                () -> assertThat(question).isEmpty(),
                () -> assertThat(user).isEmpty()
        );
    }

    @Test
    @DisplayName("User를 지우면 user만 사라지고 Question은 남아 있음. User -> Question 연결 관계는 없는 상태")
    void test7() {
        Question save = repository.save(Q1);

        Long questionId = save.getId();
        Long userId = save.getWriter().getId();

        userRepository.deleteById(userId);
        //userRepository.flush(); //flush를 안 하면 user가 없고, flush를 하면 user가 검색됨..?

        Optional<Question> question = repository.findById(questionId);
        Optional<User> user = userRepository.findById(userId);

        assertAll(
                () -> assertThat(question).isNotNull(),
                () -> assertThat(question.get().getWriter()).isNotNull(),
                () -> assertThat(question.get().getWriter().getId()).isPositive(),
                () -> assertThat(user).isEmpty()
        );
    }

    private static String prepareContentsOverLength(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('a');
        }
        return sb.toString();
    }
}