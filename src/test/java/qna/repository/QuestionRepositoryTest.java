package qna.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    Question question;

    @Test
    @DisplayName("삭제가 되지않은 질문리스트 검색하고 반환된 리스트 길이는 2이다.")
    void findByDeletedFalse_test() {
        //given
        questionRepository.save(QuestionTest.Q1);
        //when
        List<Question> questions = questionRepository.findByDeletedFalse();
        //then
        assertThat(questions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제 되지않은 질문중에서 질문 아이디로 검색하여 질문 객체를 반환한다.")
    void findByIdAndDeletedFalse() {
        //given
        questionRepository.save(QuestionTest.Q2);
        //when
        Question question = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q2.getId()).get();
        //then
        assertAll(
                () -> assertThat(question).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo(QuestionTest.Q2.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(QuestionTest.Q2.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(QuestionTest.Q2.getWriterId())
        );
    }
}
