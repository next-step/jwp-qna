package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 답변_생성() {

        Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    void 질문_아이디로_삭제_되지_않은_답변_조회() {
        Answer deletedAnswer = answerRepository.save(
                new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        Answer notDeletedAnswer = answerRepository.save(
                new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2"));

        deletedAnswer.setDeleted(true);

        List<Answer> foundAnswers = answerRepository.findByQuestionIdAndDeletedFalse(deletedAnswer.getQuestionId());

        assertThat(foundAnswers).containsExactly(notDeletedAnswer);
    }

    @Test
    void 아이디로_삭제_되지_않은_답변_조회() {
        Answer deletedAnswer = answerRepository.save(
                new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        Answer notDeletedAnswer = answerRepository.save(
                new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2"));

        deletedAnswer.setDeleted(true);
        
        Optional<Answer> foundDeletedAnswer = answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId());
        Optional<Answer> foundNotDeletedAnswer = answerRepository.findByIdAndDeletedFalse(notDeletedAnswer.getId());
        assertAll(
                () -> assertThat(foundDeletedAnswer.isPresent()).isFalse(),
                () -> assertThat(foundNotDeletedAnswer.isPresent()).isTrue()
        );
    }
}
