package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(QuestionTest.QUESTION1.getId()).isNull();
        Question actualQuestion1 = questionRepository.save(QuestionTest.QUESTION1);
        assertThat(actualQuestion1.getId()).isNotNull();
        assertThat(actualQuestion1.getTitle()).isEqualTo(QuestionTest.QUESTION1.getTitle());
        assertThat(actualQuestion1.getContents()).isEqualTo(QuestionTest.QUESTION1.getContents());
        assertThat(actualQuestion1.getCreatedAt()).isNotNull();
        assertThat(actualQuestion1.getUpdatedAt()).isNotNull();

        // 리스트 테스트
        Question actualQuestion2 = questionRepository.save(QuestionTest.QUESTION2);
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList.size()).isEqualTo(2);
        assertThat(questionList).containsExactly(actualQuestion1, actualQuestion2);
    }

    @Test
    @DisplayName("삭제되지 않은 질문 검색 테스트")
    void findByDeletedFalseTest() {
        questionRepository.save(QuestionTest.QUESTION1);
        questionRepository.save(QuestionTest.QUESTION2);
        List<Question> expectedList = questionRepository.findAll();
        List<Question> actualList = questionRepository.findByDeletedFalse();

        //findByDeletedFalse test
        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList).isEqualTo(expectedList);

        //deleted false test
        for(Question question : actualList) {
            assertThat(question.isDeleted()).isEqualTo(false);
        }
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalseTest() {
        Question expected = questionRepository.save(QuestionTest.QUESTION1);
        Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
                                            .orElseThrow(IllegalArgumentException::new);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.isDeleted()).isFalse();
    }
}
