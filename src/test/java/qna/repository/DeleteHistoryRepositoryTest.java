package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("mins99", "1234", "ms", "mins99@slipp.net"));
        question = questionRepository.save(new Question("title3", "contents3").writeBy(user));
    }

    @Test
    void save() {
        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), user,
                LocalDateTime.now());

        // when
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        // then
        assertThat(actual).isNotNull();
    }
}
