package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void save() {
        final String title = questionRepository.save(Q1).getTitle();
        assertThat(title).isEqualTo("title1");
    }

    @Test
    void findById() {
        final Question question = questionRepository.findById(questionRepository.save(Q1).getId())
                .orElseThrow(NotFoundException::new);
        assertThat(question.getTitle()).isEqualTo("title1");
    }

    @Test
    void findByDeletedFalse() {
        final Question question = questionRepository.save(Q1);
        question.setDeleted(true);
        assertThat(questionRepository.findByDeletedFalse()).hasSize(0);
    }

    @Test
    void findByIdAndDeletedFalse() {
        final Question question = questionRepository.save(Q1);
        question.setDeleted(true);
        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }
}
