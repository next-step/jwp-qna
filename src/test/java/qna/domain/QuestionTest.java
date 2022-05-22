package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("질문에 답변 달기")
    void addAnswer() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Question question = questionRepository.save(Q2);

        question.addAnswer(answer);
        Answer answerFind = answerRepository.findById(answer.getId()).get();
        assertThat(answerFind.getQuestionId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("저장하기")
    void save() {
        Question question = questionRepository.save(Q1);
        Optional<Question> questionOptional = questionRepository.findById(question.getId());

        assertThat(questionOptional.get().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("미삭제 건 전체 조회")
    void notDeleted() {
        Question question1 = questionRepository.save(Q1);
        Question question2 = questionRepository.save(Q2);
        List<Question> deletedFalse = questionRepository.findByDeletedFalse();
        assertThat(deletedFalse).contains(question1, question2);
    }

    @Test
    @DisplayName("미삭제 질문 id로 조회")
    void findByIdAndNotDeleted() {
        Question question1 = questionRepository.save(Q1);
        Question question2 = questionRepository.save(Q2);

        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(question1.getId());
        assertThat(optionalQuestion.get().getId()).isEqualTo(question1.getId());
    }

}
