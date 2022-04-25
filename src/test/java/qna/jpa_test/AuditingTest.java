package qna.jpa_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.List;

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
        UserInfo userInfo = new UserInfo("id", "pwd", "writer", "writer@slipp.net");
        writer = userRepository.save(new User(userInfo));
        question = questionRepository.save(new Question("title", new Contents("question")).writeBy(writer));
    }

    @Test
    @DisplayName("CreatedDate 학습 테스트")
    void createdDateTest() {
        LocalDateTime now = LocalDateTime.now();
        Answer answer = answerRepository.save(new Answer(writer, question, new Contents("A1")));
        LocalDateTime createTime = answer.getCreatedAt();
        assertThat(createTime).isAfter(now);
        assertThat(createTime).isNotNull();
    }

    @Test
    @DisplayName("LastModifiedDate 학습 테스트")
    void LastModifiedDate() {
        Answer answer = new Answer(writer, question, new Contents("after update"));
        LocalDateTime before = LocalDateTime.now();
        Answer actual = answerRepository.save(answer);

        actual.setContents(new Contents("after update"));
        LocalDateTime after = actual.getUpdatedAt();

        assertThat(after).isNotEqualTo(before);
    }
}
