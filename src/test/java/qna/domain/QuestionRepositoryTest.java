package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(UserRepositoryTest.JAVAJIGI);
        user2 = userRepository.save(UserRepositoryTest.SANJIGI);
    }

    @Test
    @DisplayName("question save 확인")
    void save() {
        Question question = Q1;
        Question result = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getWriter().getId()).isEqualTo(user1.getId()),
                () -> assertThat(result.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question.getTitle())
        );
    }

    @Test
    @DisplayName("deleted 값이 false인 Question 조회")
    void findByDeletedFalse() {
        Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title1", "contents1").writeBy(user2));

        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("id로 deleted값이 false인 Question 조회")
    void findByIdAndDeletedFalse() {
        Question question1 = questionRepository.save(Q1);

        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();
        assertAll(
                () -> assertThat(result.isDeleted()).isFalse(),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(question1.getWriter())
        );
    }

    @Test
    @DisplayName("Question Answer 양방향 테스트")
    void findById() {
        Question question = new Question(1L, "title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
        Answer answer1= new Answer(user1, question, "Answers Contents1");
        Answer answer2 = new Answer(user1, question, "Answers Contents2");
        question.addAnswers(answer1);
        question.addAnswers(answer2);

        List<Answer> result = question.getAnswers();

        assertAll (
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).contains(answer1, answer2)
        );
    }
}
