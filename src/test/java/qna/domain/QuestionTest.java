package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.NotFoundException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;

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

    @Autowired
    AnswerRepository answers;

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

    @ParameterizedTest(name = "save_후_update_테스트")
    @MethodSource("questionAndAnswerTestFixture")
    void save_후_update_테스트(Question question, Answer answer) {
        Question newQuestion = questions.save(question);
        newQuestion.addAnswer(answer);
        Answer linkAnswer = answers.save(answer);
        assertThat(linkAnswer.getQuestionId()).isEqualTo(newQuestion.getId());
    }

    @ParameterizedTest(name = "save_후_delete_테스트")
    @MethodSource("questionTestFixture")
    void save_후_delete_테스트(Question question) {
        Question newQuestion = questions.save(question);
        questions.delete(newQuestion);
        Optional<Question> questionsById = questions.findById(question.getId());
        assertThat(questionsById.isPresent()).isFalse();
    }

    @Test
    void 삭제된_질문에_답변을_달수_없다() {
        Question newQuestion = questions.save(Q1);
        newQuestion.setDeleted(true);
        Question question = questions.save(newQuestion);
        Assertions.assertThatThrownBy(() -> question.addAnswer(A1))
            .isInstanceOf(NotFoundException.class);
    }

    static Stream<Question> questionTestFixture() {
        return Stream.of(Q1, Q2);
    }

    static Stream<Arguments> questionAndAnswerTestFixture() {
        return Stream.of(
            Arguments.of(
                Q1, new Answer(new User("userId", "password", "name", "email"),
                    new Question("title", "contents"), "contents")
            ),
            Arguments.of(
                Q2, new Answer(new User("userId", "password", "name", "email"),
                    new Question("title", "contents"), "contents")
            )
        );
    }
}
