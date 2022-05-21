package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Test
    @DisplayName("저장")
    void create() {
        Question savedQuestion = questions.save(QuestionTest.Q1);
        Optional<Question> actual = questions.findById(savedQuestion.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        Question question = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
        questions.save(question);
        Optional<Question> actual = questions.findById(9999L);
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("갱신")
    void update() {
        final long newWriter = 2L;
        Question question = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
        Question actual = questions.save(question);
        assertThat(actual.getWriterId()).isEqualTo(question.getWriterId());

        actual.setWriterId(newWriter);
        assertThat(actual.getWriterId()).isEqualTo(newWriter);
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        Question question = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
        questions.save(question);
        Optional<Question> actual = questions.findById(question.getId());
        assertThat(actual).isPresent();

        questions.deleteById(actual.get().getId());
        actual = questions.findById(actual.get().getId());
        assertThat(actual).isNotPresent();
    }
}
