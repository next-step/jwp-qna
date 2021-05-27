package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    private ContentType contentType;
    private long contentId;
    private User deleteUser;
    private LocalDateTime createDate;


    @BeforeEach
    public void setup(){
        contentType = ContentType.QUESTION;
        contentId = 1L;
        deleteUser = UserTest.JAVAJIGI;
        createDate = LocalDateTime.now();
    }

    @Test
    @DisplayName("삭제 이력 생성")
    public void createDeleteHistory(){
        DeleteHistory deleteHistory = new DeleteHistory(contentType, contentId, deleteUser, createDate);
        assertThat(deleteHistory.equals(new DeleteHistory(contentType, contentId, deleteUser, createDate))).isTrue();
    }

    @Test
    @DisplayName("삭제 이력 저장")
    public void SaveDeleteHistory(){
        User user = userRepository.findByUserId("javajigi").orElse(userRepository.save(UserTest.JAVAJIGI));
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(contentType, contentId, user, createDate));
        DeleteHistory selectDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistory.getId()).orElseThrow(NotFoundException::new);
        assertThat(selectDeleteHistory.equals(saveDeleteHistory)).isTrue();
    }
}
