package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer answer1;
    private Answer answer2;

    private Question question;

    @BeforeEach
    void saveDefaultAnswer() {
        question = _saveDefaultQuestions();
        A1.setQuestion(question);
        A2.setQuestion(question);
        answer1 = answerRepository.save(A1);
        answer2 = answerRepository.save(A2);
    }

    private Question _saveDefaultQuestions() {
        return questionRepository.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("답변 등록")
    void save() {
        assertAll(
                () -> assertThat(answer1).isNotNull(),
                () -> assertThat(answer1.isDeleted()).isEqualTo(false)
        );
    }

    @Test
    @DisplayName("질문 수정")
    void update() {
        String contents = "질문 내용을 수정합니다.";
        answer1.setContents(contents);
        Answer actual = answerRepository.save(answer1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(contents)
        );
    }

    @Test
    @DisplayName("Question Id를 통해 Answer 갯수 조회")
    void countByQuestionIdAndDeletedFalse() {
        Long count = answerRepository.countByQuestionIdAndDeletedFalse(question.getId());
        assertThat(count).isEqualTo(2L);
    }
    
    @Test
    @DisplayName("Question Id를 통해 Answer 목록 조회")
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertAll(
                () -> assertThat(answers).isNotNull(),
                () -> assertThat(answers.size()).isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("Answer ID를 통한 삭제")
    void deleteAnswerById() {
        answer1.setDeleted(true);
        assertThat(answerRepository.countByQuestionIdAndDeletedFalse(question.getId())).isEqualTo(1L);
    }
}
