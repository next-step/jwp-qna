package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("save하면 id가 자동으로 생성되야 한다.")
    void saveTest() {
        userRepository.save(UserTest.JAVAJIGI);
        Question save = questionRepository.save(new Question("title", "content").writeBy(UserTest.JAVAJIGI));

        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getWriter()).isEqualTo(UserTest.JAVAJIGI)
        );
    }

    @Test
    @DisplayName("findById 결과는 동일성이 보장되어야한다.")
    void findByIdTest() {

        Question save = questionRepository.save(new Question("title", "contents"));

        Question find = questionRepository.findById(save.getId()).get();

        assertThat(find).isEqualTo(save);
    }

    @Test
    @DisplayName("nullable false 칼럼은 반드시 값이 있어야한다.")
    void nullableTest() {

        Question question = new Question(null, "contents");

        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
