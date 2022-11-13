package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    private Answer answer;

    @BeforeEach
    public void setUp() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        this.answer = answerRepository.save(createAnswer(user, question));
    }

    @Test
    @DisplayName("저장 테스트 : Audit 데이터 Not Null 테스트")
    public void save() {
        assertThat(answer.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("삭제되지 않은 답변 질문글 기준으로 조회 테스트")
    public void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> byQuestionIdAndDeletedFalse = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());
        assertThat(byQuestionIdAndDeletedFalse).hasSize(1);
    }

    @Test
    @DisplayName("삭제되지 않은 답변글 아이디 기준 조회 테스트 : 삭제되지 않은 경우")
    public void findByIdAndDeletedFalseTest() {
        Optional<Answer> byQuestionIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(byQuestionIdAndDeletedFalse).isPresent();
    }

    @Test
    @DisplayName("삭제되지 않은 답변글 아이디 기준 조회 테스트 : 삭제된 경우")
    public void findByIdAndDeletedFalseTest_deleted() {
        answer.setDeleted(true);
        Optional<Answer> byQuestionIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(byQuestionIdAndDeletedFalse).isNotPresent();
    }
}
