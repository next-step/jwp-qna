package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("삭제 이력을 저장한다.")
    void save() {
        //given
        User user = userRepository.save(JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
        // given-when
        DeleteHistory actaul = deleteHistoryRepository.save(deleteHistory);
        // then
        assertThat(actaul).isNotNull();
    }
}
