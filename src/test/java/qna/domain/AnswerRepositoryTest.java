package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("질문에 달린 답변 중 삭제되지 않은 답변 목록을 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        //when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(2L);

        //then
        assertThat(answers.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("삭제되지 않은 답변 한 건을 조회한다.")
    void findByIdAndDeletedFalse() {
        //when
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(1L);

        //then
        assertThat(findAnswer).hasValueSatisfying(answer -> assertAll(
                () -> assertThat(answer.getWriter().getUserId()).isEqualTo("sanjigi"),
                () -> assertThat(answer.getQuestion().getTitle()).isEqualTo("question1"),
                () -> assertThat(answer.getContents()).isEqualTo("Answers Contents1")
        ));
    }
}
