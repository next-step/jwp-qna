package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.answer5;
import static qna.domain.UserTest.userA;
import static qna.domain.UserTest.userB;

@DataJpaTest
public class QuestionTest {
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
        Question Q5 = question5();
        Q5.mappingToWriter(userRepository.save(userA()));
        Q5.mappingToAnswer(answerRepository.save(answer5()));

        //when
        Question result = questionRepository.save(Q5);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getWriter()).isNotNull(),
                () -> assertThat(result.getAnswer()).isNotEmpty()
        );
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        //given
        Question Q5 = question5();
        Q5.mappingToWriter(userRepository.save(userA()));
        Q5.mappingToAnswer(answerRepository.save(answer5()));

        //when
        Question expected = questionRepository.save(Q5);
        Question result = questionRepository.findById(expected.getId()).orElse(null);

        //then
        assertAll(
                () -> assertThat(result).isEqualTo(expected),
                () -> assertThat(result.getAnswer().get(0).getContents()).isEqualTo("답변5")
        );

    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        //given
        Question Q5 = question5();
        Q5.mappingToWriter(userRepository.save(userA()));
        Q5.mappingToAnswer(answerRepository.save(answer5()));

        //when
        Question expected = questionRepository.save(Q5);
        expected.updateQuestionContents("질문 수정!!");
        expected.mappingToWriter(userB());
        Question result = questionRepository.findById(expected.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(result.getContents()).isEqualTo("질문 수정!!");
        assertThat(result.getWriter().getUserId()).isEqualTo("javajigi");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        //given
        Question Q5 = question5();
        Q5.mappingToWriter(userRepository.save(userA()));
        Q5.mappingToAnswer(answerRepository.save(answer5()));

        //when
        Question save = questionRepository.save(Q5);
        questionRepository.delete(save);
        Question found = questionRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isNull();
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete2() {
        Question Q3 = questionRepository.findById(3L).orElse(null);
        questionRepository.delete(Q3);
        Question result = questionRepository.findById(3L).orElse(null);
        //then
        assertThat(result).isNull();
    }

    static Question question5() {
        return new Question("질문 타이틀5", "질문5");
    }

    static Question question6() {
        return new Question("질문 타이틀6", "질문6");
    }
}
