package qna.repos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    public static final Answer A1 = new Answer(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository repository;

    @DisplayName("Answer 저장 테스트")
    @Test
    void save() {
        Answer expected = A1;
        Answer actual = repository.save(A1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @DisplayName("Answer deleted=false 경우 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Answer a1 = repository.save(A1);
        Answer a2 = repository.save(A2);

        a2.setDeleted(true);

        repository.save(a2);

        Optional<Answer> a1Result = repository.findByIdAndDeletedFalse(a1.getId());
        Optional<Answer> a2Result = repository.findByIdAndDeletedFalse(a2.getId());

        assertAll(
                () -> assertThat(a1Result).isNotEmpty(),
                () -> assertThat(a2Result).isEmpty()
        );
    }
}
