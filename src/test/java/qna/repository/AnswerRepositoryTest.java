package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.Answer;

@DataJpaTest
public class AnswerRepositoryTest {
    public static final Answer ANSWER =
            new Answer(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.QUESTION, "Answers Contents1");
    public static final Answer DELETED_ANSWER =
            new Answer(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.QUESTION, "Answers Contents2");
    public static final Answer ANSWER_WITH_QUESTION_ID =
            new Answer(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.QUESTION_WITH_ID, "Answers Contents3");

    @Autowired
    private AnswerRepository answerRepository;

    @Nested
    @DisplayName("명령")
    class Command {
        @Test
        @DisplayName("새로운 답변을 추가한다.")
        void save() {
            Answer actual = answerRepository.save(ANSWER);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getWriterId()).isEqualTo(ANSWER.getWriterId()),
                    () -> assertThat(actual.getQuestionId()).isEqualTo(ANSWER.getQuestionId()),
                    () -> assertThat(actual.getContents()).isEqualTo(ANSWER.getContents()));
        }
    }

    @Nested
    @DisplayName("조회")
    class Query {
        private Answer answerWithQuestionId;
        private Answer deletedAnswer;

        @BeforeEach
        void setUp() {
            DELETED_ANSWER.setDeleted(true);
            answerWithQuestionId = answerRepository.save(ANSWER_WITH_QUESTION_ID);
            deletedAnswer = answerRepository.save(DELETED_ANSWER);
        }

        @Test
        @DisplayName("질문ID로 삭제되지 않은 답변을 찾는다.")
        void findByQuestionIdAndDeletedFalse() {
            List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(
                    answerWithQuestionId.getQuestionId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).containsExactly(answerWithQuestionId)
            );
        }

        @Test
        @DisplayName("삭제되지 않은 답변을 ID로 찾는다.")
        void findByIdAndDeletedFalse() {
            Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answerWithQuestionId.getId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).map(Answer::getContents).hasValue(answerWithQuestionId.getContents()),
                    () -> assertThat(actual).map(Answer::getWriterId).hasValue(answerWithQuestionId.getWriterId()),
                    () -> assertThat(actual).map(Answer::isDeleted).hasValue(Boolean.FALSE)
            );
        }
    }
}
