package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private Question saveQ1;

    @BeforeAll
    void setUp() {
        saveQ1 = questionRepository.save(Q1);
    }

    @Test
    @DisplayName("Question save 테스트")
    @Order(1)
    void save() {
        Question actual = questionRepository.save(Q1);
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("Question findByIdAndDeletedFalse 테스트")
    @Order(2)
    void select_findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(saveQ1.getId());
        assertThat(actual.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Question findByDeletedFalse 테스트")
    @Order(2)
    void select_findByDeletedFalse() {
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual.get(0).getId()).isEqualTo(saveQ1.getId());
    }

    @Test
    @DisplayName("writerId update 테스트")
    void update_writeBy() {
        Question actual = saveQ1.writeBy(UserTest.JAVAJIGI);
        questionRepository.flush();
        assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @Test
    @DisplayName("addAnswer 테스트")
    void addAnswer() {
        saveQ1.addAnswer(AnswerTest.A1);
        assertThat(AnswerTest.A1.getWriterId()).isEqualTo(saveQ1.getId());
    }

    @Test
    @DisplayName("Question delete 테스트")
    @Order(Integer.MAX_VALUE)
    void delete() {
        questionRepository.delete(saveQ1);
        Optional<Question> actual = questionRepository.findById(saveQ1.getId());
        assertThat(actual.isPresent()).isFalse();
    }

}
