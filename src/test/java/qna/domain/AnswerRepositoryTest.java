package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

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
        answerRepository.save(generateAnswer(true));
        Answer answerDeletedFalse = answerRepository.save(generateAnswer(false));
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(answers).containsExactly(answerDeletedFalse);
    }

    @ParameterizedTest(name = "삭제되지 않은 Answer 조회: deleted = {0}, 조회값 존재 = {1}")
    @DisplayName("Answer 조회: by Id, DeletedFalse")
    @CsvSource(value = {"true:false", "false:true"}, delimiter = ':')
    void Answer_조회_by_Id_DeletedFalse(boolean deleted, boolean resultPresent){
        Answer answer = answerRepository.save(generateAnswer(deleted));
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).isPresent()).isEqualTo(resultPresent);
    }

    private Answer generateAnswer(boolean deleted) {
        Answer answer = new Answer(user, question, "Answers Contents1");
        if(deleted){
            deleteAnswer(answer, user);
        }
        return answer;
    }

    private void deleteAnswer(Answer answer, User user) {
        try {
            answer.delete(user);
        } catch (CannotDeleteException e) {
            e.printStackTrace();
        }
    }
}
