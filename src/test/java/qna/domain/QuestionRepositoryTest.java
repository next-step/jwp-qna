package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private Question question1;
    private Question question2;
    private User questionWriter1;
    private User questionWriter2;

    @BeforeEach
    void setUp() {
        questionWriter1 = new User("qwriter1", "password", "name", "sunju@slipp.net");
        question1 = new Question("title3", "contents2").writeBy(questionWriter1);
        users.save(questionWriter1);

        questionWriter2 = new User("qwriter2", "password", "name", "jung@slipp.net");
        question2 = new Question("title3", "contents2").writeBy(questionWriter2);
        users.save(questionWriter2);
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(question1.getId()).isNull();
        Question savedQuestion = questions.save(question1);
        questions.flush();
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getTitle()).isEqualTo(question1.getTitle());
        assertThat(savedQuestion.getContents()).isEqualTo(question1.getContents());
        assertThat(savedQuestion.getCreatedAt()).isNotNull();
        assertThat(savedQuestion.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Question 여러개 save 테스트")
    void saveMultipleQuestionTest() {
        Question savedQuestion1 = questions.save(question1);
        Question savedQuestion2 = questions.save(question2);
        questions.flush();
        List<Question> questionList = questions.findAll();
        assertThat(questionList.size()).isEqualTo(2);
        assertThat(questionList).containsExactly(savedQuestion1, savedQuestion2);
    }



    @Test
    @DisplayName("삭제되지 않은 질문 검색 테스트")
    void findByDeletedFalseTest() {
        Question savedQuestion1 = questions.save(question1);
        Question savedQuestion2 = questions.save(question2);
        questions.flush();
        List<Question> actualList = questions.findByDeletedFalse();

        //findByDeletedFalse test
        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList).contains(savedQuestion1, savedQuestion2);

        //deleted false test
        for(Question question : actualList) {
            assertThat(question.isDeleted()).isEqualTo(false);
        }
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalseTest() {
        Question savedQuestion = questions.save(question1);
        questions.flush();
        Question actual = questions.findByIdAndDeletedFalse(savedQuestion.getId())
                                            .orElseThrow(IllegalArgumentException::new);

        assertThat(actual).isEqualTo(savedQuestion);
        assertThat(actual.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("작성자 확인 테스트")
    void isWrittenByTest() {
        Question savedQuestion = questions.save(question1);
        assertThat(savedQuestion.isWrittenBy(questionWriter1));
    }

    @Test
    @DisplayName("답변 삭제 테스트")
    void deleteAnswerTest() {
        Question savedQuestion = questions.save(question1);
        questions.save(question2);
        questions.flush();
        List<Question> beforeDeleteList = questions.deletedFalse();
        assertThat(beforeDeleteList.size()).isEqualTo(2);

        savedQuestion.delete();
        questions.flush();
        List<Question> afterDeleteList = questions.deletedFalse();
        assertThat(afterDeleteList.size()).isEqualTo(1);
    }
}
