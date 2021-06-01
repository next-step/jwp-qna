package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1")
        .writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Question 저장 확인")
    void saveTest() {
        Question question1 = questionRepository.save(Q1);

        assertThat(question1.getId()).isNotNull();
        assertThat(question1.getWriterId()).isEqualTo(Q1.getWriterId());
        assertThat(question1.getContents()).isEqualTo(Q1.getContents());
        assertThat(question1.getTitle()).isEqualTo(Q1.getTitle());
    }

    @Test
    @DisplayName("deleted false 찾기 테스트")
    void findByDeletedFalseTest() {
        Question question1 = questionRepository.save(Q1);
        Question question2 = questionRepository.save(Q2);

        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("deleted false 찾기 테스트")
    void findByIdAndDeletedFalseTest() {
        Question question1 = questionRepository.save(Q1);

        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();

        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getContents()).isEqualTo(question1.getContents());
        assertThat(result.getTitle()).isEqualTo(question1.getTitle());
        assertThat(result.getWriterId()).isEqualTo(question1.getWriterId());
    }
}
