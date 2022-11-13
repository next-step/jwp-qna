package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @Test
    void 저장_및_조회() {
        Question question1 = questionRepository.save(Q1);
        Question question2 = questionRepository.save(Q2);

        Question retrievedQuestion1 = questionRepository.findById(question1.getId()).get();
        Question retrievedQuestion2 = questionRepository.findById(question2.getId()).get();

        assertAll(
                () -> assertThat(retrievedQuestion1.getId()).isEqualTo(Q1.getId()),
                () -> assertThat(retrievedQuestion2.getId()).isEqualTo(Q2.getId())
        );
    }
}
