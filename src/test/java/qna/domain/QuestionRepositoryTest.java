package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    private static User javaJigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
    private static User sanJigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
    private static Question question1 = new Question("title1", "contents1").writeBy(javaJigi);
    private static Question question2 = new Question("title2", "contents2").writeBy(sanJigi);
    private static Answer answer1 = new Answer(javaJigi, question1, "Answers Contents1");

//    User javaJigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
//    User sanJigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
//    Question question1 = new Question("title1", "contents1").writeBy(javaJigi);
//    Question question2 = new Question("title2", "contents2").writeBy(sanJigi);
//    Answer answer1 = new Answer(javaJigi, question1, "Answers Contents1");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;


//    @BeforeEach
//    void init() {
//        userRepository.save(javaJigi);
//        userRepository.save(sanJigi);
//
//        questionRepository.save(question1);
//        questionRepository.save(question2);
//
//        answerRepository.save(answer1);
//    }

    @AfterEach
    void afterEach() {
        userRepository.flush();
        questionRepository.flush();
        answerRepository.flush();

        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("질문에 답변 달기")
    void addAnswer() {
        Answer answer = answerRepository.save(answer1);
        Question question = questionRepository.save(question2);

        question.addAnswer(answer);
        Answer answerFind = answerRepository.findById(answer.getId()).get();
        assertThat(answerFind.getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("저장하기")
    void save() {
        userRepository.save(javaJigi);


        Question question = questionRepository.save(question1);
        Optional<Question> questionOptional = questionRepository.findById(question.getId());

        assertThat(questionOptional.get().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("미삭제 건 전체 조회")
    void notDeleted() {
        userRepository.save(javaJigi);
        userRepository.save(sanJigi);


        Question saveQuestion1 = questionRepository.save(question1);
        Question saveQuestion2 = questionRepository.save(question2);
        List<Question> deletedFalse = questionRepository.findByDeletedFalse();
        assertThat(deletedFalse).contains(saveQuestion1, saveQuestion2);
    }

    @Test
    @DisplayName("미삭제 질문 id로 조회")
    void findByIdAndNotDeleted() {
        userRepository.save(javaJigi);

        Question saveQuestion1 = questionRepository.save(question1);

        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion1.getId());
        assertThat(optionalQuestion.get().getId()).isEqualTo(saveQuestion1.getId());
    }
}
