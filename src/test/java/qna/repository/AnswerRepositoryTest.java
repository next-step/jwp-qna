package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.*;

@DataJpaTest
@DisplayName("답변 Repository")
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer_2;

    @BeforeEach
    void setUp() {
        answer_2 = answerRepository.save(ANSWER_2);
    }


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

    @DisplayName("삭제_성공")
    @Test
    void delete() {

        assertThat(answer_2).isNotNull();

        answerRepository.delete(answer_2);

        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(answer_2.getId());

        assertThat(answer.isPresent()).isFalse();
    }
}
