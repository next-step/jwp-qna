package qna.jpa_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmbeddedTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 임베디드 타입 학습 테스트
     **/

    @Test
    @DisplayName("임베디드 타입을 여러 엔티티에서 사용하는 경우")
    void embeddedType() {
        User writer = userRepository.save(new User(new UserInfo("id", "pwd", "writer", "writer@slipp.net")));
        Question question = questionRepository.save(new Question("title", new Contents("question_contents")).writeBy(writer));
        Answer answer = answerRepository.save(new Answer(writer, question, new Contents("answer_contents")));

        assertThat(question.getContents()).isNotEqualTo(answer.getContents());
    }

    @Test
    @DisplayName("임베디드 타입을 여러 엔티티에서 공유 참조를 막기 위한 방법 - 입베디드타입을 불변 객체로 설계")
    void embeddedTypeShare() {
        Contents contents = new Contents("contents");
        User writer = userRepository.save(new User(new UserInfo("id", "pwd", "writer", "writer@slipp.net")));
        Question question = questionRepository.save(new Question("title1", contents).writeBy(writer));
        Answer answer = answerRepository.save(new Answer(writer, question, contents));

        Contents newContents = new Contents("contents2");
        answer.setContents(newContents);

        assertThat(question.getContents()).isNotEqualTo(answer.getContents());
    }

}
