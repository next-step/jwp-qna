package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.domain.Answer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_1;

@DisplayName("답변 Repository")
class AnswerRepositoryTest extends RepositoryTest{

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        Answer answer = answerRepository.save(new Answer(javajigi, question1, ANSWERS_CONTENTS_1));

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriterId()).isNotNull(),
                () -> assertThat(answer.getQuestion().getId()).isNotNull(),
                () -> assertThat(answer.getCreatedAt()).isNotNull(),
                () -> assertThat(answer.getUpdatedAt()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(ANSWERS_CONTENTS_1)
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse_조회_성공")
    @Test
    void findByQuestionIdAndDeletedFalse() {

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer2.getQuestion().getId());

        assertAll(
                () -> assertThat(answers).hasSize(1),
                () -> assertThat(answers).containsExactly(answer2)
        );
    }

    @DisplayName("findByIdAndDeletedFalse_조회_성공")
    @Test
    void findByIdAndDeletedFalse() {

        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer2.getId()).orElse(null);

        assertAll(
                () -> assertThat(findAnswer).isNotNull(),
                () -> assertThat(findAnswer).isEqualTo(answer2)
        );
    }

    @DisplayName("삭제_성공")
    @Test
    void delete() {

        assertThat(answer2).isNotNull();

        answerRepository.delete(answer2);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer2.getId())).isNotPresent();
    }
}
