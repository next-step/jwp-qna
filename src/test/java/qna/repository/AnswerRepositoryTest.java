package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Answer answer;

    @BeforeEach
    public void setup(){
        User user = userRepository.findByUserId("javajigi").orElse(userRepository.save(UserTest.JAVAJIGI));
        Question question = new Question("Question Title", "Question Contents").writeBy(user);
        Question saveQuestion = questionRepository.save(question);
        Question selectQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId()).orElse(questionRepository.save(question));
        answer = new Answer(user, selectQuestion, "Save Answer Content");
    }

    @Test
    @DisplayName("답변 저장")
    public void saveAnswer() {
        Answer saveAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(saveAnswer.getId()).isNotNull(),
                () -> assertThat(saveAnswer.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("답변 검색")
    public void selectAnswer() {
        Answer saveAnswer = answerRepository.save(answer);
        Answer selectAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId()).orElseThrow(NotFoundException::new);

        assertThat(selectAnswer.equals(saveAnswer)).isTrue();
    }
}
