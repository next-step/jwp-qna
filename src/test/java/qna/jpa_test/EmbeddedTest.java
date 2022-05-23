package qna.jpa_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmbeddedTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 임베디드 타입 학습 테스트
     **/

    @Test
    @DisplayName("임베디드 타입을 여러 엔티티에서 사용하는 경우")
    void embeddedType() {
        UserLogin userLogin = new UserLogin("id", "pwd", "writer@slipp.net");
        User writer = userRepository.save(new User("writer", userLogin));
        Question question = questionRepository.save(new Question("title", "question_contents").writeBy(writer));
        Answer answer = answerRepository.save(new Answer(writer, question, "answer_contents"));

        assertThat(question.getContents()).isNotEqualTo(answer.getContents());
    }

    @Test
    @DisplayName("임베디드 타입을 여러 엔티티에서 공유 참조를 막기 위한 방법 - 입베디드타입을 불변 객체로 설계")
    void embeddedTypeShare() {
        UserLogin userLogin = new UserLogin("id", "pwd", "writer@slipp.net");
        User writer = userRepository.save(new User("writer", userLogin));
        Question question = questionRepository.save(new Question("title1", "contents").writeBy(writer));
        Answer answer = answerRepository.save(new Answer(writer, question, "contents"));

        answer.setContents("contents2");

        assertThat(question.getContents()).isNotEqualTo(answer.getContents());
    }


    @Test
    @DisplayName("cascade 를 설정하고 일급 컬렉션을 임베디드 타입으로 사용하는 경우")
    void embeddedFirstCollection() {
        User newWriter = new User("user", new UserLogin("id", "pwd", "email@slipp.net"));
        Question newQuestion = new Question("question", "question is");
        Answer newAnswer1 = new Answer(newWriter, newQuestion, "answer is 1");
        Answer newAnswer2 = new Answer(newWriter, newQuestion, "answer is 2");

        newQuestion.writeBy(newWriter);
        newQuestion.addAnswer(newAnswer1);
        newQuestion.addAnswer(newAnswer2);

        User writer = userRepository.save(newWriter);
        Question question = questionRepository.save(newQuestion);
        Optional<Question> actual = questionRepository.findById(question.getId());
        List<Answer> answers = actual.get().getAnswers();

        // cascade 를 설정하여서 answers 를 추가로 Repository 에서 조회할 필요가 없이 Question 객체 내부에서 확인 가능
        answers.stream().forEach(
                answer -> assertThat(answer.getWriter()).isEqualTo(writer));
    }

}
