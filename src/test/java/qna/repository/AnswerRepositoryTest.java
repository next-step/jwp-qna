package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("질문아이디로 검색하여 answer객체 리스트를 반환한다.")
    void findByQuestionIdAndDeletedFalse_test() {
        //given
        answerRepository.save(AnswerTest.A1);
        //when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());
        //then
        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("답변아이디로 검색하여 answer객체를 반환한다.")
    void findByIdAndDeletedFalse_test() {
        //given
        answerRepository.save(AnswerTest.A2);
        //when
        Answer answer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A2.getId()).get();
        //then
        assertAll(
                () -> assertThat(answer).isNotNull(),
                () -> assertThat(answer.getId()).isEqualTo(AnswerTest.A2.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A2.getContents())
        );
    }
}
