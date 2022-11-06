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
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("저장")
    void save() {
        //given
        Answer answer = answerRepository.save(A1);

        //expect
        assertThat(answer).isNotNull();
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse 조회")
    void findByQuestionIdAndDeletedFalse() {
        // given
        answerRepository.save(A1);
        answerRepository.save(A2);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        // then
        assertThat(answers).hasSize(2);
        assertThat(answers.stream().noneMatch(answer -> answer.isDeleted())).isTrue();
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse은 delete False만 조회한다.")
    void findByQuestionIdAndDeletedFalse_delete_검증() {
        // given
        answerRepository.save(A1);
        answerRepository.save(A2);
        Answer a3 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents3");
        a3 = answerRepository.save(a3);
        a3.setDeleted(true);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        // then
        assertThat(answers).hasSize(2);
        assertThat(answers.stream().noneMatch(answer -> answer.isDeleted())).isTrue();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 조회")
    void findByIdAndDeletedFalse() {
        // given
        Answer a1 = answerRepository.save(A1);

        // when
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(a1.getId());

        // then
        assertThat(answer).isPresent();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse는 Id가 일치하더라도 Delete Flase면 조회되지 않는다.")
    void findByIdAndDeletedFalse_delete_검증() {
        // given
        Answer a1 = answerRepository.save(A1);
        a1.setDeleted(true);

        // when
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(a1.getId());

        // then
        assertThat(answer).isNotPresent();
    }


}
