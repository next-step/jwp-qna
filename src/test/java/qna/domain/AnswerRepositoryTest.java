package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.domain.utils.JpaTest;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerRepositoryTest {
    private User user = new User("tester", "password", "tester", "test@test.com");
    private Question question1 = new Question("title", "content");
    private Answer answer1 = new Answer(user, question1, "content");

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("답변 정보가 주어지면")
        class Context_with_answer extends JpaTest {
            final Answer expected = answer1;

            @Test
            @DisplayName("답변을 저장하고, 답변 객체를 리턴한다")
            void it_saves_answer_and_returns_answer() {
                Answer actual = getAnswerRepository().save(expected);

                assertThat(actual).isEqualTo(expected);
            }
        }
    }

    @Nested
    @DisplayName("findByIdAndDeletedFalse 메서드는")
    class Describe_find_by_id_and_deleted_false {

        @Nested
        @DisplayName("저장된 답변 이력과 식별자가 주어지면 ")
        class Context_with_answer_and_id extends JpaTest {
            Answer givenAnswer1 = answer1;

            long givenId() {
                return givenAnswer1.getId();
            }

            @BeforeEach
            void setUp() {
                getAnswerRepository().save(givenAnswer1);
            }

            @Test
            @DisplayName("식별키에 해당하는 답변을 리턴한다")
            void it_returns_answer() {
                final Answer actual = getAnswerRepository().findByIdAndDeletedFalse(givenId())
                        .orElseThrow(EntityNotFoundException::new);

                assertThat(actual).isEqualTo(givenAnswer1);
            }
        }
    }
}
