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

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    EntityManager entityManager;

    private User user;
    private Question question;

    @BeforeEach
    public void setUp() {
        user = createUser("DEVELOPYO");
        question = createQuestion().writeBy(user);
        questionRepository.save(question);
    }

    @Test
    @DisplayName("삭제된질문 1개, 삭제되지않은 질문 1개가 있을 때, 삭제false 데이터만 조회시 삭제된 질문만 조회한다")
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

    @Test
    @DisplayName("answers @where 테스트 : 질문글 조회시 삭제되지 않은 답변만 조회된다")
    public void findAll() {
        Answer answer = createAnswer(user, question);
        answer.delete(user);
        answerRepository.save(answer);
        Answer answer2 = createAnswer(user, question);
        answerRepository.save(answer2);
        entityManager.detach(question);
        assertThat(questionRepository.findById(question.getId()).get().getAnswers().getAnswers()).hasSize(1);
    }
}
