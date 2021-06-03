package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

public class AnswerTest {

    public static final Answer A1 = new Answer(1L, JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    public void 답변_생성_작성자_질문_세팅_테스트_GREEN() {
        User maeve = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

        Question question = new Question(1L, "title", "머시깽이");

        question.writeBy(maeve);

        Answer answer = new Answer(1L, JAVAJIGI, question, "머식꺵깽이야");

        assertThat(answer.writer()).isEqualTo(JAVAJIGI);
        assertThat(answer.questionId()).isEqualTo(1L);
        assertThat(answer.isOwner(JAVAJIGI)).isTrue();
    }

    @Test
    public void 답변_생성_작성자_질문_세팅_테스트_RED_user_miss() {
        User maeve = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

        Question question = new Question(1L, "title", "머시깽이");

        question.writeBy(maeve);

        assertThatThrownBy(() -> {
            new Answer(1L, null, question, "머식꺵깽이야");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    public void 답변_생성_작성자_질문_세팅_테스트_RED_questuin_miss() {
        assertThatThrownBy(() -> {
            new Answer(1L, JAVAJIGI, null, "머식꺵깽이야");
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void 답변_주인확인_테스트(){
        User maeve = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

        Question question = new Question("title", "머시깽이");

        question.writeBy(maeve);

        Answer answer = new Answer(JAVAJIGI, question, "머식꺵깽이야");

        assertThat(answer.isOwner(JAVAJIGI)).isTrue();
    }
}
