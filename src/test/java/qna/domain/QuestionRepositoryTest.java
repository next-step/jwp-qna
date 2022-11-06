package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static qna.domain.AnswerTest.A1;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 답변_저장_조회후_삭제() {
        userRepository.save(JAVAJIGI);
        Q1.addAnswer(A1);
        questionRepository.save(Q1);

        entityManager.clear();

        Question question = questionRepository.findById(Q1.getId()).get();
        question.delete(JAVAJIGI);

        entityManager.flush();
    }

}