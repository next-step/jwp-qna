package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.TestAnswerFactory;
import qna.domain.TestDeleteHistoryFactory;
import qna.domain.TestQuestionFactory;
import qna.domain.TestUserFactory;
import qna.domain.User;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void 삭제이력을_저장하면_반환된_삭제이력의_id는_비어있지_않다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);
        DeleteHistory deleteHistory = TestDeleteHistoryFactory.create(ContentType.ANSWER, answer.getId(), answer.getWriter());

        //when
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        //then
        assertThat(saveDeleteHistory.getId()).isNotNull();
    }

    @Test
    void 저장한_삭제이력들_전체를_조회한다() {
        //given
        User writer = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);
        DeleteHistory deleteHistory1 = TestDeleteHistoryFactory.create(ContentType.QUESTION, question.getId(), question.getWriter());
        DeleteHistory deleteHistory2 = TestDeleteHistoryFactory.create(ContentType.ANSWER, answer.getId(), answer.getWriter());

        deleteHistoryRepository.save(deleteHistory1);
        deleteHistoryRepository.save(deleteHistory2);

        //when
        List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();

        //then
        assertThat(findDeleteHistorys).hasSize(2);
    }

    @TestFactory
    Collection<DynamicTest> 삭제이력_조회_시나리오() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        DeleteHistory deleteHistory = TestDeleteHistoryFactory.create(ContentType.QUESTION, question.getId(), question.getWriter());
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        Long saveDeleteHistoryId = saveDeleteHistory.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("id로 삭제이력을 조회한다.", () -> {
                    //when
                    Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistoryId);

                    //then
                    assertThat(findDeleteHistory).isPresent();
                }),
                DynamicTest.dynamicTest("삭제이력을 삭제하면 조회할 수 없다.", () -> {
                    //when
                    deleteHistoryRepository.delete(saveDeleteHistory);
                    Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistoryId);

                    //then
                    assertThat(findDeleteHistory).isNotPresent();
                })
        );
    }

    @Test
    void 질문_삭제여부_변경_시_삭제이력_추가() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Question saveQuestion = questionRepository.save(question);

        //when
        List<DeleteHistory> deleteHistories = saveQuestion.delete(writer);
        deleteHistoryRepository.saveAll(deleteHistories);
        List<DeleteHistory> findDeleteHistories = deleteHistoryRepository.findAll();

        //then
        assertThat(findDeleteHistories).containsAll(deleteHistories);
    }

    @Test
    void 답변_삭제여부_변경_시_삭제이력_추가() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);
        Answer saveAnswer = answerRepository.save(answer);

        //when
        DeleteHistory deleteHistory = saveAnswer.delete(writer);
        deleteHistoryRepository.save(deleteHistory);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

        //then
        assertThat(deleteHistories).contains(deleteHistory);
    }
}
