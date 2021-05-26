package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private ContentType contentType;
    private long contentId;
    private long deletedById;
    private LocalDateTime createDate;

    @BeforeEach
    public void setup(){
        contentType = ContentType.QUESTION;
        contentId = 1L;
        deletedById = 1L;
        createDate = LocalDateTime.now();
    }

    @Test
    @DisplayName("삭제 이력 생성")
    public void createDeleteHistory(){
        DeleteHistory deleteHistory = new DeleteHistory(contentType, contentId, deletedById, createDate);
        assertThat(deleteHistory.equals(new DeleteHistory(contentType, contentId, deletedById, createDate))).isTrue();
    }

    @Test
    @DisplayName("삭제 이력 저장")
    public void SaveDeleteHistory(){
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(contentType, contentId, deletedById, createDate));
        DeleteHistory selectDeleteHistory = deleteHistoryRepository.findById(1L).orElseThrow(NotFoundException::new);
        assertThat(selectDeleteHistory.equals(saveDeleteHistory)).isTrue();
    }
}
