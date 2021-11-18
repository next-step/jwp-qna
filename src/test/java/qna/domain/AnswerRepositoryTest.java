package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AnswerRepositoryTest {

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @Test
    void 삭제되지_않은_대상건중_한건을_true변경시_제외한_대상건만_조회() {

        //given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        Question question = new Question("title", "content").writeBy(user);
        question.addAnswer(new Answer(user, question, "answer"));
        question.addAnswer(new Answer(user, question, "answer"));
        questionRepository.save(question);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        Answer answer = answers.get(0);
        answer.setDeleted(true);

        List<Answer> expectedAnswers = answerRepository.findByDeletedFalse();

        // then
        assertThat(expectedAnswers.size() == 1).isTrue();
    }

    @Test
    void 여러건을_저장할때_동일한_객체라도_모두_저장된다() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        Question question = new Question("title", "content").writeBy(user);
        question.addAnswer(new Answer(user, question, "answer"));
        question.addAnswer(new Answer(user, question, "answer"));
        question.addAnswer(new Answer(user, question, "answer"));
        questionRepository.save(question);


        // when
        List<Answer> findAnswer = answerRepository.findAll();

        // then
        assertThat(findAnswer.size()).isEqualTo(3);
    }

}
