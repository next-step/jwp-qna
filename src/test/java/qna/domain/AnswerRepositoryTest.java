package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void save() {
        //given
        Answer expected = AnswerTest.A1;
        //when
        Answer actual = answerRepository.save(expected);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findById() {
        //given
        Answer expected = answerRepository.save(AnswerTest.A1);
        //when
        Answer actual = answerRepository.findById(AnswerTest.A1.getId()).get();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
