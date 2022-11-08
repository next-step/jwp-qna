package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    public void cleanup() {
        answerRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("answer 저장 테스트")
    void save() {
        Answer answer = answerRepository.save((A1));
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(A1.getQuestionId())
        );
    }

    @Test
    @DisplayName("answer 저장 후 조회 테스트")
    void findByIdAndDeletedFalse_test() {
        // given
        Answer answer = answerRepository.save(A1);
        Long saveAnswerId = answer.getId();
        // when
        answerRepository.save(A2);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);
        // then
        assertThat(findAnswer.isPresent()).isTrue();
        findAnswer.ifPresent(paramAnswer -> assertAll(
                () -> assertThat(paramAnswer).isEqualTo(answer),
                () -> assertThat(paramAnswer.isDeleted()).isFalse(),
                () -> assertThat(paramAnswer).isNotEqualTo(A2)
        ));
    }

    @Test
    @DisplayName("answer 삭제 set 후 조회 시 미조회 테스트")
    void set_delete_find_test() {
        // given
        Answer answer = answerRepository.save(A1);
        Long id = answer.getId();
        // when
        answer.setDeleted(true);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(id);
        // then
        assertThat(findAnswer.isPresent()).isFalse();
    }

    @Test
    @DisplayName("answer 삭제 후 조회 시 미조회 테스트")
    void delete_find_test() {
        // given
        Answer saveAnswer = answerRepository.save(A1);
        Long id = saveAnswer.getId();
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(id);
        // when
        findAnswer.ifPresent(answer -> answerRepository.delete(answer));
        Optional<Answer> deletedAnswer = answerRepository.findByIdAndDeletedFalse(id);
        // then
        assertThat(deletedAnswer.isPresent()).isFalse();
    }
}