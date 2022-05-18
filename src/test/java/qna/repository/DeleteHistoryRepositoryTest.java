package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    Answer answer;
    Question question;
    User user;
    DeleteHistory deleteHistory1;
    DeleteHistory deleteHistory2;

    @BeforeEach
    void setUp() {
        user = User.builder("javajigi", "password", "name")
                .email("javajigi@slipp.net")
                .build();
        question = Question.builder("title1")
                .contents("contents1")
                .build()
                .writeBy(user);
        answer = Answer.builder(user, question)
                .contents("Answers Contents1")
                .build();
        deleteHistory1 = DeleteHistory.builder()
                .contentType(ContentType.QUESTION)
                .contentId(question.getId())
                .deletedBy(question.getWriter())
                .createDate(LocalDateTime.now())
                .build();
        deleteHistory2 = DeleteHistory.builder()
                .contentType(ContentType.ANSWER)
                .contentId(answer.getId())
                .deletedBy(answer.getWriter())
                .createDate(LocalDateTime.now())
                .build();
    }

    @DisplayName("저장 테스트")
    @Test
    void save() {
        DeleteHistory saved1 = deleteHistoryRepository.save(deleteHistory1);
        DeleteHistory saved2 = deleteHistoryRepository.save(deleteHistory2);
        assertAll(
                () -> assertThat(saved1).isEqualTo(deleteHistory1),
                () -> assertThat(saved2).isEqualTo(deleteHistory2)
        );
    }

    @DisplayName("질문 ID 로 조회 테스트")
    @Test
    void findByContentIdAndDeletedById() {
        deleteHistoryRepository.save(deleteHistory1);
        deleteHistoryRepository.save(deleteHistory2);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        assertThat(deleteHistories).hasSize(2);
    }
}
