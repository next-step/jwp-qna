package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Answer savedAnswer;
    private Question savedQuestion;
    private User savedUser;


    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(new User( "javajigi", "password", "name", "javajigi@slipp.net"));
        savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(savedUser));
        savedAnswer = answerRepository.save(new Answer(savedUser, savedQuestion, "Answers Contents1"));
    }

    @DisplayName("저장")
    @Test
    public void save(){
        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(savedAnswer.getWriter()).isEqualTo(savedUser);
        assertThat(savedAnswer.getQuestion()).isEqualTo(savedQuestion);
        assertThat(savedAnswer.getWriter()).isEqualTo(savedUser);
        assertThat(savedAnswer.getContents()).isEqualTo("Answers Contents1");
    }

    @DisplayName("Id로 삭제되지 않은 answer 검색")
    @Test
    public void findByIdAndDeletedFalse(){
        Answer actual = answerRepository.findByIdAndDeletedFalse(1L).get();
        assertThat(actual).isEqualTo(savedAnswer);
    }

    @DisplayName("questionId로 삭제되지 않은 answer 검색")
    @Test
    public void findByQuestionIdAndDeletedFalse(){
        Answer actual = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId()).get(0);
        assertThat(actual).isEqualTo(savedAnswer);
    }

    @DisplayName("삭제 테스트")
    @Test
    public void delete(){
        Answer actual = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId()).get(0);
        assertThat(actual).isEqualTo(savedAnswer);
        actual.deleted(true);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId());
        assertTrue(answerList.isEmpty());
    }




}
