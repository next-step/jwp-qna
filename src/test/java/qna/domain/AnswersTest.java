package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
class AnswersTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    private Question question;
    private User user;
    private Answer answer;

    @BeforeEach
    void setup() {
        user = new User("lkimilhol", "1234", "김일호", "lkimilhol@gmail.com");
        question = new Question("질문", "내용");
        answer = new Answer(user, question, "답변");
        question.writeBy(user);

        userRepository.save(user);
        questionRepository.save(question);
        answerRepository.save(answer);
    }

    @Test
    @DisplayName("삭제 권한이 있는지 체크 - 성공")
    void checkLoginUserAuth() throws CannotDeleteException {
        //given
        //when
        answer.delete(user);
        //then
        assertThat(question.getWriter().getUserId()).isEqualTo("lkimilhol");
    }

    @Test
    @DisplayName("삭제 권한이 있는지 체크 - 실패")
    void checkLoginUserAuthFailed() {
        //given
        User anotherUser = new User("kimmayer", "1234", "김메이어", "kimmayer@gamil.com");
        Answer anotherAnswer = new Answer(anotherUser, question, "다른 답변");
        //when
        question.addAnswer(anotherAnswer);
        //then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answer.delete(anotherUser))
                .withMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}