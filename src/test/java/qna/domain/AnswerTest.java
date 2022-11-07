package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("Not Null 테스트")
    public void save() {
        Answer answer = answerRepository.save(A1);
        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isNotNull();
        assertThat(answer.getCreatedAt()).isNotNull();
        assertThat(answer.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("조회 객체 같은지 테스트")
    public void find() {
        Answer answer = answerRepository.save(A1);
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(answer).isEqualTo(actual);
    }

    @Test
    @DisplayName("삭제되지 않은 데이터만 조회되는지 테스트")
    public void findByQuestionIdAndDeletedFalseTest() {
        answerRepository.save(A1);
        Answer answer2 = answerRepository.save(A2);
        answer2.setDeleted(true);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(answers).hasSize(1);
        assertThat(answers.stream().noneMatch(Answer::isDeleted)).isTrue();
    }

    @Test
    @DisplayName("삭제되지 않은 데이터만 조회되는지 테스트")
    public void findByIdAndDeletedFalseTest() {
        Answer answer = answerRepository.save(A1);
        answer.setDeleted(true);
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual).isEmpty();
    }
}