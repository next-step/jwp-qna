package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


/**
 * packageName : qna.domain
 * fileName : AnswerRepositoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Question question1;

    @BeforeEach
    void setUp() {
        this.user1 = userRepository.save(UserTest.JAVAJIGI);
        this.user2 = userRepository.save(UserTest.SANJIGI);
        this.question1 = questionRepository.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("Answer 검증 테스트")
    public void T1_answerSaveTest() {
        //WHEN
        Answer answer = answerRepository.save(AnswerTest.A1);
        Answer answer2 = answerRepository.save(AnswerTest.A2);
        //THEN
        assertAll(
                () -> assertThat(answer.isOwner(user1)).isTrue(),
                () -> assertThat(answer2.isOwner(user2)).isTrue(),
                () -> assertThat(answer.getWriter()).isEqualTo(user1),
                () -> assertThat(answer2.getWriter()).isEqualTo(user2),
                () -> assertThat(answer.getQuestion()).isEqualTo(question1),
                () -> assertThat(answer2.getQuestion()).isEqualTo(question1)
        );
    }
}
