package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        Question actual = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @Test
    @DisplayName("질문 수정 : 질문 제목, 질문 내용")
    void update() {
        Question question = questionRepository.save(Q1);
        question.setTitle("첫번째 질문");
        question.setContents("첫번째 질문 내용입니다.");
        Question actual = questionRepository.save(question);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo("첫번째 질문"),
                () -> assertThat(actual.getContents()).isEqualTo("첫번째 질문 내용입니다.")
        );
    }
}
