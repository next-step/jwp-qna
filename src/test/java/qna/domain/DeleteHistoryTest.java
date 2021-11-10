package qna.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    private void beforeEach() {
        deleteHistoryRepository.save(deleteHistory1);
        deleteHistoryRepository.save(deleteHistory2);
    }

    @DisplayName("내용 유형에대한 삭제 이력을 조회한다.")
    @Test
    public void find_forContentType() {
        // given

        // when
        List<DeleteHistory> findResult = deleteHistoryRepository.findByContentType(ContentType.ANSWER);

        // then
        Assertions.assertThat(findResult).containsExactly(deleteHistory1);
    }
}
