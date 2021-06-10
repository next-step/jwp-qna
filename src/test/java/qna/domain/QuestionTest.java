package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setup() {
        user = new User("lkimilhol", "1234", "김일호", "lkimilhol@gmail.com");
        question = new Question("질문", "내용");
        Answer answer = new Answer(user, question, "답변");
        question.writeBy(user);

        userRepository.save(user);
        questionRepository.save(question);
        answerRepository.save(answer);
    }

    @Test
    @DisplayName("삭제 실패 - 글을 작성한 유저가 아님")
    void deleteFailed() {
        //given
        //when
        //then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.delete(new User("test", "1234", "테스트", "test@gmail.com")));
    }

    @Test
    @DisplayName("삭제 성공")
    void delete() throws CannotDeleteException {
        //given
        //when
        question.delete(user);
        //then
        assertThat(question.isDeleted()).isTrue();
    }
}