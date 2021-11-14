package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save 테스트")
    @Test
    void save() {
        // when
        Question newQuestion = questionRepository.save(Q1);

        // then
        assertAll(
                () -> assertThat(newQuestion.getId()).isNotNull()
                , () -> assertThat(newQuestion.getTitle()).isEqualTo(Q1.getTitle())
                , () -> assertThat(newQuestion.getContents()).isEqualTo(Q1.getContents())
                , () -> assertThat(newQuestion.getWriterId()).isEqualTo(Q1.getWriterId())
        );
    }

    @DisplayName("findByDeletedFalse 테스트")
    @Test
    void findByDeletedFalse() {
        // when
        Question newQuestion = questionRepository.save(Q1);
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertAll(
                () -> assertThat(questions).hasSize(1)
                , () -> assertThat(questions).containsExactly(newQuestion)
        );
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        // when
        Question newQuestion = questionRepository.save(Q1);
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(1L);

        // then
        assertAll(
                () -> assertThat(optionalQuestion.get()).isNotNull()
                , () -> assertThat(optionalQuestion.get()).isEqualTo(newQuestion)
        );
    }
}
