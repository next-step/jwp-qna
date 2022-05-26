package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = { @ComponentScan.Filter(value = { EnableJpaAuditing.class }) })
public class AnswerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private User JAVAJIGI;
    private Question Q1;

    @BeforeEach
    void setUp() {
        JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);

        userRepository.save(JAVAJIGI);
        questionRepository.save(Q1);
    }
    @Test
    @DisplayName("Answer를_저장하게_되면_발급된_Id_를_확인할_수_있다")
    void save() {
        final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        assertThat(A1.getId()).isNull();

        answerRepository.save(A1);

        assertThat(A1.getId()).isNotNull();
    }
}
