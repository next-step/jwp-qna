package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Test
    @DisplayName("저장")
    void create() {
        Answer savedAnswer = answers.save(AnswerTest.A1);
        Optional<Answer> actual = answers.findById(savedAnswer.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        answers.save(answer);
        Optional<Answer> actual = answers.findById(9999L);
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("갱신")
    void update() {
        final long newWriter = 2L;
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        Answer actual = answers.save(answer);
        assertThat(actual.getWriterId()).isEqualTo(answer.getWriterId());

        actual.setWriterId(newWriter);
        assertThat(actual.getWriterId()).isEqualTo(newWriter);
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        answers.save(answer);
        Optional<Answer> actual = answers.findById(answer.getId());
        assertThat(actual).isPresent();

        answers.deleteById(actual.get().getId());
        actual = answers.findById(actual.get().getId());
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("동등성/동일성 확인")
    void identifyAndEquality() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        Answer savedAnswer = answers.save(answer);
        assertThat(answer == savedAnswer).isTrue();
        assertThat(answer).isEqualTo(savedAnswer);
    }
}
