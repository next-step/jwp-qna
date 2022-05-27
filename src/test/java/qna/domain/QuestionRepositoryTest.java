package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문 저장 및 값 비교 테스트")
    void save() {
        //given
        final Question expected = QuestionTest.Q1;

        //when
        final Question actual = questionRepository.save(expected);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록 조회")
    void findByDeletedFalse() {
        //given
        Question question1 = questionRepository.save(QuestionTest.Q1);
        Question question2 = questionRepository.save(QuestionTest.Q2);

        //when
        question1.setDeleted(true);
        List<Question> founds = questionRepository.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(founds).hasSize(1),
                () -> assertThat(founds).doesNotContain(question1),
                () -> assertThat(founds).containsExactly(question2)
        );
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 목록 조회")
    void findByIdAndDeletedFalse() {
        //given
        Question question1 = questionRepository.save(QuestionTest.Q1);
        Question question2 = questionRepository.save(QuestionTest.Q2);

        //when
        question1.setDeleted(true);
        Optional<Question> foundsQuestion1 = questionRepository.findByIdAndDeletedFalse(question1.getId());
        Optional<Question> foundsQuestion2 = questionRepository.findByIdAndDeletedFalse(question2.getId());

        //then
        assertAll(
                () -> assertThat(foundsQuestion1.isPresent()).isFalse(),
                () -> assertThat(foundsQuestion2.isPresent()).isTrue()
        );
    }
}
