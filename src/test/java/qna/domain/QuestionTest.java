package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setup() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User("leejun", "password", "name", "leejun@slipp.net");

        userRepository.save(user1);
        userRepository.save(user2);

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents1").writeBy(user2));
    }

    @Test
    @DisplayName("Question 저장 확인")
    void saveTest() {
        Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        assertThat(question1.getId()).isNotNull();
        assertThat(question1.getWriter()).isEqualTo(question1.getWriter());
        assertThat(question1.getContents()).isEqualTo(question1.getContents());
        assertThat(question1.getTitle()).isEqualTo(question1.getTitle());
    }

    @Test
    @DisplayName("deleted false 찾기 테스트")
    void findByDeletedFalseTest() {
        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("deleted false 찾기 테스트")
    void findByIdAndDeletedFalseTest() {
        Question question1 = questionRepository.save(Q1);
        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();

        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getContents()).isEqualTo(question1.getContents());
        assertThat(result.getTitle()).isEqualTo(question1.getTitle());
        assertThat(result.getWriter()).isEqualTo(question1.getWriter());
    }
}
