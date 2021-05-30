package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    private Question savedQuestion;

    @BeforeEach
    void setUp() {
        Question expect = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        savedQuestion = questionRepository.save(expect);
    }

    @DisplayName("저장")
    @Test
    public void save(){
        assertThat(savedQuestion.getTitle()).isEqualTo("title1");
        assertThat(savedQuestion.getContents()).isEqualTo("contents1");
        assertThat(savedQuestion.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @DisplayName("Id로 삭제되지 않은 Question 조회")
    @Test
    public void findByIdAndDeletedFalse(){
        Question actual = questionRepository.findByIdAndDeletedFalse(1L).get();
        assertThat(actual.getTitle()).isEqualTo("title1");
        assertThat(actual.getContents()).isEqualTo("contents1");
        assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @DisplayName("삭제되지 않은 Question 조회")
    @Test
    public void findByDeletedFalse(){
        List<Question> questionList = questionRepository.findByDeletedFalse();
        assertFalse(questionList.isEmpty());
        assertThat(questionList.get(0).getTitle()).isEqualTo("title1");
        assertThat(questionList.get(0).getContents()).isEqualTo("contents1");
        assertThat(questionList.get(0).getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @DisplayName("삭제후 조회 확인")
    @Test
    public void delete(){
        List<Question> questionList = questionRepository.findByDeletedFalse();
        Question actual = questionList.get(0);
        actual.setDeleted(true);
        List<Question> afterDeleteList = questionRepository.findByDeletedFalse();
        assertTrue(afterDeleteList.isEmpty());
    }



}
