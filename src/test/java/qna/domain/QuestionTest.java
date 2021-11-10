package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository question;

    @Test
    @DisplayName("저장됐는지 확인")
    void 저장() {
        Question expected = new Question("new title", "new contents").writeBy(UserTest.JAVAJIGI);
        Question saveQuestion = question.save(expected);
        assertAll(() -> assertThat(saveQuestion).isEqualTo(expected), 
                () -> assertThat(saveQuestion.getId()).isNotNull(), 
                () -> assertThat(saveQuestion.getId()).isEqualTo(expected.getId()));
    }

    @Test
    @DisplayName("아이디로 조회")
    void 아이디로_조회() {
        Question expected = new Question("new title", "new contents").writeBy(UserTest.JAVAJIGI);
        question.save(expected);
        Optional<Question> actual = question.findById(expected.getId());
        assertAll(() -> assertThat(actual.isPresent()).isTrue(), 
                () -> assertThat(actual.orElse(null)).isEqualTo(expected));
    }

    @Test
    @DisplayName("삭제됐는지 확인")
    void 삭제() {
        Question expected = new Question("new title", "new contents").writeBy(UserTest.JAVAJIGI);
        question.save(expected);
        question.delete(expected);
        assertThat(question.findById(expected.getId()).isPresent()).isFalse();
    }

}
