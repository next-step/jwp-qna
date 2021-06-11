package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
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
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User("leejun", "password", "name", "leejun@slipp.net");

        userRepository.save(user1);
        userRepository.save(user2);

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(user2));

        Answer A1 = new Answer(user1, question1, "contents1");
        Answer A2 = new Answer(user2, question2, "contents2");

        answer1 = answerRepository.save(new Answer(user1, question1, A1.getContents()));
        answer2 = answerRepository.save(new Answer(user2, question2, A2.getContents()));
    }

    @Test
    @DisplayName("Answer 저장 테스트")
    public void saveTest() {
        assertThat(answer1.getId()).isNotNull();
        assertThat(answer1.getWriter()).isEqualTo(answer1.getWriter());
        assertThat(answer1.getQuestion()).isEqualTo(answer1.getQuestion());
        assertThat(answer1.getContents()).isEqualTo(answer1.getContents());
    }

    @Test
    @DisplayName("Question id로 deleted false 찾기 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());

        assertThat(actualList).contains(answer1);
    }

    @Test
    @DisplayName("answer id로 deleted false 찾기 테스트")
    void findByIdAndDeletedFalseTest() {
        Answer result = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();
        assertThat(result.getId()).isEqualTo(answer1.getId());

        assertThat(result.getWriter()).isEqualTo(answer1.getWriter());
        assertThat(result.getQuestion()).isEqualTo(answer1.getQuestion());
        assertThat(result.getContents()).isEqualTo(answer1.getContents());
    }
}
