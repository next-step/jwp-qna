package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("AnswerRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void answer_저장_테스트() {
        Answer actual = answerRepository.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals(UserTest.JAVAJIGI.getId(), actual.getWriterId()),
                () -> assertEquals(QuestionTest.Q1.getId(), actual.getQuestionId()),
                () -> assertFalse(actual.isDeleted()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void answer_동일성_보장_테스트() {
        Answer answerSaved = answerRepository.save(AnswerTest.A1);
        Answer answerFound = answerRepository.findById(answerSaved.getId()).get();

        assertTrue(answerSaved == answerFound);
    }

    @Test
    void answer_업데이트_테스트() {
        Answer answerSaved = answerRepository.save(new Answer(UserTest.JAVAJIGI,
                new Question("title", "contents"), "contents"));
        answerSaved.setContents("답변 변경");

        answerRepository.saveAndFlush(answerSaved);

        assertAll(
                () -> assertEquals("답변 변경", answerSaved.getContents()),
                () -> assertTrue(answerSaved.getUpdatedAt().isAfter(answerSaved.getCreatedAt()))
        );
    }

    @Test
    void answer_삭제_테스트() {
        Answer answerSaved = answerRepository.save(AnswerTest.A1);

        answerRepository.deleteById(answerSaved.getId());

        assertThat(answerRepository.findById(answerSaved.getId())).isEmpty();
    }

    @Test
    void 입력된_question_id를_가지고_있고_deleted_가_false인_answer_조회() {
        answerRepository.save(AnswerTest.A1);

        List<Answer> answersFound =
                answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
                () -> assertThat(answersFound).hasSize(1),
                () -> assertTrue(answersFound.stream().noneMatch(Answer::isDeleted))
        );
    }

    @Test
    void 입력된_id와_일치하고_deleted가_false인_answer_조회() {
        Answer answerSaved = answerRepository.save(AnswerTest.A1);
        Answer answerFound = answerRepository.findByIdAndDeletedFalse(answerSaved.getId()).get();

        assertFalse(answerFound.isDeleted());
    }
}
