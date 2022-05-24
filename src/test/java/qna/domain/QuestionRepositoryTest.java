package qna.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void save() {
        //given
        Question expected = QuestionTest.Q1;
        //when
        Question actual = questionRepository.save(expected);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findById() {
        //given
        Question expected = questionRepository.save(QuestionTest.Q1);
        //when
        Question actual = questionRepository.findById(expected.getId()).get();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
