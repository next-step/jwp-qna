package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private

    @DisplayName("저장 테스트")
    @Test
    void save() {
        User writer = users.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, writer, LocalDateTime.now());

        DeleteHistory result = deleteHistoryRepository.save(deleteHistory);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(result.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(result.getDeletedByUser()).isEqualTo(deleteHistory.getDeletedByUser())
        );
    }

    @DisplayName("질문 삭제 시간과 삭제내역 생성시간이 같아야함")
    @Test
    void timeCompare() throws Exception{
        final User user = userRepository.save(UserTest.JAVAJIGI);
        final Question question =questionRepository.save( new Question("test","contents").writeBy(user));
        final Answer answer = answerRepository.save(new Answer(UserTest.JAVAJIGI, question, "Answers Contents1"));

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);
        deleteHistoryRepository.saveAll(deleteHistories);
        assertThat(deleteHistories.get(0).getCreateDate()).isEqualTo(question.getUpdatedAt());
        assertThat(deleteHistories.get(1).getCreateDate()).isEqualTo(answer.getUpdatedAt());

    }
}
