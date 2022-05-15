package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.Question;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question QUESTION =
            new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question DELETED_QUESTION =
            new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);
    public static final Question QUESTION_WITH_ID =
            new Question(3L, "title3", "contents3").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Nested
    @DisplayName("명령")
    class Command {
        @Test
        @DisplayName("새로운 질문을 추가한다.")
        void save() {
            Question actual = questionRepository.save(QUESTION);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getTitle()).isEqualTo(QUESTION.getTitle()),
                    () -> assertThat(actual.getContents()).isEqualTo(QUESTION.getContents())
            );
        }
    }

    @Nested
    @DisplayName("조회")
    class Query {
        private List<Question> savedQuestions;
        private List<Question> questionsNotDeleted;

        @BeforeEach
        void setUp() {
            DELETED_QUESTION.setDeleted(true);
            savedQuestions = questionRepository.saveAll(Arrays.asList(QUESTION, DELETED_QUESTION, QUESTION_WITH_ID));
            questionsNotDeleted = savedQuestions.stream()
                    .filter(question -> !question.isDeleted())
                    .collect(Collectors.toList());
        }

        @Test
        @DisplayName("UserId로 질문을 찾는다.")
        void findByUserId() {
            Question expected = savedQuestions.get(0);
            Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).map(Question::getTitle).hasValue(expected.getTitle()),
                    () -> assertThat(actual).map(Question::getContents).hasValue(expected.getContents()),
                    () -> assertThat(actual).map(Question::isDeleted).hasValue(Boolean.FALSE)
            );
        }

        @Test
        @DisplayName("삭제되지 않은 질문을 찾는다.")
        void findByDeletedFalse() {
            List<Question> actual = questionRepository.findByDeletedFalse();
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).hasSameElementsAs(questionsNotDeleted)
            );
        }
    }
}
