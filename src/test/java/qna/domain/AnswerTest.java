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
public class AnswerTest {
    private static final String ANSWERS_CONTENTS_1 = "Answers Contents1";
    private static final String ANSWERS_CONTENTS_2 = "Answers Contents2";

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, ANSWERS_CONTENTS_1);
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, ANSWERS_CONTENTS_2);

    @Autowired
    AnswerRepository answers;

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("answerTestFixture")
    void save_테스트(Answer answer) {
        Answer saved = answers.save(answer);
        assertThat(saved).isEqualTo(answer);
        assertThat(saved.getContents()).isEqualTo(answer.getContents());
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest(name = "save_후_findById_테스트")
    @MethodSource("answerTestFixture")
    void save_후_findById_테스트(Answer answer) {
        Answer answer1 = answers.save(answer);
        Answer answer2 = answers.findById(answer1.getId()).get();
        assertThat(answer1).isEqualTo(answer2);
        assertThat(answer1.getContents()).isEqualTo(answer2.getContents());
    }

    @ParameterizedTest(name = "save_후_update_테스트")
    @MethodSource("answerTestFixture")
    void save_후_update_테스트(Answer answer) {
        Answer answer1 = answers.save(answer);
        String original = answer1.getContents();
        answer1.setContents("허억!!");
        Answer answer2 = answers.findById(answer1.getId()).get();
        assertThat(answer1).isEqualTo(answer2);
        assertThat(original).isNotEqualTo(answer2.getContents());
        answers.flush();
    }

    static Stream<Answer> answerTestFixture() {
        return Stream.of(A1, A2);
    }
}
