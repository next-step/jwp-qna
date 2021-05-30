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

    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        Answer expect = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        savedAnswer = answerRepository.save(expect);
    }

    @DisplayName("저장")
    @Test
    public void save(){
        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(savedAnswer.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
        assertThat(savedAnswer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId());
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
        Answer actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()).get(0);
        assertThat(actual).isEqualTo(savedAnswer);
    }

    @DisplayName("삭제 테스트")
    @Test
    public void delete(){
        Answer actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()).get(0);
        assertThat(actual).isEqualTo(savedAnswer);
        actual.setDeleted(true);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertTrue(answerList.isEmpty());
    }




}
