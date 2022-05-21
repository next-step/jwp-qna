package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired AnswerRepository answerRepository;

    @Test
    @DisplayName("Answer 저장")
    void save(){
        Answer saved = answerRepository.save(AnswerTest.A1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer를 QuestionId, DeletedFalse로 조회")
    void Answer_조회_byQuestionId_DeletedFalse(){
        AnswerTest.A1.setDeleted(true);
        answerRepository.save(AnswerTest.A1);
        Answer answerDeletedFalse = answerRepository.save(AnswerTest.A2);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(answers).containsExactly(answerDeletedFalse);
    }

    @Test
    @DisplayName("Answer를 Id, DeletedFalse로 조회")
    void Answer_조회_byId_DeletedFalse(){
        Answer saved = answerRepository.save(AnswerTest.A1);
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(saved.getId());
        assertThat(answer.get()).isEqualTo(saved);
    }
}
