package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

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
        Question savedQuestion = questionRepository.save(Q1);

        assertAll(
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(Q1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents()),
            () -> assertThat(savedQuestion.getWriterId()).isEqualTo(Q1.getWriterId()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(Q1.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        Long savedId = questionRepository.save(Q1).getId();
        Question savedQuestion = questionRepository.findByIdAndDeletedFalse(savedId).get();

        assertAll(
            () -> assertThat(savedQuestion.getId()).isEqualTo(savedId),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(Q1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents()),
            () -> assertThat(savedQuestion.getWriterId()).isEqualTo(Q1.getWriterId()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(Q1.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
