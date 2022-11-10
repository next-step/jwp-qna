package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;
import qna.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.createQuestion;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    private Question question;

    @BeforeEach
    public void setUp() {
        question = createQuestion();
        questionRepository.save(question);
    }

    @Test
    @DisplayName("삭제되지 않은 질문 조회 테스트")
    public void findByDeletedFalseTest() {
        Question question2 = createQuestion();
        question2.setDeleted(true);
        questionRepository.save(question2);

        assertThat(questionRepository.findByDeletedFalse()).hasSize(1);
    }

    @Test
    @DisplayName("질문글아이디 기준 삭제된 질문 조회시 조회결과 없음 테스트")
    public void findByIdAndDeletedFalseTest() {
        question.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isEmpty();
    }

    @Test
    @DisplayName("질문글아이디 기준 삭제되지 않은 질문 조회 테스트")
    public void findByIdAndDeletedFalseTest2() {
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isPresent();
    }
}
