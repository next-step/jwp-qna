package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    User javaJigi;
    User sanJigi = UserTest.getSanjigi();
    Question question1;
    Answer answer1;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        javaJigi = userRepository.save(UserTest.getJavajigi());
        question1 = questionRepository.save(QuestionTest.getQuestion1(javaJigi));
        answer1 = AnswerTest.getAnswer1(javaJigi, question1);
    }

    @AfterEach
    void afterEach() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("저장하기")
    void save() {
        Answer save = answerRepository.save(answer1);
        assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("삭제되지 않은 답변 id로 조회")
    void findByIdAndDeletedFalse() {
        Answer save = answerRepository.save(answer1);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(save.getId());
        assertThat(optionalAnswer.get().getId()).isEqualTo(save.getId());
    }

    @Test
    @DisplayName("질문 id로 답변들 찾기")
    void findByQuestionIdAndDeletedFalse() {
        Answer save1 = answerRepository.save(answer1);
        save1.setQuestion(question1);

        User user2 = userRepository.save(sanJigi);
        Answer answer2 = AnswerTest.getAnswer2(user2, question1);

        Answer save2 = answerRepository.save(answer2);
        save2.setQuestion(question1);

        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
        assertThat(answerList).contains(save1, save2);
    }

    @Test
    @DisplayName("답변 주인 찾기")
    void isOwner() {
        Answer saveAnswer1 = answerRepository.save(answer1);
        assertThat(saveAnswer1.isOwner(javaJigi)).isTrue();
    }

    @Test
    @DisplayName("내용 수정")
    void updateContent() {
        Answer saveAnswer1 = answerRepository.save(answer1);
        saveAnswer1.setContents("Changed Contents1");
        Optional<Answer> optionalAnswer = answerRepository.findById(saveAnswer1.getId());
        assertThat(optionalAnswer.get().getContents()).isEqualTo("Changed Contents1");
    }

    @Test
    @DisplayName("어떤 질문에 대한 답변인지 변경하기")
    void toQuestion() {
        Answer answer = answerRepository.save(answer1);

        Question question2 = questionRepository.save(QuestionTest.getQuestion2(sanJigi));

        questionRepository.save(question2);
        answer.toQuestion(question2);
        Optional<Answer> optionalAnswer = answerRepository.findById(answer.getId());
        assertThat(optionalAnswer.get().getQuestion().getId()).isEqualTo(question2.getId());
    }
}
