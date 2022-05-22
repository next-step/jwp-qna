package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setup() {
        user = new User("yang123", "pass123", "yangName", "rhfpdk92@naver.com");
        question = new Question("제목", "내용");
        entityManager.persist(user);
        entityManager.persist(question);

        answer = new Answer(user, question, "contesnts");
        answer = answerRepository.save(answer);
    }

    @Test
    void answer_저장() {
        assertThat(answer).isNotNull();
        assertThat(answer.getContents()).isEqualTo("contesnts");
    }
    
    @Test
    void questionId로_삭제되지_않은_answer들_조회(){
        List<Answer> byQuestionIdAndDeletedFalse = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(byQuestionIdAndDeletedFalse).hasSize(1);
    }

    @Test
    void id로_삭제되지_않은_answer조회(){
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(1L)
                .orElseThrow(IllegalArgumentException::new);
        assertThat(findAnswer).isEqualTo(answer);
    }
}
