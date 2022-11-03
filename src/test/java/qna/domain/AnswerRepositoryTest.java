package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void init() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변을 저장할 수 있어야 한다.")
    void save() {
        Answer answer = A1;
        Answer savedAnswer = answerRepository.save(answer);
        assertThat(savedAnswer.getId()).isNotNull();

        assertAll(
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull());
    }

    @Test
    @DisplayName("AnswerId로 삭제상태가 아닌 Answer를 가져올 수 있어야 한다.")
    void findByIdAndDeletedFalse() {
        answerRepository.save(A1);

        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(A1.getId());

        assertThat(answer).contains(A1);
    }

    @DisplayName("toQuestion을 이용해 Answer에 대한 Question을 변경한다.")
    @Test
    void modifyQuestion() {
        final Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "test");
        answer.toQuestion(new Question(1L, "test title", "test contents"));
        final Answer saved = answerRepository.save(answer);
        assertThat(saved.getQuestionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("삭제 상태가 아닌 결과를 리턴한다.")
    void findByQuestionIdAndDeleted() {
        answerRepository.saveAll(Arrays.asList(A1, A2));
        List<Answer> savedAnswers = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());

        savedAnswers.forEach(answer -> {
            assertThat(answer.isDeleted()).isEqualTo(false);
        });

        assertThat(savedAnswers).usingFieldByFieldElementComparator()
                .containsExactly(A1, A2);
    }
}
