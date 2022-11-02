package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void 답변을_저장하면_저장한_답변을_반환한다() {
        //when
        Answer answer = answerRepository.save(A1);

        //then
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(answer.isDeleted()).isFalse()
        );
    }

    @TestFactory
    Collection<DynamicTest> 답변_삭제여부_변경_시나리오() {
        //given
        Answer saveAnswer = answerRepository.save(A2);
        saveAnswer.setDeleted(false);
        Long saveAnswerId = saveAnswer.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("삭제여부가 거짓인 답변을 조회한다.", () -> {
                    //when
                    Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);

                    //then
                    assertThat(findAnswer).isPresent();
                    assertAll(
                            () -> assertThat(findAnswer.get().getContents()).isEqualTo(saveAnswer.getContents()),
                            () -> assertThat(findAnswer.get().isDeleted()).isFalse()
                    );
                }),
                DynamicTest.dynamicTest("답변의 삭제여부를 참으로 바꾸면 조회할 수 없다.", () -> {
                    //when
                    saveAnswer.setDeleted(true);
                    Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);

                    //then
                    assertThat(findAnswer).isNotPresent();
                })
        );
    }

    @TestFactory
    Collection<DynamicTest> 답변_조회_시나리오() {
        //given
        Answer saveAnswer = answerRepository.save(A1);
        Long saveAnswerId = saveAnswer.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("id로 답변을 조회한다.", () -> {
                    //when
                    Optional<Answer> findAnswer = answerRepository.findById(saveAnswerId);

                    //then
                    assertThat(findAnswer).isPresent();
                }),
                DynamicTest.dynamicTest("답변을 삭제하면 조회할 수 없다.", () -> {
                    //when
                    answerRepository.delete(saveAnswer);
                    Optional<Answer> deleteAnswer = answerRepository.findById(saveAnswerId);

                    //then
                    assertThat(deleteAnswer).isNotPresent();
                })
        );
    }
}
