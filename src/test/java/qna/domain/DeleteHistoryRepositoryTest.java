package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("저장하기")
    void saveDeleteHistory() {
        User user = new User("userId", "password", "hyun", "hyun@email.com");
        User savedUser = userRepository.save(user);

        Question question = new Question("title", "this is content").writeBy(savedUser);
        Question savedQuestion = questionRepository.save(question);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, savedQuestion.getId(), savedUser);

        deleteHistoryRepository.save(deleteHistory);

        Assertions.assertThat(deleteHistoryRepository.findAll()).hasSize(1);
    }
}