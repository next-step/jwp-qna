package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Test
    void save_질문생성() {
        Question actual = questions.save(Q1);
        Question expected = questions.findById(actual.getId()).orElse(null);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void add_답변생성() {
        Question actual = questions.save(Q1);
        User writer = new User("lsh", "lsh", "lsh", "lsh@gmail.com");
        Answer answer = new Answer(writer, actual, "test");
        actual.addAnswer(answer);
        assertThat(answer.getQuestionId()).isEqualTo(actual.getId());
    }

    @Test
    void findByIdAndDeletedFalse_조회() {
        Question actual = questions.save(Q1);
        Question excepted = questions.findByIdAndDeletedFalse(actual.getId()).orElse(null);
        assertThat(excepted).isEqualTo(actual);
    }

    @Test
    void findByIdAndDeletedFalse_삭제_조회() {
        Question actual = questions.save(Q1);
        actual.setDeleted(true);
        Question excepted = questions.findByIdAndDeletedFalse(actual.getId()).orElse(null);
        assertThat(excepted).isNull();
    }
}
