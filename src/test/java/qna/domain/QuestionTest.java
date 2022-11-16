package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void 삭제처리() throws CannotDeleteException {
        User user1 = userRepository.save(new User("userId", "password", "name", "email"));
        Question question1 = questionRepository.save(new Question("title", "contents").writeBy(user1));
        Answer answer1 = new Answer(user1, question1, "contents");
        question1.addAnswer(answer1);

        List<DeleteHistory> delete = question1.delete(user1);

        assertThat(question1.isDeleted()).isTrue();
        assertThat(delete).isEqualTo(Arrays.asList(new DeleteHistory(ContentType.QUESTION, question1.getId(), question1.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter(), LocalDateTime.now())));
    }
}
