package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.entity.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private User user;
    private Question question;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(UserTest.USER_JAVAJIGI);
        question = questionRepository.save(QuestionTest.QUESTION_OF_JAVAJIGI);
    }

    @Test
    public void exists() throws CannotDeleteException {
        List<DeleteHistory> actual = deleteHistoryRepository.saveAll(question.deleted(user).list());

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(1)
        );
    }
}
