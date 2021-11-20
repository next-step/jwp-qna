package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Answer 데이터 save 하는 테스트 진행")
    @Test
    public void save() {

        // given
        Answer answer1 = AnswerTest.A1;
        Answer answer2 = AnswerTest.A2;

        // when
        Answer saved1 = answerRepository.save(answer1);
        Answer saved2 = answerRepository.save(answer2);

        // then
        Assertions.assertThat(saved1).isEqualTo(answer1);
        Assertions.assertThat(saved2).isEqualTo(answer2);
    }

    @DisplayName("Answer 데이터 find 하는 테스트 진행")
    @Test
    public void find() {

        // given
        Answer answer1 = AnswerTest.A3;
        Answer answer2 = AnswerTest.A4;

        // when
        Answer saved1 = answerRepository.save(answer1);
        Answer saved2 = answerRepository.save(answer2);

        Answer find1 = answerRepository.findById(saved1.getId()).orElse(null);
        Answer find2 = answerRepository.findById(saved2.getId()).orElse(null);

        // then
        Assertions.assertThat(saved1).isEqualTo(find1);
        Assertions.assertThat(saved2).isEqualTo(find2);
    }
}
