package qna.domain;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.DirtiesContext;
import qna.utils.StringUtils;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : qna.domain
 * fileName : QuestionHistoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {
    private static final int MAX_COLUMN_LENGTH = 500;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        this.user1 = userRepository.save(UserTest.JAVAJIGI);
        this.user2 = userRepository.save(UserTest.SANJIGI);
    }


    @Test
    @DisplayName("Question 검증 테스트")
    public void T1_questionSaveTest() {
        //WHEN
        Question question1 = questionRepository.save(QuestionTest.Q1);
        Question question2 = questionRepository.save(QuestionTest.Q2);
        //THEN
        assertAll(
                () -> assertThat(question1.isOwner(user1)).isTrue(),
                () -> assertThat(question2.isOwner(user2)).isTrue()
        );
    }

    @Test
    @DisplayName("Question 유효성체크1 null")
    public void T2_validate() {
        //WHEN
        Question titleNull = new Question(null, "contents1");
        //THEN
        assertThatThrownBy(() -> questionRepository.save(titleNull)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Question 유효성체크2 길이초과")
    public void T3_validate2() {
        //WHEN
        Question titleLengthOver = new Question(StringUtils.getRandomString(MAX_COLUMN_LENGTH), "contents1");
        //THEN
        assertThatThrownBy(() -> questionRepository.save(titleLengthOver)).isInstanceOf(DataIntegrityViolationException.class);
    }



}
