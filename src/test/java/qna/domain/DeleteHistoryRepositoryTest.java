package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.util.annotation.DataJpaTestIncludeAuditing;

@DataJpaTestIncludeAuditing
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private Question question;

    private User javajigi;

    private Answer answer;

    @BeforeEach
    void persistRelatedEntities(){
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(javajigi);

        question =  new Question("title1", "contents1").writeBy(javajigi);
        questionRepository.save(question);

        answer = new Answer(javajigi, question, "Answers Contents1");
        answerRepository.save(answer);
    }

    @Test
    void save_테스트(){
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now());
        DeleteHistory managedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        assertThat(managedDeleteHistory.getId()).isNotNull();
    }

    @Test
    void saveAll_테스트(){
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        List<DeleteHistory> managedDeleteHistories = deleteHistoryRepository.saveAll(deleteHistories);
        assertThat( managedDeleteHistories.equals(deleteHistories)).isTrue();
    }
}
