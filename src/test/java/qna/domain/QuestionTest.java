package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 저장_테스트() {
        Question question = Q1;
        questionRepository.save(question);
        Question savedQuestion = questionRepository.getOne(question.getId());
        assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle());
    }

    @Test
    void 답변_달기() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Question question = questionRepository.save(Q1);

        question.addAnswer(answer);
        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        assertThat(findAnswer.getQuestionId()).isEqualTo(question.getId());
    }

}
