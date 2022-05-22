package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

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
    @DisplayName("Answer 조회: by QuestionId, DeletedFalse")
    void Answer_조회_by_QuestionId_DeletedFalse(){
        generateAnswerDeletedTrue();
        Answer answerDeletedFalse = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(answers).containsExactly(answerDeletedFalse);
    }

    @Test
    @DisplayName("Answer 조회: by Id, DeletedFalse")
    void Answer_조회_by_Id_DeletedFalse(){
        Answer answerDeletedTrue = generateAnswerDeletedTrue();
        Answer answerDeletedFalse = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        assertAll(
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
