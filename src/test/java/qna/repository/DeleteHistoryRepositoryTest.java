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
    void 삭제이력을_저장하면_반환된_삭제이력의_id는_비어있지_않다() {
        //when
        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);

        //then
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    void 저장한_삭제이력들_전체를_조회한다() {
        //given
        deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);

        //when
        List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();

        //then
        assertThat(findDeleteHistorys).hasSize(2);
    }

    @TestFactory
    Collection<DynamicTest> 삭제이력_조회_시나리오() {
        //given
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(D2);
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
