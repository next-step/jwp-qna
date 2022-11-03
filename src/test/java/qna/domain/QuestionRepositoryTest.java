package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @DisplayName("Question을 저장할 수 있다.")
    @Test
    void save() {
        Question actual = questionRepository.save(QuestionTest.Q1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(actual.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
                () -> assertThat(actual.getContents()).isEqualTo(QuestionTest.Q1.getContents())
        );
    }

    @DisplayName("저장된 Question을 조회할 수 있다.")
    @Test
    void find() {
        Question actual = questionRepository.save(QuestionTest.Q1);

        Optional<Question> expect = questionRepository.findById(actual.getId());

        assertThat(expect).isPresent();
    }

    @DisplayName("저장된 Question을 삭제할 수 있다.")
    @Test
    void delete() {
        Question actual = questionRepository.save(QuestionTest.Q1);

        questionRepository.delete(actual);
        Optional<Question> expect = questionRepository.findById(actual.getId());

        assertThat(expect).isNotPresent();
    }

    @DisplayName("저장된 Question의 값을 변경할 수 있다.")
    @Test
    void update() {
        Question actual = questionRepository.save(new Question("title", "content")
                .writeBy(UserTest.SANJIGI));

        actual.writeBy(UserTest.JAVAJIGI);

        Optional<Question> expect = questionRepository.findById(actual.getId());

        assertThat(expect.orElseThrow(NotFoundException::new).isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("삭제되지 않은 question 목록을 조회할 수 있다.")
    @Test
    void find_by_deleted_false() {
        Question actual1 = questionRepository.save(new Question("title", "content"));
        Question actual2 = questionRepository.save(new Question("title", "content"));

        actual2.setDeleted(true);
        List<Question> expect = questionRepository.findByDeletedFalse();

        assertThat(expect).containsExactly(actual1);
    }

    @DisplayName("Id로 삭제되지 않은 question을 조회할 수 있다.")
    @Test
    void find_by_id_and_deleted_false() {
        Question actual1 = questionRepository.save(new Question("title", "content"));
        Question actual2 = questionRepository.save(new Question("title", "content"));

        actual2.setDeleted(true);
        Optional<Question> expect1 = questionRepository.findByIdAndDeletedFalse(actual1.getId());
        Optional<Question> expect2 = questionRepository.findByIdAndDeletedFalse(actual2.getId());

        assertAll(
                () -> assertThat(expect1).isPresent(),
                () -> assertThat(expect2).isNotPresent()
        );

    }
}
