package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;

@DataJpaTest
public class DeletedTest {

    @Autowired
    QuestionRepository questions;

    @Autowired
    AnswerRepository answers;

    @Autowired
    UserRepository users;

    private Question QUESTION;
    private User USER;

    @BeforeEach
    public void setUp() throws Exception {
        USER = users.save(UserTest.createUser("answerJavajigi", "password", "javajigi",
            new Email("javajigi@slipp.net")));
        QUESTION = new Question("title1", "contents1").writeBy(USER);
    }

    @Test
    @DisplayName("질문 삭제, DeleteHistory 도메인 검증")
    void deleteOf_question() {
        Question saveQuestion = questions.save(QUESTION);
        Deleted deleted = new Deleted();

        DeleteHistory deleteHistory = deleted.deleteOf(saveQuestion);

        assertAll(
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(saveQuestion.getId()),
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }

    @Test
    @DisplayName("답변 삭제, DeleteHistory 도메인 검증")
    void deleteOf_answer() {
        questions.save(QUESTION);
        Answer saveAnswer = new Answer(USER, QUESTION, "Answers Contents1");

        questions.flush();
        Deleted deleted = new Deleted();

        DeleteHistory deleteHistory = deleted.deleteOf(saveAnswer);

        assertAll(
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(saveAnswer.getId()),
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }

}
