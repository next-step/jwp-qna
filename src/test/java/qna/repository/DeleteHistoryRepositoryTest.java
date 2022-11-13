package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.createDeleteHistory;
import static qna.domain.DomainTestFactory.createUser;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("audit 테스트 : 저장시 생성시각 저장 테스트")
    public void auditTest() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(createDeleteHistory(ContentType.QUESTION, 1L, createUser("DEVELOPYO")));
        assertThat(deleteHistoryRepository.findById(deleteHistory.getId()).get().getCreateDate()).isNotNull();
    }
}
