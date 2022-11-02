package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void 질문을_저장하면_저장한_질문을_반환한다() {
        //when
        Question question = questionRepository.save(Q1);

        //then
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse()
        );
    }

    @Test
    void 삭제여부가_거짓인_질문들을_조회한다() {
        //given
        Question saveQuestion1 = questionRepository.save(Q1);
        Question saveQuestion2 = questionRepository.save(Q2);
        saveQuestion1.setDeleted(false);
        saveQuestion2.setDeleted(true);

        //when
        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(findQuestions.size()).isEqualTo(1),
                () -> assertThat(findQuestions).contains(saveQuestion1),
                () -> assertThat(saveQuestion2).isNotIn(findQuestions)
        );
    }

    @TestFactory
    Collection<DynamicTest> 질문_삭제여부_변경_시나리오() {
        //given
        Question saveQuestion = questionRepository.save(Q2);
        Long saveQuestionId = saveQuestion.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("삭제여부가 거짓인 질문을 조회한다.", () -> {
                    //when
                    saveQuestion.setDeleted(false);
                    Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

                    //then
                    assertThat(findQuestion).isPresent();
                }),
                DynamicTest.dynamicTest("질문의 삭제여부를 참으로 바꾸면 조회할 수 없다.", () -> {
                    //when
                    saveQuestion.setDeleted(true);
                    Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

                    //then
                    assertThat(findQuestion).isNotPresent();
                })
        );
    }

    @TestFactory
    Collection<DynamicTest> 질문_조회_시나리오() {
        //given
        Question saveQuestion = questionRepository.save(Q2);
        Long saveQuestionId = saveQuestion.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("id로 질문을 조회한다.", () -> {
                    //when
                    Optional<Question> findQuestion = questionRepository.findById(saveQuestionId);

                    //then
                    assertThat(findQuestion).isPresent();
                }),
                DynamicTest.dynamicTest("질문을 삭제하면 조회할 수 없다.", () -> {
                    //when
                    questionRepository.delete(saveQuestion);
                    Optional<Question> deleteQuestion = questionRepository.findById(saveQuestionId);

                    //then
                    assertThat(deleteQuestion).isNotPresent();
                })
        );
    }
}
