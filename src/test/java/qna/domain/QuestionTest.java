package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.config.JpaAuditingConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.ANSWER;
import static qna.domain.ContentType.QUESTION;
import static qna.domain.Question.CANT_DELETE_OTHER_PERSON;
import static qna.domain.Question.CANT_DELETE_QUESTION;
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
    List<Answer> answerEntityList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        writer = users.save(new User("userId", "password", "name", "email"));
        question = questions.save(new Question("제 질문은요", "80점입니다.").writeBy(writer));
        answerEntityList.add(answers.save(new Answer(writer, question, "answer 1")));
        answerEntityList.add(answers.save(new Answer(writer, question, "answer 2")));

        for (Answer answer : answerEntityList) {
            question.addAnswer(answer);
        }
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
        Question checkQuestion = questions.findById(modifiedQuestion.getId()).get();
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
        question.delete();
        Answer additionalAnswer = answers.save(new Answer(writer, question, "추가 답변드립니다."));
        Assertions.assertThatThrownBy(() -> question.addAnswer(additionalAnswer))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 삭제전_질문과_답변_작성자가_일치하는지_검증한다() {
        assertThatNoException()
            .isThrownBy(() -> question.validateDelete(writer));

        User other = users.save(new User("crawal", "password", "name", "esesmail"));
        assertThatThrownBy(() -> question.validateDelete(other))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage(CANT_DELETE_QUESTION);


        Answer otherAnswer = answers.save(new Answer(other, question, "Other Answers"));
        question.addAnswer(otherAnswer);
        assertThatThrownBy(() -> question.validateDelete(writer))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage(CANT_DELETE_OTHER_PERSON);
    }

    @Test
    void 질문과_답변을_모두_삭제한다() {
        List<DeleteHistory> deleteHistories = question.delete();
        assertThat(deleteHistories).hasSize(3);
        assertThat(deleteHistories).isEqualTo(Arrays.asList(
            new DeleteHistory(QUESTION, question.getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ANSWER, answerEntityList.get(0).getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ANSWER, answerEntityList.get(1).getId(), writer, LocalDateTime.now())
        ));
    }
}
