package qna.jpa_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CascadeTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    /**
     * Cascade 학습 테스트
     **/

    @BeforeEach
    void setting() {
        UserInfo userInfo = new UserInfo("id", "pwd", "writer", "writer@slipp.net");
        writer = userRepository.save(new User(userInfo));
        question = questionRepository.save(QuestionTest.Q1.writeBy(writer));
    }

    @Test
    @DisplayName("cascade 학습테스트. 부모가 영속 상태이면, 자식도 영속 상태가 된다")
    void cascade() {
        //given
        User newWriter = new User(new UserInfo("id", "pwd", "user", "email@slipp.net"));
        Question newQuestion = new Question("question", new Contents("question is"));
        Answer newAnswer = new Answer(newWriter, newQuestion, new Contents("answer is"));

        newQuestion.writeBy(newWriter);         // 연관관계의 주인으로 양방향 연관관계 설정 (연관관계를 안해주면 연관관계가 실패하여 save 가 실패한다.)
        newQuestion.addAnswer(newAnswer);       // 연관관계의 주인이 아니지만, cascade 를 통해서 Answer 도 영속성.

        //when
        userRepository.save(newWriter);
        Question actualQuestion = questionRepository.save(newQuestion);

        //then
        //userRepository, questionRepository, answerRepository 모두 같은 영속성 컨텍스트에서 관리된다.
        assertThat(actualQuestion.getAnswer().size()).isEqualTo(1);
        assertThat(answerRepository.findById(newAnswer.getId()).get().getWriter()).isEqualTo(newWriter);
        assertThat(answerRepository.findById(newAnswer.getId())).isNotEmpty();
    }
}
