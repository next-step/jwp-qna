package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("저장 테스트 : Audit 데이터 Not Null 테스트")
    public void save() {
        Answer answer = answerRepository.save(A1);
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

    @Test
    @DisplayName("auditing 호출시점 테스트 : @PreUpdate 에서 호출되므로 persist 시점(@PrePersist)이 아닌 update 시점(@PreUpdate)에 저장되는지 확인")
    public void auditingLastModifiedDateTest() {
        Answer answer = answerRepository.saveAndFlush(A1);
        LocalDateTime initialUpdatedAt = answer.getUpdatedAt();
        answer.setContents("test");
        LocalDateTime persistUpdateAt = answer.getUpdatedAt();
        assertThat(initialUpdatedAt).isEqualTo(persistUpdateAt);
        answerRepository.flush();
        LocalDateTime updatedUpdateAt = answer.getUpdatedAt();
        assertThat(updatedUpdateAt).isNotEqualTo(persistUpdateAt);
    }

    @Test
    @DisplayName("auditing 호출시점 테스트 : commit 시점에 저장되는지 확인")
    public void auditingCreatedDateTest() {
        Answer answer = answerRepository.saveAndFlush(A1);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        Question question = questionRepository.save(QuestionTest.Q1);
        LocalDateTime answerCreatedAt = answer.getCreatedAt();
        LocalDateTime questionCreatedAt = question.getCreatedAt();
        entityManager.flush();
        assertThat(answerCreatedAt).isNotEqualTo(questionCreatedAt);
    }
}
