package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.domain.utils.JpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QuestionRepository")
class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("질문 정보가 주어지면")
        class Context_with_question_data extends JpaTest {
            final Question expected = Q1;

            @Test
            @DisplayName("질문 정보를 저장하고, 질문 객체를 리턴한다")
            void it_saves_question_and_returns_question() {
                Question actual = getQuestionRepository().save(expected);

                assertThat(actual).extracting("title").isEqualTo(Q1.getTitle());
                assertThat(actual).extracting("contents").isEqualTo(Q1.getContents());
            }
        }
    }

    @Nested
    @DisplayName("findByDeletedFalse 메서드는")
    class Describe_findByDeletedFalse {

        @Nested
        @DisplayName("저장된 질문 객체가 주어지면")
        class Context_with_questions extends JpaTest {
            Question question;

            @BeforeEach
            void setUp() {
                question = getQuestionRepository().save(Q1);
                question.setDeleted(true);
                getQuestionRepository().save(Q2);
            }

            @Test
            @DisplayName("삭제되지않은 질문 목록을 리턴한다.")
            void it_returns_not_deleted_questions() {
                List<Question> questions = getQuestionRepository().findByDeletedFalse();

                assertThat(questions.size()).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("findByIdAndDeletedFalse 메서드는")
    class Describe_findByIdAndDeletedFalse {

        @Nested
        @DisplayName("저장된 질문 객체가 주어지면")
        class Context_with_question extends JpaTest {
            Question givenQuestion;

            long givenId() {
                return givenQuestion.getId();
            }

            @BeforeEach
            void setUp() {
                givenQuestion = getQuestionRepository().save(Q1);
            }

            @Test
            @DisplayName("삭제되지않은 질문을 리턴한다.")
            void it_returns_not_deleted_question() {
                Question question = getQuestionRepository()
                        .findByIdAndDeletedFalse(givenId())
                        .orElseThrow(EntityNotFoundException::new);
                assertThat(question).isNotNull();
            }
        }
    }
}
