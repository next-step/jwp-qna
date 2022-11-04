package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private UserRepository userRepository;
    private Question Q1;
    private Question Q2;

    private User NEWUSER1;
    private User NEWUSER2;

    @BeforeEach
    void setUp() {
        NEWUSER1 = new User("id","pass","name","email");
        NEWUSER2 = new User("id","pass","name","email");
        Q1 = new Question("title", "contents").writeBy(NEWUSER1);
        Q2 = new Question("title", "contents").writeBy(NEWUSER2);
        repository.saveAll(Arrays.asList(Q1,Q2));
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

    private static String prepareContentsOverLength(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('a');
        }
        return sb.toString();
    }
}