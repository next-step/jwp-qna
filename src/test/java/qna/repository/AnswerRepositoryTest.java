package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    static final Answer A1 = new Answer.AnswerBuilder(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.Q1)
            .contents("Answers Contents1")
            .build();
    static final Answer A2 = new Answer.AnswerBuilder(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.Q1)
            .contents("Answers Contents2")
            .build();
    static final Answer A3 = new Answer.AnswerBuilder(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.Q1)
            .contents("Answers Contents3")
            .build();

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장 테스트")
    @Test
    void save() {
        Answer actual = answerRepository.save(A1);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(actual.getContents()).isEqualTo(A1.getContents())
        );
    }

    @DisplayName("질문 ID 로 조회 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.save(A1);
        answerRepository.save(A2);
        answerRepository.save(A3);
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionRepositoryTest.Q1.getId());
        assertThat(actual).hasSize(3);
    }

    @DisplayName("delete 가 잘 되었는지 테스트")
    @Test
    void delete() {
        answerRepository.save(A1);
        answerRepository.save(A2);
        Answer a3 = answerRepository.save(A3);
        answerRepository.delete(a3);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionRepositoryTest.Q1.getId());
        assertThat(answers).hasSize(2);
    }

    @DisplayName("ID로 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.save(A1);
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual.orElse(null)).isEqualTo(answer);
    }
}
