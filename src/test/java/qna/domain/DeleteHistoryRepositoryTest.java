package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("삭제이력 저장 테스트")
    void save(){
        //given
        User user = userRepository.save(new User(1L, "qnaAdmin", "qnaAdmin1!", "관리자", ""));
        DeleteHistory saveHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 10L, user, LocalDateTime.now()));

        //when
        DeleteHistory findHistory = deleteHistoryRepository.findById(saveHistory.getId())
                .orElse(null);

        //then
        assertThat(findHistory).isEqualTo(saveHistory);
    }

    @Test
    @DisplayName("모든 삭제된 질문의 내역 조회")
    void findAllByContentTypeTest1(){
        //given
        User user = userRepository.save(new User(1L, "qnaAdmin", "qnaAdmin1!", "관리자", ""));
        DeleteHistory saveHistory1 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 10L, user, LocalDateTime.now()));
        DeleteHistory saveHistory2 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 10L, user, LocalDateTime.now()));

        //when
        List<DeleteHistory> histories = deleteHistoryRepository.findAllByContentType(ContentType.QUESTION);

        //then
        assertThat(histories).containsExactly(
                saveHistory1, saveHistory2
        );
    }

    @Test
    @DisplayName("특정 사용자의 삭제내역 조회 테스트")
    void findByDeletedUserTest(){
        //given
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        DeleteHistory history1 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now()));
        DeleteHistory history2 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 2L, user, LocalDateTime.now()));
        DeleteHistory history3 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.ANSWER, 3L, user, LocalDateTime.now()));

        //when
        List<DeleteHistory> histories = deleteHistoryRepository.findAllByDeletedUser(user);
        List<DeleteHistory> historiesByUser = user.getDeleteHistories();

        //then
        assertThat(histories.size()).isEqualTo(3);
        assertThat(histories).containsExactly(
                history1, history2, history3
        );
        assertThat(historiesByUser.size()).isEqualTo(3);
        assertThat(histories).isEqualTo(historiesByUser);
    }
}
