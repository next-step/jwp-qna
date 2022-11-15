package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 저장_및_조회() {
        User user1 = userRepository.save(new User("userId", "password", "name", "email"));
        User user2 = userRepository.save(new User("userId2", "password", "name", "email"));
        Question question1 = questionRepository.save(new Question("title", "contents").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title", "contents").writeBy(user2));
        Answer answer1 = answerRepository.save(new Answer(user1, question1, "contents"));
        Answer answer2 = answerRepository.save(new Answer(user2, question2, "contents"));

        Answer retrievedAnswer1 = answerRepository.findById(answer1.getId()).get();
        Answer retrievedAnswer2 = answerRepository.findById(answer2.getId()).get();

        assertAll(
                () -> assertThat(retrievedAnswer1.getId()).isEqualTo(answer1.getId()),
                () -> assertThat(retrievedAnswer2.getId()).isEqualTo(answer2.getId())
        );
    }

    @Test
    void 삭제처리() {
        User user1 = userRepository.save(new User("userId", "password", "name", "email"));
        Question question1 = questionRepository.save(new Question("title", "contents").writeBy(user1));
        Answer answer1 = answerRepository.save(new Answer(user1, question1, "contents"));

        DeleteHistory delete = answer1.delete();

        assertThat(answer1.isDeleted()).isTrue();
        assertThat(delete).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer1.getId(), user1, LocalDateTime.now()));
    }
}
