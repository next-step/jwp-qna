package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("question save 확인")
    void save() {
        Question question = Q1;
        Question result = questionRepository.save(Q1);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(result.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(result.getId()).isEqualTo(question.getId())
        );
    }

    @Test
    @DisplayName("deleted 값이 false인 Question 조회")
    void findByDeletedFalse() {
        Question question1 = questionRepository.save(Q1);
        Question question2 = questionRepository.save(Q2);

        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("id로 deleted값이 false인 Question 조회")
    void findByIdAndDeletedFalse() {
        Question question1 = questionRepository.save(Q1);

        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();
        assertAll(
                () -> assertThat(result.isDeleted()).isFalse(),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(result.getWriterId()).isEqualTo(question1.getWriterId())
        );
    }
}
