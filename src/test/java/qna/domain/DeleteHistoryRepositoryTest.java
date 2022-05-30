package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    User savedUser;
    Question savedQuestion;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        User user = new User("userId", "password", "hyun", "hyun@email.com");
        savedUser = userRepository.save(user);

        Question question = new Question("title", "this is content").writeBy(savedUser);
        savedQuestion = questionRepository.save(question);
    }

    @Test
    @DisplayName("질문 삭제 내역 저장하기")
    void saveQuestionDeleteHistory() {
        savedQuestion.setDeleted(true);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, savedQuestion.getId(), savedUser);
        deleteHistoryRepository.save(deleteHistory);

        Assertions.assertThat(deleteHistoryRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("답변 삭제 내역 저장하기")
    void saveAnswerDeleteHistory() {
        Answer answer = new Answer(savedUser, savedQuestion, "answer contents");
        Answer savedAnswer = answerRepository.save(answer);

        savedAnswer.setDeleted(true);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, savedAnswer.getId(), savedUser);
        deleteHistoryRepository.save(deleteHistory);

        answerRepository.flush();
        deleteHistoryRepository.flush();

        Assertions.assertThat(deleteHistoryRepository.findAll()).hasSize(1);
    }
}