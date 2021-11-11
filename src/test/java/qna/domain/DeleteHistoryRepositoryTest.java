package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : qna.domain
 * fileName : DeleteHistoryRepositoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryRepositoryTest {

    private Question question;

    private User user;

    @Autowired
    private DeleteHistoryRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.user = userRepository.save(UserTest.JAVAJIGI);
        this.question = questionRepository.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("DeleteHistory save")
    public void T1_save() {
        //GIVEN
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now());
        //WHEN
        DeleteHistory savedHistory = repository.save(deleteHistory);
        //THEN
        assertThat(savedHistory.equals(deleteHistory)).isTrue();
    }

}
