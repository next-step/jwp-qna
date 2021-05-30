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
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(UserTest.JAVAJIGI);
        user2 = userRepository.save(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("Question 저장 확인")
    void saveTest() {
        Question result = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getWriter().getId()).isEqualTo(user1.getId()),
                () -> assertThat(result.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @Test
    @DisplayName("deleted 값이 false인 Question 조회")
    void findByDeletedFalseTest() {
        Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title1", "contents1").writeBy(user2));

        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("id로 deleted값이 false인 Question 조회")
    void findByIdAndDeletedFalseTest() {
        Question question1 = questionRepository.save(Q1);

        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();

        assertAll(
                () -> assertThat(result.isDeleted()).isFalse(),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(question1.getWriter())
        );
    }
}
