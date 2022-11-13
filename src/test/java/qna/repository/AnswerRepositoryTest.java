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
}
