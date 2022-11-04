package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.TestQuestionFactory;
import qna.domain.TestUserFactory;
import qna.domain.User;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void 질문을_저장하면_저장한_질문을_반환한다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);

        //when
        Question saveQuestion = questionRepository.save(question);

        //then
        assertAll(
                () -> assertThat(saveQuestion.getId()).isNotNull(),
                () -> assertThat(saveQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(saveQuestion.isDeleted()).isFalse()
        );
    }

    @TestFactory
    Collection<DynamicTest> 질문_삭제여부_변경_시나리오() {
        //given
        User writer = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        Question saveQuestion = questionRepository.save(question);
        Long saveQuestionId = saveQuestion.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("삭제여부가 거짓인 질문을 조회한다.", () -> {
                    //when
                    saveQuestion.changeDeleted(false);
                    Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

                    //then
                    assertThat(findQuestion).isPresent();
                }),
                DynamicTest.dynamicTest("질문의 삭제여부를 참으로 바꾸면 조회할 수 없다.", () -> {
                    //when
                    saveQuestion.changeDeleted(true);
                    Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestionId);

                    //then
                    assertThat(findQuestion).isNotPresent();
                })
        );
    }

    @TestFactory
    Collection<DynamicTest> 질문_조회_시나리오() {
        //given
        User writer = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        Question saveQuestion = questionRepository.save(question);
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
