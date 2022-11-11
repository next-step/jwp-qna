package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.time.LocalDateTime;

import static qna.domain.AnswerTest.ANSWERS_CONTENTS_2;

@DataJpaTest
class RepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    protected User javajigi;
    protected User sanjigi;
    protected Question question1;
    protected Question question2;
    protected Answer answer1;
    protected Answer answer2;
    protected DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        sanjigi = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(sanjigi));
        answer1 = answerRepository.save(new Answer(sanjigi, question1, ANSWERS_CONTENTS_2));
        answer2 = answerRepository.save(new Answer(sanjigi, question2, ANSWERS_CONTENTS_2));
        deleteHistory = deleteHistoryRepository.save(DeleteHistory.of(ContentType.QUESTION, question1.getId(), question1.getWriter(), LocalDateTime.now()));
    }
}
