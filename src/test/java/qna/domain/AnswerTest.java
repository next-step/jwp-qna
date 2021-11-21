package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.userA;
import static qna.domain.UserTest.userB;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        //given
        Answer A5 = answer5();
        A5.mappingToWriter(userRepository.save(userB()));
        A5.mappingToQuestion(questionRepository.save(question6()));

        //when
        Answer result = answerRepository.save(A5);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        //given
        Answer A6 = answer6();
        A6.mappingToWriter(userRepository.save(userB()));
        A6.mappingToQuestion(questionRepository.save(question6()));

        //when
        Answer save = answerRepository.save(A6);
        Answer result = answerRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(result).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        //given
        Answer A6 = answer6();
        A6.mappingToWriter(userRepository.save(userB()));
        A6.mappingToQuestion(questionRepository.save(question6()));

        //when
        Answer save = answerRepository.save(A6);
        save.updateAnswerContents("답변 수정!!");
        save.mappingToQuestion(questionRepository.save(question5()));
        Answer result = answerRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(result.getContents()).isEqualTo("답변 수정!!");
        assertThat(result.getQuestion().getContents()).isEqualTo(question5().getContents());
    }

    @DisplayName("Update 테스트 2")
    @Test
    void update2() {
        //when
        Answer A1 = answerRepository.findById(1L).orElse(null);
        Answer save = answerRepository.save(A1);
        save.updateAnswerContents("답변 수정!!");
        Answer result = answerRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(result.getContents()).isEqualTo("답변 수정!!");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        //given
        Answer A6 = answer6();
        A6.mappingToWriter(userRepository.save(userB()));
        A6.mappingToQuestion(questionRepository.save(question6()));

        //when
        Answer save = answerRepository.save(A6);
        answerRepository.delete(save);
        Answer found = answerRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isNull();
    }

    @DisplayName("Delete 테스트 2")
    @Test
    void delete2() {
        Answer A1 = answerRepository.findById(1L).orElse(null);
        answerRepository.delete(A1);
        Answer result = answerRepository.findById(1L).orElse(null);
        //then
        assertThat(result).isNull();
    }

    static Answer answer5() {
        return new Answer(userA(), question5(), "답변5");
    }

    static Answer answer6() {
        return new Answer(userB(), question6(), "답변6");
    }
}
