package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Answer saveAnswer(Answer answer){
        return answerRepository.save(answer);
    }

    @DisplayName("ANSWER이 잘 저장되는지 확인한다.")
    @Test
    void saveAnswerTest() {

        Answer saveAnswer1 = saveAnswer(AnswerTest.A1);
        Answer saveAnswer2 = saveAnswer(AnswerTest.A2);

        assertAll(
                () -> assertThat(saveAnswer1.getId()).isNotNull(),
                () -> assertThat(saveAnswer1.getContents()).isEqualTo(AnswerTest.A1.getContents()),
                () -> assertThat(saveAnswer2.getId()).isNotNull(),
                () -> assertThat(saveAnswer2.getWriterId()).isEqualTo(AnswerTest.A2.getWriterId())
        );

    }

    @DisplayName("QUESTION ID로 삭제되지 않은 질문을 확인한다.")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {

        Answer saveAnswer1 = saveAnswer(AnswerTest.A1);
        Answer saveAnswer2 = saveAnswer(AnswerTest.A2);

        assertEquals(2, answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId()).size());
    }

    @DisplayName("ANSWER ID로 삭제되지 않은 질문을 확인한다.")
    @Test
    void findByIdAndDeletedFalseTest() {

        Answer saveAnswer2 = saveAnswer(AnswerTest.A2);
        assertSame(saveAnswer2, answerRepository.findByIdAndDeletedFalse(saveAnswer2.getId()).get());

        Answer saveAnswer1 = saveAnswer(AnswerTest.A1);
        assertSame(saveAnswer1, answerRepository.findByIdAndDeletedFalse(saveAnswer1.getId()).get());
    }
}
