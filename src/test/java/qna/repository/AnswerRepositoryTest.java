package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

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
}
