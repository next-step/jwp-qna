package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.*;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_1;
import static qna.domain.AnswerTest.ANSWER_1;

@DataJpaTest
@DisplayName("답변 Repository")
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        Answer answer = answerRepository.save(ANSWER_1);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriterId()).isNotNull(),
                () -> assertThat(answer.getQuestionId()).isNotNull(),
                () -> assertThat(answer.getCreatedAt()).isNotNull(),
                () -> assertThat(answer.getUpdatedAt()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(ANSWERS_CONTENTS_1)
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 조회 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {

        Answer answer1 = answerRepository.save(ANSWER_1);
        Answer answer2 = answerRepository.save(ANSWER_2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestionId());

        assertAll(
                () -> assertThat(answers).hasSize(1),
                () -> assertThat(answers).containsExactly(answer1)
        );
    }

    @DisplayName("findByIdAndDeletedFalse 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {

        Answer answer = answerRepository.save(ANSWER_1);
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getQuestionId()).orElse(null);

        assertAll(
                () -> assertThat(findAnswer).isNotNull(),
                () -> assertThat(findAnswer).isEqualTo(answer)
        );
    }
}
