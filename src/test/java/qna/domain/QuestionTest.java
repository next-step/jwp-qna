package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class QuestionTest {
    public static final Question QUESTION1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question QUESTION2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(QUESTION1.getId()).isNull();
        Question actualQuestion1 = questionRepository.save(QUESTION1);
        assertThat(actualQuestion1.getId()).isNotNull();
        assertThat(actualQuestion1.getTitle()).isEqualTo(QUESTION1.getTitle());
        assertThat(actualQuestion1.getContents()).isEqualTo(QUESTION1.getContents());
        assertThat(actualQuestion1.getCreatedAt()).isNotNull();
        assertThat(actualQuestion1.getUpdatedAt()).isNotNull();

        // 리스트 테스트
        Question actualQuestion2 = questionRepository.save(QUESTION2);
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList.size()).isEqualTo(2);
        assertThat(questionList).containsExactly(actualQuestion1, actualQuestion2);
    }

    @Test
    @DisplayName("전체 검색 테스트")
    void findByDeletedFalseTest() {
        questionRepository.save(QUESTION1);
        questionRepository.save(QUESTION2);
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
    void findByIdAndDeletedFalse() {
        Question expected = questionRepository.save(QUESTION1);
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

        assertThat(actual.get()).isEqualTo(expected);
        assertThat(actual.get().isDeleted()).isFalse();
    }
}
