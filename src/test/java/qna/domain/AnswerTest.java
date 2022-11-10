package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.config.TruncateConfig;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
@EnableJpaAuditing
public class AnswerTest extends TruncateConfig {
    private static final String ANSWERS_CONTENTS_1 = "Answers Contents1";
    private static final String ANSWERS_CONTENTS_2 = "Answers Contents2";

    public static final Answer A1 = new Answer(JAVAJIGI, Q1, ANSWERS_CONTENTS_1);
    public static final Answer A2 = new Answer(SANJIGI, Q1, ANSWERS_CONTENTS_2);

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @BeforeEach
    void setUp() {
        users.save(JAVAJIGI);
        users.save(SANJIGI);
        questions.save(Q1);
    }

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("answerTestFixture")
    void save_테스트(Answer answer) {
        Answer saved = answers.save(answer);
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
    }

    @ParameterizedTest(name = "save_후_delete_테스트")
    @MethodSource("answerTestFixture")
    void save_후_delete_테스트(Answer answer) {
        Answer saved = answers.save(answer);
        answers.delete(saved);
        Optional<Answer> answersById = answers.findById(answer.getId());
        assertThat(answersById.isPresent()).isFalse();
    }

    @Test
    void 작성자가_없이_답변을_남길_수_없다() {
        assertThatThrownBy(() -> new Answer(null, new Question("Hello", "MyNameIs"), "Jun"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문없이_답변을_남길_수_없다() {
        assertThatThrownBy(() ->
            new Answer(new User("victor-jo", "password", "victor", "mail"), null, "Jun"))
            .isInstanceOf(NotFoundException.class);
    }

    static Stream<Answer> answerTestFixture() {
        return Stream.of(A1, A2);
    }
}
