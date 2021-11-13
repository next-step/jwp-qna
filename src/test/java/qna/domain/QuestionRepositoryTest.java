package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.CannotDeleteException;
import qna.utils.StringUtils;

import java.util.List;

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
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {
    private static final int MAX_COLUMN_LENGTH = 500;
    @Autowired
    private AnswerRepository answerRepository;
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
        Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title2", "contents2").writeBy(user2));
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

    @Test
    @DisplayName("Question의 답변이 있는지 확인한다.")
    public void T4_hasAnswers() {
        //GIVEN
        Question questionWithAnswer = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question questionOnly = questionRepository.save(new Question("title2", "contents2").writeBy(user2));
        //WHEN
        Answer answer = answerRepository.save(new Answer(user1, questionWithAnswer, "Answers Contents1"));
        questionWithAnswer.addAnswer(answer);
        //THEN
        assertThat(questionWithAnswer.hasAnswers()).isTrue();
        assertThat(questionOnly.hasAnswers()).isFalse();
    }

    @Test
    @DisplayName("Question 의 삭제 가능 검증")
    public void T4_deleteValidate() {
        //GIVEN
        Question QUESTION_NO_ANSWER = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question QUESTION_WITH_OWN_ANSWER = questionRepository.save(new Question("title2", "contents2").writeBy(user1));
        Question QUESTION_WITH_OTHER_ANSWER = questionRepository.save(new Question("title3", "contents3").writeBy(user1));
        //WHEN
        QUESTION_WITH_OWN_ANSWER.addAnswer(answerRepository.save(new Answer(user1, QUESTION_WITH_OWN_ANSWER, "Answers Contents1")));
        QUESTION_WITH_OWN_ANSWER.addAnswer(answerRepository.save(new Answer(user1, QUESTION_WITH_OWN_ANSWER, "Answers Contents2")));

        QUESTION_WITH_OTHER_ANSWER.addAnswer(answerRepository.save(new Answer(user1, QUESTION_WITH_OWN_ANSWER, "Answers Contents3")));
        QUESTION_WITH_OTHER_ANSWER.addAnswer(answerRepository.save(new Answer(user2, QUESTION_WITH_OWN_ANSWER, "Answers Contents4")));

        //THEN
        assertAll(
                () -> assertThat(QUESTION_NO_ANSWER.delete(user1)).hasSize(1),
                () -> assertThat(QUESTION_WITH_OWN_ANSWER.delete(user1)).hasSize(3)
        );
        Question findQuestion1 = questionRepository.findById(QUESTION_NO_ANSWER.getId()).get();
        Question findQuestion2 = questionRepository.findById(QUESTION_WITH_OWN_ANSWER.getId()).get();
        Question findQuestion3 = questionRepository.findById(QUESTION_WITH_OTHER_ANSWER.getId()).get();

        assertAll(
                () -> assertThat(findQuestion1.isDeleted()).isTrue(),
                () -> assertThat(findQuestion2.isDeleted()).isTrue(),
                () -> assertThat(findQuestion3.isDeleted()).isFalse()
        );

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(findQuestion2.getId());
        assertThat(findAnswers).hasSize(0);
    }

    @Test
    @DisplayName("Question 의 삭제 가능 검증 예외")
    public void T4_deleteValidateException() {
        //GIVEN
        Question QUESTION_NO_ANSWER = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question QUESTION_WITH_OWN_ANSWER = questionRepository.save(new Question("title2", "contents2").writeBy(user1));
        Question QUESTION_WITH_OTHER_ANSWER = questionRepository.save(new Question("title3", "contents3").writeBy(user1));
        //WHEN
        QUESTION_WITH_OWN_ANSWER.addAnswer(answerRepository.save(new Answer(user1, QUESTION_WITH_OWN_ANSWER, "Answers Contents1")));
        QUESTION_WITH_OWN_ANSWER.addAnswer(answerRepository.save(new Answer(user1, QUESTION_WITH_OWN_ANSWER, "Answers Contents2")));

        QUESTION_WITH_OTHER_ANSWER.addAnswer(answerRepository.save(new Answer(user1, QUESTION_WITH_OWN_ANSWER, "Answers Contents3")));
        QUESTION_WITH_OTHER_ANSWER.addAnswer(answerRepository.save(new Answer(user2, QUESTION_WITH_OWN_ANSWER, "Answers Contents4")));

        //THEN
        assertAll(
                () -> assertThatThrownBy(() -> QUESTION_NO_ANSWER.delete(user2)).isInstanceOf(CannotDeleteException.class)
                        .hasMessageContaining("질문을 삭제할 권한이 없습니다."),
                () -> assertThatThrownBy(() -> QUESTION_WITH_OTHER_ANSWER.delete(user1)).isInstanceOf(CannotDeleteException.class)
                        .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.")
        );
    }
}
