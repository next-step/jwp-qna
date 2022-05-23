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
        Question question = questionRepository.save(QuestionTest.Q1);
        User user = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question.getId(), user.getId(),
                LocalDateTime.now());

        DeleteHistory actual = deleteHistoryRepository.save(expected);

        assertThat(actual).isEqualTo(expected);

    }
}
