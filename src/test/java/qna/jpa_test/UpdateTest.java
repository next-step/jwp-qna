package qna.jpa_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UpdateTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    /**
     * update 학습 테스트
     **/

    @BeforeEach
    void setting() {
        LoginInfo loginInfo = new LoginInfo("id", "pwd", "writer@slipp.net");
        UserInfo userInfo = new UserInfo("writer", loginInfo);
        writer = userRepository.save(new User(userInfo));
        question = questionRepository.save(new Question("title", "question").writeBy(writer));
    }

    @Test
    @DisplayName("findAll 사용하면 update 발생")
    void LastModifiedDate() {
        Answer answer = new Answer(writer, question, "before update");
        Answer actual = answerRepository.save(answer);
        LocalDateTime before = actual.getCreatedAt();

        actual.setContents("after update");
        Answer persist = answerRepository.findAll().get(0);
        LocalDateTime after = persist.getUpdatedAt();
        //해당 테이블에 있는 모든걸 가져와야해서 update 발생

        assertThat(after).isNotEqualTo(before);
    }

    @Test
    @DisplayName("findById 사용하면 update 발생하지 않음")
    void updateFail() {
        Answer answer = new Answer(writer, question, "before update");
        Answer actual = answerRepository.save(answer);
        LocalDateTime before = actual.getCreatedAt();

        actual.setContents("after update");
        Optional<Answer> update = answerRepository.findById(actual.getId());
        LocalDateTime after = update.get().getUpdatedAt();
        //영속성 컨텍스트에서 관리중이어서 update 발생하지 않음

        assertThat(after).isEqualTo(before);
    }
}