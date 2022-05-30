package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class AnswerRepositoryTest {

    User javaJigi;
    User sanJigi;
    Question question1;
    Answer answer;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        javaJigi = userRepository.save(UserTest.getJavajigi());
        question1 = questionRepository.save(new Question("title1", "contents1", javaJigi));
        answer = new Answer(javaJigi, question1, "Answers Contents1");

        sanJigi = userRepository.save(UserTest.getSanjigi());
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
        Answer save = answerRepository.save(answer);
        assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("삭제되지 않은 답변 id로 조회")
    void findByIdAndDeletedFalse() {
        Answer save = answerRepository.save(answer);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(save.getId());
        assertThat(optionalAnswer.get().getId()).isEqualTo(save.getId());
    }

    @Test
    @DisplayName("질문 id로 답변들 찾기")
    void findByQuestionIdAndDeletedFalse() {
        Answer save1 = answerRepository.save(answer);
        save1.setQuestion(question1);

        Answer answer2 = new Answer(sanJigi, question1, "Answers Contents2");

        Answer save2 = answerRepository.save(answer2);
        save2.setQuestion(question1);

        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
        assertThat(answerList).contains(save1, save2);
    }

    @Test
    @DisplayName("답변 주인 찾기")
    void isOwner() {
        Answer saveAnswer1 = answerRepository.save(answer);
        assertThat(saveAnswer1.isOwner(javaJigi)).isTrue();
    }

    @Test
    @DisplayName("내용 수정")
    void updateContent() {
        Answer saveAnswer1 = answerRepository.save(answer);
        saveAnswer1.setContents("Changed Contents1");
        Optional<Answer> optionalAnswer = answerRepository.findById(saveAnswer1.getId());
        assertThat(optionalAnswer.get().getContents()).isEqualTo("Changed Contents1");
    }

    @Test
    @DisplayName("어떤 질문에 대한 답변인지 변경하기")
    void toQuestion() {
        Answer answer = answerRepository.save(this.answer);
        Question question2 = questionRepository.save(new Question("title2", "contents2", sanJigi));

        questionRepository.save(question2);
        answer.toQuestion(question2);
        Optional<Answer> optionalAnswer = answerRepository.findById(answer.getId());
        assertThat(optionalAnswer.get().getQuestion().getId()).isEqualTo(question2.getId());
    }

    @Test
    @DisplayName("작성자가 답변 삭제 처리하기")
    void deleteByOwner() throws CannotDeleteException {
        Answer savedAnswer = answerRepository.save(answer);
        savedAnswer.deleteByOwner(javaJigi);

        answerRepository.flush();
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())).isEmpty();
    }

    @Test
    @DisplayName("작성자가 아닌 사람 답변 삭제 요청하기")
    void deleteByNotOwner() {
        Answer savedAnswer = answerRepository.save(answer);
        assertThatThrownBy(() -> savedAnswer.deleteByOwner(sanJigi)).isInstanceOf(CannotDeleteException.class);
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())).isPresent();
    }

    @Test
    @DisplayName("답변 삭제 처리 확인")
    void deleteBySetter() {
        Answer savedAnswer = answerRepository.save(answer);
        savedAnswer.setDeleted(true);
        answerRepository.flush();
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())).isEmpty();
    }

    @Test
    @DisplayName("답변 삭제 처리 확인 repository로 호출")
    void deleteByRepository() {
        Answer savedAnswer = answerRepository.save(answer);
        answerRepository.delete(savedAnswer);
        answerRepository.flush();
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())).isEmpty();
    }
}
