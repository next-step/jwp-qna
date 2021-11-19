package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.question;
import static qna.domain.UserTest.admin;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(admin(), question(), "JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 합니다.");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        //when
        A1.mappedToWriter(userRepository.save(admin()));
        A1.mappedToQuestion(questionRepository.save(question()));
        Answer save = answerRepository.save(A1);

        //then
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        //when
        A1.mappedToWriter(userRepository.save(admin()));
        A1.mappedToQuestion(questionRepository.save(question()));
        Answer save = answerRepository.save(A1);
        Answer found = answerRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        //when
        A1.mappedToWriter(userRepository.save(admin()));
        A1.mappedToQuestion(questionRepository.save(question()));
        Answer save = answerRepository.save(A1);
        save.setContents("update!!");
        //save.setQuestion(questionRepository.save(question2()));
        Answer found = answerRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(found.getContents()).isEqualTo("update!!");
        //assertThat(found.getContents()).isEqualTo("JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 합니다.");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        //when
        A1.mappedToWriter(userRepository.save(admin()));
        A1.mappedToQuestion(questionRepository.save(question()));
        Answer save = answerRepository.save(A1);
        answerRepository.delete(save);
        Answer found = answerRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isNull();
    }
}
