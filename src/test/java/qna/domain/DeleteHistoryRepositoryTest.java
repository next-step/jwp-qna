package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : qna.domain
 * fileName : DeleteHistoryRepositoryTest
 * author : haedoang
 * date : 2021-11-09Ø
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryRepositoryTest {

    private Question question;
    private User user;

    @Autowired
    private DeleteHistoryRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("DeleteHistory 등록 테스트")
    public void T1_save() throws Exception {
        //GIVEN
        Question QUESTION_NO_ANSWER = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        //WHEN
        List<DeleteHistory> deleteHistories = repository.saveAll(QUESTION_NO_ANSWER.delete(user).getDeleteHistoryList());
        //THEN
        assertThat(deleteHistories).hasSize(1);
    }

    @Test
    @DisplayName("DeleteHistory 에 추가한 Question, Answer는 deleted 컬럼이 true 값이어야 한다.")
    public void T2_save() throws Exception {
        //GIVEN
        Question QUESTION_WITH_OWN_ANSWER = questionRepository.save(new Question("title2", "contents2").writeBy(user));
        //WHEN
        QUESTION_WITH_OWN_ANSWER.addAnswer(answerRepository.save(new Answer(user, QUESTION_WITH_OWN_ANSWER, "Answers Contents1")));
        QUESTION_WITH_OWN_ANSWER.addAnswer(answerRepository.save(new Answer(user, QUESTION_WITH_OWN_ANSWER, "Answers Contents2")));
        //THEN
        List<DeleteHistory> findDeleteHistory = repository.saveAll(QUESTION_WITH_OWN_ANSWER.delete(user).getDeleteHistoryList());
        assertThat(findDeleteHistory).hasSize(3);
        //WHEN
        Question question = questionRepository.findById(QUESTION_WITH_OWN_ANSWER.getId()).get();
        List<Answer> findAnswers = answerRepository.findByQuestionId(QUESTION_WITH_OWN_ANSWER.getId());
        //THEN
        assertThat(question.isDeleted()).isTrue();
        assertThat(findAnswers).extracting(Answer::isDeleted).contains(true);
    }

}
