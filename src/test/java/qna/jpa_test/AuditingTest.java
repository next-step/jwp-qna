package qna.jpa_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuditingTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    /**
     * Auditing 학습 테스트
     **/

    @BeforeEach
    void setting() {
        UserLogin userLogin = new UserLogin("id", "pwd", "writer@slipp.net");
        writer = userRepository.save(new User("writer", userLogin));
        question = questionRepository.save(new Question("title", "question").writeBy(writer));
    }

    @Test
    @DisplayName("CreatedDate 학습 테스트")
    void createdDateTest() {
        Answer answer = answerRepository.save(new Answer(writer, question, "A1"));
        LocalDateTime createTime = answer.getCreatedAt();
        assertThat(createTime).isNotNull();
        assertThat(createTime).isNotEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("LastModifiedDate 학습 테스트")
    void LastModifiedDate() {
        Answer answer = new Answer(writer, question, "before update");
        Answer actual = answerRepository.save(answer);
        LocalDateTime before = actual.getCreatedAt();

        actual.setContents("after update");
        Answer persist = answerRepository.findAll().get(0);
        LocalDateTime after = persist.getUpdatedAt();

        assertThat(after).isNotEqualTo(before);
    }
}
