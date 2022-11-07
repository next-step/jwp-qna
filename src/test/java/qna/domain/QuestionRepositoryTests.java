package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("질문 저장소 테스트")
class QuestionRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        questionRepository.deleteAll();

        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("질문을 저장한다.")
    void save() {
        Question expected = QuestionTest.Q1.writeBy(user);
        Question question = questionRepository.save(expected);

        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(question.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(question.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("식별자로 질문을 조회한다.")
    void findById() {
        Question expected = questionRepository.save(QuestionTest.Q1.writeBy(user));

        Question question = questionRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(question.getId()).isEqualTo(expected.getId()),
                () -> assertThat(question.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(question.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(question.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문을 조회한다.")
    void findByDeletedFalse() {
        Question expected = questionRepository.save(QuestionTest.Q1.writeBy(user));

        Question question = questionRepository.findByDeletedFalse().get(0);

        assertAll(
                () -> assertThat(question.getId()).isEqualTo(expected.getId()),
                () -> assertThat(question.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(question.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(question.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("식별자로 삭제되지 않은 질문을 조회한다.")
    void findByIdAndDeletedFalse() {
        Question expected = questionRepository.save(QuestionTest.Q1.writeBy(user));

        Question question = questionRepository.findByIdAndDeletedFalse(expected.getId()).get();

        assertAll(
                () -> assertThat(question.getId()).isEqualTo(expected.getId()),
                () -> assertThat(question.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(question.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(question.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("질문을 삭제한다.")
    void delete() {
        Question expected = questionRepository.save(QuestionTest.Q1.writeBy(user));

        questionRepository.delete(expected);

        List<Question> questions = questionRepository.findAll();
        assertThat(questions).isEmpty();
    }
}
