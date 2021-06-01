package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    private ContentType contentType;
    private long contentId;
    private LocalDateTime createDate;
    private User user;

    @BeforeEach
    public void setup(){
        contentType = ContentType.QUESTION;
        contentId = 1L;
        createDate = LocalDateTime.now();
        user = userRepository.findByUserId("javajigi").orElse(userRepository.save(UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("삭제 이력 저장")
    public void saveDeleteHistory(){
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(contentType, contentId, user, createDate));
        assertThat(saveDeleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("삭제 이력 검색")
    public void selectDeleteHistory(){
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(contentType, contentId, user, createDate));
        DeleteHistory selectDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistory.getId()).orElseThrow(NotFoundException::new);

        assertThat(selectDeleteHistory.equals(saveDeleteHistory)).isTrue();
    }
}
