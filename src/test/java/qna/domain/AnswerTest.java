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
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(UserTest.JAVAJIGI);
        user2 = userRepository.save(UserTest.SANJIGI);

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(user2));

        answer1 = answerRepository.save(new Answer(user1, question1, A1.getContents()));
        answer2 = answerRepository.save(new Answer(user2, question1, A2.getContents()));
    }

    @Test
    @DisplayName("Answer 저장 확인")
    void saveTest() {
        assertAll(
                () -> assertThat(answer1.getId()).isNotNull(),
                () -> assertThat(answer1.getWriter()).isEqualTo(question1.getWriter()),
                () -> assertThat(answer1.getQuestion()).isEqualTo(question1),
                () -> assertThat(answer1.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("question id 값으로 deleted false 리스트 확인")
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());

        assertThat(actualList).contains(answer1, answer2);
    }

    @Test
    @DisplayName("Deleted값이 false인 answer 찾기")
    void findByIdAndDeletedFalseTest() {
        Answer result = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();
        assertThat(result.getId()).isEqualTo(answer1.getId());

        assertAll(
                () -> assertThat(result.getWriter()).isEqualTo(answer1.getWriter()),
                () -> assertThat(result.getQuestion()).isEqualTo(answer1.getQuestion()),
                () -> assertThat(result.getContents()).isEqualTo(answer1.getContents())
        ) ;
    }
}
