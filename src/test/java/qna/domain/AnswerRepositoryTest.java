package qna.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired AnswerRepository answerRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired UserRepository userRepository;

    private Question question;
    private User user;

    @BeforeEach
    void initialize(){
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    @DisplayName("Answer 저장")
    void save(){
        Answer saved = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer를 QuestionId, DeletedFalse로 조회")
    void Answer_조회_byQuestionId_DeletedFalse(){
        generateAnswerDeletedTrue();
        Answer answerDeletedFalse = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(answers).containsExactly(answerDeletedFalse);
    }

    @Test
    @DisplayName("Answer를 Id, DeletedFalse로 조회")
    void Answer_조회_byId_DeletedFalse(){
        Answer answerDeletedTrue = generateAnswerDeletedTrue();
        Answer answerDeletedFalse = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        Assertions.assertAll(
                () -> assertThat(answerRepository.findByIdAndDeletedFalse(answerDeletedTrue.getId()))
                        .isEmpty(),
                () -> assertThat(answerRepository.findByIdAndDeletedFalse(answerDeletedFalse.getId())).get()
                        .isEqualTo(answerDeletedFalse)
        );
    }

    private Answer generateAnswerDeletedTrue() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        answer.setDeleted(true);
        return answerRepository.save(answer);
    }
}
