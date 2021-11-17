package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    UserRepository users;

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Autowired
    QuestionRepository questions;

    @Autowired
    AnswerRepository answers;

    User USER;
    Question QUESTION;
    Answer ANSWER;
    DeleteHistory ANSWER_HISTORY;
    DeleteHistory QUESTION_HISTORY;

    @BeforeEach
    public void setUp() {
        USER = users.save(UserTest.createUserDataString("answerJavajigi", "password", "javajigi",
            new Email("javajigi@slipp.net")));
        QUESTION = questions.save(new Question("title1", "contents1").writeBy(USER));
        ANSWER = answers.save(new Answer(QUESTION.getWriter(), QUESTION, "Answers Contents1"));

        ANSWER_HISTORY = DeleteHistory.OfQuestion(QUESTION);
        QUESTION_HISTORY = DeleteHistory.OfAnswer(ANSWER);
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 ID not null 체크")
    void save() {
        // when
        DeleteHistory expect = deleteHistories.save(ANSWER_HISTORY);

        // then
        assertThat(expect.getId()).isNotNull();
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        DeleteHistory actual = deleteHistories.save(QUESTION_HISTORY);
        DeleteHistory expect = deleteHistories.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @ParameterizedTest
    @CsvSource(value = {"QUESTION,ANSWER"})
    @DisplayName("nativeQuery 사용하여 ContentType 값 으로 조회")
    void findByContentType(ContentType QUESTION, ContentType ANSWER) {
        // given
        deleteHistories.save(QUESTION_HISTORY);
        deleteHistories.save(ANSWER_HISTORY);

        // when
        // then
        assertAll(
            () -> assertThat(deleteHistories.findByContentType(QUESTION)).isNotNull(),
            () -> assertThat(deleteHistories.findByContentType(ANSWER)).isNotNull()
        );
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 findByContentIdAndContentType 조회 검증")
    void findByContentIdAndContentType() {
        // given
        DeleteHistory questionDeleteHistory = deleteHistories.save(QUESTION_HISTORY);
        DeleteHistory answerDeleteHistory = deleteHistories.save(ANSWER_HISTORY);

        // when
        List<DeleteHistory> questionDeleteHistories = deleteHistories.findByContentIdAndContentType(
            QUESTION.getId(),
            questionDeleteHistory.getContentType());
        List<DeleteHistory> answerDeleteHistories = deleteHistories.findByContentIdAndContentType(
            ANSWER.getId(),
            answerDeleteHistory.getContentType());

        // then
        assertAll(
            () -> assertThat(questionDeleteHistories).contains(questionDeleteHistory),
            () -> assertThat(answerDeleteHistories).contains(answerDeleteHistory)
        );
    }
}
