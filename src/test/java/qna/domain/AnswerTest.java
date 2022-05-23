package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("저장하기")
    void save() {
        Answer save = answerRepository.save(A1);
        assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("삭제되지 않은 답변 id로 조회")
    void findByIdAndDeletedFalse() {
        Answer save = answerRepository.save(A1);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(save.getId());
        assertThat(optionalAnswer.get().getId()).isEqualTo(save.getId());
    }

    @Test
    @DisplayName("질문 id로 답변들 찾기")
    void findByQuestionIdAndDeletedFalse() {
        Answer save1 = answerRepository.save(A1);
        save1.setQuestionId(QuestionTest.Q1.getId());
        Answer save2 = answerRepository.save(A2);
        save2.setQuestionId(QuestionTest.Q1.getId());

        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(answerList).contains(save1, save2);
    }

    @Test
    @DisplayName("답변 주인 찾기")
    void isOwner() {
        Answer answer1 = answerRepository.save(A1);
        assertThat(answer1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("내용 수정")
    void updateContent() {
        Answer answer1 = answerRepository.save(A1);
        answer1.setContents("Changed Contents1");
        Optional<Answer> optionalAnswer = answerRepository.findById(answer1.getId());
        assertThat(optionalAnswer.get().getContents()).isEqualTo("Changed Contents1");
    }

    @Test
    @DisplayName("어떤 질문에 대한 답변인지 변경하기")
    void toQuestion() {
        Answer answer = answerRepository.save(A1);
        questionRepository.save(QuestionTest.Q2);
        answer.toQuestion(QuestionTest.Q2);
        Optional<Answer> optionalAnswer = answerRepository.findById(answer.getId());
        assertThat(optionalAnswer.get().getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
    }

}
