package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.config.TruncateConfig;

import java.util.Optional;

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

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        User javajigi = users.save(JAVAJIGI);
        User sanjigi = users.save(SANJIGI);
        Question question = questions.save(Q1);
        answer1 = answers.save(new Answer(javajigi, question, ANSWERS_CONTENTS_1));
        answer2 = answers.save(new Answer(sanjigi, question, ANSWERS_CONTENTS_2));
    }

    @Test
    void save_테스트() {
        assertThat(answer1.getCreatedAt()).isNotNull();
        assertThat(answer2.getCreatedAt()).isNotNull();
    }

    @Test
    void save_후_findById_테스트() {
        Optional<Answer> maybe1 = answers.findById(answer1.getId());
        Optional<Answer> maybe2 = answers.findById(answer2.getId());
        assertThat(maybe1.isPresent()).isTrue();
        assertThat(maybe2.isPresent()).isTrue();
    }

    @Test
    void save_후_update_테스트() {
        String contents = answer1.getContents();
        answer1.setContents("허억!!");
        Answer answer2 = answers.findById(answer1.getId()).get();
        assertThat(contents).isNotEqualTo(answer2.getContents());
    }

    @Test
    void save_후_delete_테스트() {
        answers.delete(answer1);
        answers.delete(answer2);
        Optional<Answer> maybe1 = answers.findById(answer1.getId());
        Optional<Answer> maybe2 = answers.findById(answer2.getId());
        assertThat(maybe1.isPresent()).isFalse();
        assertThat(maybe2.isPresent()).isFalse();
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
}
