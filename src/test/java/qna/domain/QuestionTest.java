package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("not null 저장 테스트")
    public void saveTest() {
        Question question = new Question(null, "content").writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> questionRepository.save(question)).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("삭제된 데이터만 가져오는지 테스트")
    public void findByDeletedFalseTest() {
        Question question = questionRepository.save(Q1);
        question.setDeleted(true);
        questionRepository.save(Q2);
        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();
        assertThat(byDeletedFalse).hasSize(1);
    }

    @Test
    public void findByIdAndDeletedFalseTest() {
        Question question = questionRepository.save(Q1);
        question.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isEmpty();
    }
}
