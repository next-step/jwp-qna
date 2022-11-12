package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
@EnableJpaAuditing
public class AnswerTest {
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

    User writer;
    Question question;
    Answer answer;

    @BeforeEach
    void setUp() {
        writer = users.save(new User("userId", "password", "name", "email"));
        question = questions.save(new Question("Hello", "Why??"));
        answer = answers.save(new Answer(writer, question, "What?!"));
    }

    @Test
    void save_테스트() {
        assertThat(answer.getId()).isNotNull();
        assertThat(answer.getCreatedAt()).isNotNull();
        assertThat(answer.getContents()).isEqualTo("What?!");
    }

    @Test
    void save_후_findById_테스트() {
        Optional<Answer> maybe = answers.findById(answer.getId());
        assertThat(maybe.isPresent()).isTrue();
        assertThat(maybe.get()).isEqualTo(answer);
    }

    @Test
    void save_후_update_테스트() {
        Answer modifiedAnswer = answers.findById(answer.getId()).get();
        String contents = modifiedAnswer.getContents();
        modifiedAnswer.setContents("허억!!");
        Answer checkAnswer = answers.findById(answer.getId()).get();
        assertThat(contents).isNotEqualTo(checkAnswer.getContents());
    }

    @Test
    void save_후_delete_테스트() {
        answers.delete(answer);
        Optional<Answer> maybe = answers.findById(answer.getId());
        assertThat(maybe.isPresent()).isFalse();
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
