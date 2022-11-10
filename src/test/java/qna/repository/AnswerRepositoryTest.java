package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

@DisplayName("Answer_관련_테스트")
@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager manager;

    private User user1;
    private User user2;
    private Question question;

    @BeforeEach
    void before() {
        user1 = userRepository.save(UserTest.JAVAJIGI);
        user2 = userRepository.save(UserTest.SANJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
    }

    @AfterEach
    void after() {
        manager.flush();
        manager.clear();
    }

    @DisplayName("저장_확인")
    @Test
    void save() {
        Answer inputAnswer = new Answer(user1, question, "Answers Contents1");
        Answer answer = answerRepository.save(inputAnswer);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(inputAnswer.getContents()),
                () -> assertThat(answer.getWriter()).isEqualTo(inputAnswer.getWriter()),
                () -> assertThat(answer.getQuestion()).isEqualTo(inputAnswer.getQuestion())
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse_question_id_기준으로_조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer answer1 = answerRepository.save(new Answer(user1, question, "Answers Contents1"));
        Answer answer2 = answerRepository.save(new Answer(user2, question, "Answers Contents2"));
        List<Answer> expectedResults = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestion().getId());

        assertThat(expectedResults).contains(answer1);
    }

    @DisplayName("findByIdAndDeletedFalse_deleted_false인_data_조회")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer1 = answerRepository.save(new Answer(user1, question, "Answers Contents1"));
        Answer expectedResult = answerRepository.findByIdAndDeletedFalse(answer1.getId()).orElse(null);

        assertThat(expectedResult).isEqualTo(answer1);
    }
}
