package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @BeforeEach
    void setUp() {
        answers.save(AnswerTest.A1);
    }

    @Test
    public void save() {
         Answer actual = answers.save(AnswerTest.A2);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    public void findByContents() {
        Answer actual = answers.findByContents("Answers Contents1").get();
        assertThat(actual.getContents()).isEqualTo("Answers Contents1");
    }
}