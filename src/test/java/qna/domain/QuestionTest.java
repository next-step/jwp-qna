package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

        userRepository.save(user1);
        userRepository.save(user2);

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents1").writeBy(user2));
    }

    @Test
    @DisplayName("Question 저장 확인")
    void saveTest() {
        Question result = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getWriter().getId()).isEqualTo(user1.getId()),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle())
        );
    }

    @Test
    @DisplayName("deleted 값이 false인 Question 조회")
    void findByDeletedFalseTest() {
        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("id로 deleted값이 false인 Question 조회")
    void findByIdAndDeletedFalseTest() {
        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();

        assertAll(
                () -> assertThat(result.isDeleted()).isFalse(),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(question1.getWriter())
        );
    }
}
