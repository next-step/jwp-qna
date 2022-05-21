package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("삭제가 false인 질문리스트 길이는 2이다.")
    void findByDeletedFalse_test() {
        //given
        questionRepository.save(QuestionTest.Q1);
        //when
        List<Question> questions = questionRepository.findByDeletedFalse();
        //then
        assertThat(questions).hasSize(1);
    }

    @Test
    @DisplayName("질문 아이디로 삭제가 false인 객체를 검색하여 질문 객체를 반환한다.")
    void findByIdAndDeletedFalse() {
        //given
        questionRepository.save(QuestionTest.Q2);
        //when
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q2.getId());
        //then
        assertAll(
                () -> assertThat(question.isPresent()).isTrue(),
                () -> assertThat(question.get() == QuestionTest.Q2).isTrue()
        );

    }
}
