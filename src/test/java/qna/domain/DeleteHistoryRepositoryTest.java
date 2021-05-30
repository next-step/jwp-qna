package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("질문 삭제 기록하기")
    @Test
    void deleteQuestion() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question = new Question("title", "contents").writeBy(alice);
        questionRepository.save(question);

        DeleteHistory deleteHistory = new DeleteHistory(question, alice);
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        assertThat(actual.equals(deleteHistory)).isTrue();
        assertThat(actual.hashCode()).isEqualTo(deleteHistory.hashCode());
    }

    @DisplayName("답변 삭제 기록하기")
    @Test
    void deleteAnswer() {
        User alice = new User("alice", "password", "Alice", "alice@mail");
        userRepository.save(alice);
        Question question = new Question("title", "contents").writeBy(alice);
        questionRepository.save(question);
        Answer answer = new Answer(alice, question, "Answer Contents");
        answerRepository.save(answer);

        DeleteHistory deleteHistory = new DeleteHistory(answer, alice);
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        assertThat(actual.equals(deleteHistory)).isTrue();
        assertThat(actual.hashCode()).isEqualTo(deleteHistory.hashCode());
    }
}
