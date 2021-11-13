package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;


    @Test
    @DisplayName("Question Entity Create 및 ID 생성 테스트")
    void save() {
        final Question question = questionRepository.save(Q1);
        assertThat(question.getId()).isNotNull();
    }

    @Test
    @DisplayName("Question Entity Read 테스트")
    void findById() {
        final Question saved = questionRepository.save(Q1);
        final Question found = questionRepository.findById(saved.getId()).orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("Question Entity Update 테스트")
    void update() {
        final Question saved = questionRepository.save(Q1);
        saved.setContents("updated!");
        final Question found = questionRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("updated!");
    }

    @Test
    @DisplayName("Question Entity Delete 테스트")
    void delete() {
        final Question saved = questionRepository.save(Q1);
        questionRepository.delete(saved);
        questionRepository.flush();
        final Question found = questionRepository.findById(saved.getId()).orElseGet(() -> null);
        assertThat(found).isNull();
    }
}
