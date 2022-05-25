package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

class AnswersTest {
    private Answers answers;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "id", "password", "name", "email@test.com");
        Question question = new Question(1L, "title", "contents");

        List<Answer> data = Arrays.asList(
                new Answer(user, question, "answer1"),
                new Answer(user, question, "answer2"),
                new Answer(user, question, "answer3")
        );
        answers = new Answers(data);
    }

    @Test
    @DisplayName("Answer의 개수를 확인한다.")
    void 개수_확인() {
        assertThat(answers.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("Answer를 추가한다.")
    void 추가() {
        answers.add(new Answer());
        assertThat(answers.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("모든 Answer를 삭제한다.")
    void 모두_삭제() throws CannotDeleteException {
        DeleteHistories actual = answers.deleteAll(user);
        assertThat(actual.get()).hasSize(3);
    }
}
