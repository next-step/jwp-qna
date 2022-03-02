package qna.jpa_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuditingTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("CreatedDate 학습 테스트")
    void createdDateTest() {
        LocalDateTime now = LocalDateTime.now();
        Answer answer = answerRepository.save(AnswerTest.A1);
        LocalDateTime createTime = answer.getCreatedAt();
        assertThat(createTime).isAfter(now);
        assertThat(createTime).isNotNull();
    }

    @Test
    @DisplayName("LastModifiedDate 학습 테스트")
    void LastModifiedDate() {
        LocalDateTime before = LocalDateTime.now();
        Answer answer = answerRepository.save(AnswerTest.A2);

        List<Answer> actual = answerRepository.findAll();
        LocalDateTime after = actual.get(0).getUpdatedAt();

        assertThat(after).isAfter(before);
        assertThat(after).isNotNull();
    }
}
