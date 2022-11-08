package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void beforeEach() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @Test
    void findById() {
        Question question1 = questionRepository.findById(1L).get();
        Question question2 = questionRepository.findById(2L).get();
        assertThat(question1).isNotNull();
        assertThat(question2).isNotNull();
        assertThat(question1.getTitle()).isEqualTo(Q1.getTitle());
        assertThat(question2.getTitle()).isEqualTo(Q2.getTitle());
        assertThat(question1.getContents()).isEqualTo(Q1.getContents());
        assertThat(question2.getContents()).isEqualTo(Q2.getContents());
        assertThat(question1.getWriterId()).isEqualTo(Q1.getWriterId());
        assertThat(question2.getWriterId()).isEqualTo(Q2.getWriterId());
    }

    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).isNotEmpty();
        assertThat(questions.get(0).getTitle()).isEqualTo(Q1.getTitle());
        assertThat(questions.get(1).getTitle()).isEqualTo(Q2.getTitle());
        assertThat(questions.get(0).getContents()).isEqualTo(Q1.getContents());
        assertThat(questions.get(1).getContents()).isEqualTo(Q2.getContents());
        assertThat(questions.get(0).getWriterId()).isEqualTo(Q1.getWriterId());
        assertThat(questions.get(1).getWriterId()).isEqualTo(Q2.getWriterId());
    }

    @Test
    void findByIdAndDeletedFalse() {
        Question question1 = questionRepository.findByIdAndDeletedFalse(1L).get();
        Question question2 = questionRepository.findByIdAndDeletedFalse(2L).get();
        assertThat(question1).isNotNull();
        assertThat(question2).isNotNull();
        assertThat(question1.getTitle()).isEqualTo(Q1.getTitle());
        assertThat(question2.getTitle()).isEqualTo(Q2.getTitle());
        assertThat(question1.getContents()).isEqualTo(Q1.getContents());
        assertThat(question2.getContents()).isEqualTo(Q2.getContents());
        assertThat(question1.getWriterId()).isEqualTo(Q1.getWriterId());
        assertThat(question2.getWriterId()).isEqualTo(Q2.getWriterId());
    }
}
