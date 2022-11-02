package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import qna.NoneDdlDataJpaTest;

@NoneDdlDataJpaTest
class DeleteHistoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void 삭제히스토리_저장_및_찾기() {
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, Q1.getId(), JAVAJIGI.getId(),
            LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType()),
            () -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId()),
            () -> assertThat(actual.getCreateDate()).isEqualTo(expected.getCreateDate()),
            () -> assertThat(actual.getDeletedById()).isEqualTo(JAVAJIGI.getId())
        );
        assertThat(deleteHistoryRepository.findById(actual.getId())).contains(actual);
    }
}