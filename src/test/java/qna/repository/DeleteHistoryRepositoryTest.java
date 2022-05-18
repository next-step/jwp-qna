package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    static final DeleteHistory D1 = DeleteHistory.builder()
            .contentType(ContentType.QUESTION)
            .contentId(QuestionRepositoryTest.Q1.getId())
            .deletedBy(QuestionRepositoryTest.Q1.getWriter())
            .createDate(LocalDateTime.now())
            .build();
    static final DeleteHistory D2 = DeleteHistory.builder()
            .contentType(ContentType.ANSWER)
            .contentId(AnswerRepositoryTest.A1.getId())
            .deletedBy(AnswerRepositoryTest.A1.getWriter())
            .createDate(LocalDateTime.now())
            .build();

    @DisplayName("저장 테스트")
    @Test
    void save() {
        DeleteHistory d1 = deleteHistoryRepository.save(D1);
        DeleteHistory d2 = deleteHistoryRepository.save(D2);
        assertAll(
                () -> assertThat(d1).isEqualTo(D1),
                () -> assertThat(d2).isEqualTo(D2)
        );
    }

    @DisplayName("질문 ID 로 조회 테스트")
    @Test
    void findByContentIdAndDeletedById() {
        deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        assertThat(deleteHistories).hasSize(2);
    }
}
