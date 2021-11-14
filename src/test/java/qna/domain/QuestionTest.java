package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        Question save = questionRepository.save(Q1);
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        Question save = questionRepository.save(Q1);
        Question found = questionRepository.findById(save.getId()).orElse(null);
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        Question save = questionRepository.save(Q1);
        save.setContents("update!!");
        Question found = questionRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("update!!");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        Question save = questionRepository.save(Q1);
        questionRepository.delete(save);
        questionRepository.flush();
        Question found = questionRepository.findById(save.getId()).orElse(null);
        assertThat(found).isNull();
    }
}
