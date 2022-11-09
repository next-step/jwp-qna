package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
public class QuestionTest {
    private static final String TITLE_1 = "title1";
    private static final String TITLE_2 = "title2";

    private static final String CONTENTS_1 = "contents1";
    private static final String CONTENTS_2 = "contents2";

    public static final Question Q1 = new Question(TITLE_1, CONTENTS_1).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(TITLE_2, CONTENTS_2).writeBy(UserTest.SANJIGI);


    @Autowired
    QuestionRepository questions;

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("questionTestFixture")
    void save_테스트(Question question) {
        Question saved = questions.save(question);
        assertThat(saved).isEqualTo(question);
        assertThat(saved.getContents()).isEqualTo(question.getContents());
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest(name = "save_후_findById_테스트")
    @MethodSource("questionTestFixture")
    void save_후_findById_테스트(Question question) {
        Question question1 = questions.save(question);
        Question question2 = questions.findById(question1.getId()).get();
        assertThat(question1).isEqualTo(question2);
        assertThat(question1.getContents()).isEqualTo(question2.getContents());
    }

    static Stream<Question> questionTestFixture() {
        return Stream.of(Q1, Q2);
    }
}
