package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @BeforeEach
    void setUp() {
        Question question = new Question("타이틀", "콘텐츠");

        questions.save(question);
    }

    @Test
    @DisplayName("question테이블 save 테스트")
    void save() {
        Question expected = new Question("타이틀2", "콘텐츠2");
        Question actual = questions.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("question테이블 select 테스트")
    void findById() {
        Question expected = questions.findByTitle("타이틀").get();
        Question actual = new Question("타이틀", "콘텐츠");

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("question테이블 update 테스트")
    void updateDeletedById() {
        Question expected = questions.findByTitle("타이틀")
                .get();
        expected.setTitle("바꾼타이틀");
        Question actual = questions.findByTitle("바꾼타이틀")
                .get();

        assertThat(actual.getTitle()).isEqualTo("바꾼타이틀");
    }

    @Test
    @DisplayName("question테이블 delete 테스트")
    void delete() {
        Question expected = questions.findByTitle("타이틀")
                .get();
        questions.delete(expected);

        assertThat(questions.findByTitle("타이틀").isPresent()).isFalse();
    }

}