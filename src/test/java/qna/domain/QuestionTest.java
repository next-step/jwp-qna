package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.NotFoundException;
import qna.config.JpaAuditingConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
@Import(JpaAuditingConfiguration.class)
public class QuestionTest {
    private static final String TITLE_1 = "title1";
    private static final String TITLE_2 = "title2";

    private static final String CONTENTS_1 = "contents1";
    private static final String CONTENTS_2 = "contents2";

    public static final Question Q1 = new Question(TITLE_1, CONTENTS_1).writeBy(JAVAJIGI);
    public static final Question Q2 = new Question(TITLE_2, CONTENTS_2).writeBy(SANJIGI);


    @Autowired
    QuestionRepository questions;

    @Autowired
    AnswerRepository answers;

    @Autowired
    UserRepository users;

    Question question;
    User writer;
    Answer answer;


    @BeforeEach
    void setUp() {
        writer = users.save(new User("userId", "password", "name", "email"));
        question = questions.save(new Question("제 질문은요", "80점입니다."));
        answer = answers.save(new Answer(writer, question, "What?!"));
    }

    @Test
    void save_테스트() {
        assertThat(question.getId()).isNotNull();
        assertThat(question.getContents()).isEqualTo("80점입니다.");
        assertThat(question.getCreatedAt()).isNotNull();
    }

    @Test
    void save_후_findById_테스트() {
        Optional<Question> maybe = questions.findById(question.getId());
        assertThat(maybe.isPresent()).isTrue();
        assertThat(maybe.get()).isEqualTo(question);
    }

    @Test
    void save_후_update_테스트() {
        Question modifiedQuestion = questions.findById(question.getId()).get();
        String contents = modifiedQuestion.getContents();
        modifiedQuestion.setContents("90점입니다.");
        Answer checkQuestion = answers.findById(modifiedQuestion.getId()).get();
        assertThat(contents).isNotEqualTo(checkQuestion.getContents());
    }

    @Test
    void save_후_delete_테스트() {
        questions.delete(question);
        Optional<Question> maybe = questions.findById(question.getId());
        assertThat(maybe.isPresent()).isFalse();
    }

    @Test
    void 삭제된_질문에_답변을_달수_없다() {
        question.setDeleted(true);
        Assertions.assertThatThrownBy(() -> question.addAnswer(new Answer(writer, question, "추가 답변드립니다.")))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 질문에서_참조를_통해_답변을_가져올때_삭제된_답변은_가져오지_않는다() {
        question.addAnswer(answer);
        assertThat(question.getAnswersCount()).isEqualTo(1);
        question.deleteAnswer(answer);
        assertThat(question.getAnswersCount()).isEqualTo(0);
    }
}
