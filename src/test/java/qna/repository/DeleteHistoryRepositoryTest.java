package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DeleteHistoryTest.D1;
import static qna.domain.DeleteHistoryTest.D2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void 삭제이력을_저장하면_반환된_삭제이력_객체의_id는_비어있지_않다() {
        //when
        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);

        //then
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    void 삭제이력을_두개_저장한_후_전체_조회를_하면_총_두개의_삭제이력_리스트를_반환한다() {
        //given
        deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);

        //when
        List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();

        //then
        assertThat(findDeleteHistorys).hasSize(2);
    }

    @TestFactory
    Collection<DynamicTest> 삭제이력을_저장하면_조회가_되지만_해당_삭제이력을_삭제하고_조회하면_해당_이력이_조회되지_않는다() {
        //given
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(D2);
        Long saveDeleteHistoryId = saveDeleteHistory.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("저장한 삭제이력의 id로 삭제이력을 조회하면 정상적으로 조회가 된다.", () -> {
                    //when
                    Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistoryId);

                    //then
                    assertThat(findDeleteHistory).isPresent();
                }),
                DynamicTest.dynamicTest("저장한 삭제이력을 삭제하고, 다시 조회하면 해당 삭제이력이 조회되지 않는다.", () -> {
                    //when
                    deleteHistoryRepository.delete(saveDeleteHistory);
                    Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistoryId);

                    //then
                    assertThat(findDeleteHistory).isNotPresent();
                })
        );
    }
}
