package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

class AnswersTest {

    private User u1;
    private User u2;
    private Answer a1;
    private Answer a2;
    private Answer a3;

    @BeforeEach
    void setup() {
        u1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        u2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        Question q1 = new Question(1L, "title1", "contents1").writeBy(u1);
        Question q2 = new Question(2L, "title2", "contents3").writeBy(u1);
        a1 = new Answer(1L, u1, q1, "contents");
        a2 = new Answer(2L, u1, q2, "contents");
        a3 = new Answer(3L, u2, q1, "contents");
    }


    @DisplayName("삭제가능한 Answers를 삭제하면 DeleteHistories를 반환한다")
    @Test
    void deleteAll_test() throws CannotDeleteException {
        Answers answers = new Answers();
        Stream.of(a1, a2).forEach(answers::add);

        DeleteHistories deleteHistories = answers.deleteAll(u1);
        assertNotNull(deleteHistories);
    }

    @DisplayName("Answers에 Login 유저 외의 Answer가 포함되어 있으면 CannotDeleteException을 발생시킨다.")
    @Test
    void deleteAll_exception() {
        Answers answers = new Answers();
        Stream.of(a1, a2, a3).forEach(answers::add);
        assertThatThrownBy(() -> answers.deleteAll(u1))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("답변을 추가할 수 있다.")
    @Test
    void update_test() {
        Answers answers = new Answers();
        Stream.of(a1, a2).forEach(answers::add);
        answers.add(a3);
        assertTrue(answers.contains(a3));
    }

}