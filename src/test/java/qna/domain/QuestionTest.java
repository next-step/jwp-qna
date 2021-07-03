package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
public class QuestionTest {

    @Autowired
    QuestionRepository questions;
    @Autowired
    UserRepository users;

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void save() {
        users.save(UserTest.JAVAJIGI);
        Question actual = questions.save(Q1);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo("title1");
        assertThat(actual.getContents()).isEqualTo("contents1");
    }
}
