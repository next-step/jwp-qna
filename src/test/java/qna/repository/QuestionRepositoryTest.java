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
    void 질문을_저장하면_저장한_질문_객체를_반환한다() {
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
    void 질문을_저장한_후_삭제되지_않은_질문들을_조회하면_삭제여부가_false인_질문_리스트가_반환된다() {
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
    Collection<DynamicTest> 질문을_저장한_후_삭제여부값에_따라_findByIdAndDeletedFalse메소드_조회_결과가_달라진다() {
        //given
        Question saveQuestion = questionRepository.save(Q2);
        Long saveQuestionId = saveQuestion.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("저장한 질문은 findByIdAndDeletedFalse()로 조회하면 정상적으로 조회가 된다.", () -> {
                    //when
                    saveQuestion.setDeleted(false);
                    Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

                    //then
                    assertThat(findQuestion).isPresent();
                }),
                DynamicTest.dynamicTest("저장한 질문의 삭제여부가 true이면 findByIdAndDeletedFalse() 조회 시 조회되지 않는다.", () -> {
                    //when
                    saveQuestion.setDeleted(true);
                    Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

                    //then
                    assertThat(findQuestion).isNotPresent();
                })
        );
    }

    @TestFactory
    Collection<DynamicTest> 질문을_저장하면_조회가_되지만_해당_질문을_삭제하고_조회하면_더_이상_조회되지_않는다() {
        //given
        Question saveQuestion = questionRepository.save(Q2);
        Long saveQuestionId = saveQuestion.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("저장한 질문의 id로 질문을 조회하면 정상적으로 조회가 된다.", () -> {
                    //when
                    Optional<Question> findQuestion = questionRepository.findById(saveQuestionId);

                    //then
                    assertThat(findQuestion).isPresent();
                }),
                DynamicTest.dynamicTest("저장한 질문을 삭제하고, 다시 조회하면 해당 질문이 조회되지 않는다.", () -> {
                    //when
                    questionRepository.delete(saveQuestion);
                    Optional<Question> deleteQuestion = questionRepository.findById(saveQuestionId);

                    //then
                    assertThat(deleteQuestion).isNotPresent();
                })
        );
    }
}
