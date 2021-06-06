package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private DeleteHistory deleteHistory1;
    private DeleteHistory deleteHistory2;

    @BeforeEach
    void setUp() {
        User questionWriter = new User("qwriter", "password", "name", "sunju@slipp.net");
        Question question= new Question("title3", "contents2").writeBy(questionWriter);
        userRepository.save(questionWriter);
        Question savedQuestion = questionRepository.save(question);

        User answerWriter = new User("awriter", "password", "name", "sunju@slipp.net");
        Answer answer = new Answer(answerWriter, question, "Answers Contents");
        userRepository.save(answerWriter);
        Answer savedAnswer = answerRepository.save(answer);

        deleteHistory1 = new DeleteHistory(ContentType.ANSWER, savedAnswer, LocalDateTime.now());
        deleteHistory2 = new DeleteHistory(ContentType.QUESTION, savedQuestion, LocalDateTime.now());

    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        deleteHistoryRepository.save(deleteHistory1);
        deleteHistoryRepository.save(deleteHistory2);
        deleteHistoryRepository.flush();
    }
}
