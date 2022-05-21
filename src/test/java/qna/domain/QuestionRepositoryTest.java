package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    private Question question;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        question = questionRepository.save(new Question("질문제목", "질문내용"));
    }

    @Test
    void save() {
        assertThat(question).isNotNull();
    }

    @Test
    void select() {
        Question question2 = questionRepository.findByIdAndDeletedFalse(1L).get();
        assertThat(question).isSameAs(question2);
    }
}
