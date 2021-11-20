package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Question 데이터 save 하는 테스트 진행")
    @Test
    public void save() {

        // given
        Question question1 = QuestionTest.Q1;
        Question question2 = QuestionTest.Q2;

        // when
        Question saved1 = questionRepository.save(question1);
        Question saved2 = questionRepository.save(question2);

        // then
        Assertions.assertThat(saved1).isEqualTo(question1);
        Assertions.assertThat(saved2).isEqualTo(question2);

    }

    @DisplayName("Question 데이터 find 하는 테스트 진행")
    @Test
    public void find() {

        // given
        Question question1 = QuestionTest.Q3;
        Question question2 = QuestionTest.Q4;

        Question saved1 = questionRepository.save(question1);
        Question saved2 = questionRepository.save(question2);

        // when
        Question find1 = questionRepository.findById(saved1.getId()).get();
        Question find2 = questionRepository.findById(saved2.getId()).get();

        // then
        Assertions.assertThat(find1).isEqualTo(question1);
        Assertions.assertThat(find2).isEqualTo(question2);
    }

}
