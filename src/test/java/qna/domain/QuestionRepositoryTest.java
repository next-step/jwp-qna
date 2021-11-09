package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;
import qna.utils.StringUtils;

import java.util.Optional;

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
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {
    private static final int MAX_COLUMN_LENGTH = 500;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Question save")
    public void T1_save() {
        //THEN
        assertAll(
                () -> assertThat(questionRepository.save(QuestionTest.Q1).getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(questionRepository.save(QuestionTest.Q2).getTitle()).isEqualTo(QuestionTest.Q2.getTitle())
        );
    }

    @Test
    @DisplayName("Question 유효성체크1 null")
    public void T4_validate() {
        //WHEN
        Question titleNull = new Question(null, "contents1").writeBy(UserTest.JAVAJIGI);
        //THEN
        assertThatThrownBy(() -> questionRepository.save(titleNull)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Question 유효성체크2 길이초과")
    public void T5_validate2() {
        //WHEN
        Question titleLengthOver = new Question(StringUtils.getRandomString(MAX_COLUMN_LENGTH), "contents1").writeBy(UserTest.JAVAJIGI);
        //THEN
        assertThatThrownBy(() -> questionRepository.save(titleLengthOver)).isInstanceOf(DataIntegrityViolationException.class);
    }
}
