package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    DeleteHistory deleteHistory;
    User writer;
    Question question;

    @BeforeEach
    void setUp() {
        writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(writer);
        question = new Question("title1", "contents1").writeBy(writer);
        questionRepository.save(question);
        deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), now());
        deleteHistoryRepository.save(deleteHistory);
    }

    @Test
    void 생성() {
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    void 조회() {
        DeleteHistory findHistory = deleteHistoryRepository.findById(deleteHistory.getId()).get();
        assertThat(findHistory).isSameAs(deleteHistory);
    }

    @Test
    void 삭제() {
        deleteHistoryRepository.delete(deleteHistory);
        assertThat(deleteHistoryRepository.findById(deleteHistory.getId())).isNotPresent();
    }
}
