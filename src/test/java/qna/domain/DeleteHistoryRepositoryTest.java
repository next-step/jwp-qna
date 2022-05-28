package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void DeleteHistory_생성() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title10", "contents10").writeBy(user));
        DeleteHistory expected = DeleteHistory.ofQuestion(question.getId(), user);

        DeleteHistory actual = deleteHistoryRepository.save(expected);

        assertThat(actual).isEqualTo(expected);

    }
}
