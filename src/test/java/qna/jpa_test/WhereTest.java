package qna.jpa_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WhereTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    /**
     * Where Annotation 학습 테스트
     * (soft-delete 학습 테스트)
     **/

    @BeforeEach
    void setting() {
        UserInfo userInfo = new UserInfo("id", "pwd", "writer", "writer@slipp.net");
        writer = userRepository.save(new User(userInfo));
        question = questionRepository.save(QuestionTest.Q1.writeBy(writer));
    }

    @Test
    @DisplayName("soft-delete 학습 테스트")
    void createdDateTest() {
        Answer actual = answerRepository.save(new Answer(writer, question, new Contents("answer")));
        assertThat(actual.isDeleted()).isEqualTo(false);
        //actual.setDeleted(true);
        answerRepository.delete(actual);

        List<Answer> result = answerRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

}
