package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @DisplayName("질문 삭제 기록하기")
    @Test
    void deleteQuestion() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        users.save(alice);
        Question question = new Question("title", "contents").writeBy(alice);
        questions.save(question);

        DeleteHistory deleteHistory = new DeleteHistory(question, alice);
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        assertThat(actual.equals(deleteHistory)).isTrue();
        assertThat(actual.hashCode()).isEqualTo(deleteHistory.hashCode());
    }

    @DisplayName("답변 삭제 기록하기")
    @Test
    void deleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        users.save(alice);
        Question question = new Question("title", "contents").writeBy(alice);
        questions.save(question);
        Answer answer = new Answer(alice, question, "Answer Contents");
        answers.save(answer);

        DeleteHistory deleteHistory = new DeleteHistory(answer, alice);
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        assertThat(actual.equals(deleteHistory)).isTrue();
        assertThat(actual.hashCode()).isEqualTo(deleteHistory.hashCode());
    }
}
