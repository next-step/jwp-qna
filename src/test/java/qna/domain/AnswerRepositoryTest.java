package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("답장을 저장한다.")
    @Test
    void save() {
        Answer answer = AnswerTest.A1;
        final Answer savedAnswer = answerRepository.save(answer);
        assertThat(savedAnswer.getId()).isNotNull();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isNotNull(),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
            () -> assertThat(savedAnswer.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장한다.")
    @Test
    void sameEntity() {
        final Answer saved = answerRepository.save(AnswerTest.A2);
        final Answer answer = answerRepository.findById(saved.getId()).get();
        assertThat(answer.getId()).isEqualTo(saved.getId());
        assertThat(answer).isEqualTo(saved);
    }

    @DisplayName("Answer에 대한 Question을 변경한다.")
    @Test
    void toQuestion() {
        final Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "test");
        answer.toQuestion(new Question(3L, "3", "3"));
        final Answer saved = answerRepository.save(answer);
        assertThat(saved.getQuestionId()).isEqualTo(3L);
    }
}
