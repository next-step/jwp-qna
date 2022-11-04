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
}
