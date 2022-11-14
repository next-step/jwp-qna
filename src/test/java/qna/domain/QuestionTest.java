package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 저장_및_조회() {
        User user1 = userRepository.save(new User("userId", "password", "name", "email"));
        User user2 = userRepository.save(new User("userId2", "password", "name", "email"));
        Question question1 = questionRepository.save(new Question("title", "contents").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title", "contents").writeBy(user2));

        Question retrievedQuestion1 = questionRepository.findById(question1.getId()).get();
        Question retrievedQuestion2 = questionRepository.findById(question2.getId()).get();

        assertAll(
                () -> assertThat(retrievedQuestion1.getId()).isEqualTo(question1.getId()),
                () -> assertThat(retrievedQuestion2.getId()).isEqualTo(question2.getId())
        );
    }
}
