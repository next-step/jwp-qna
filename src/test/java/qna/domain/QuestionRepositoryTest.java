package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;

    private Question savedQuestion;
    private User savedWriter;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        this.savedWriter = userRepository.save(newUser("1"));
        this.savedQuestion = questionRepository.save(newQuestion("1").writeBy(this.savedWriter));
    }

    @Test
    void 질문_저장_및_찾기() {
        Question question = newQuestion("1");
        assertAll(
            () -> assertThat(savedQuestion.getId()).isNotNull(),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(savedQuestion.isDeleted()).isFalse(),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(savedWriter),
            () -> assertThat(savedQuestion.getCreatedAt()).isNotNull(),
            () -> assertThat(savedQuestion.getUpdatedAt()).isNotNull(),
            () -> assertThat(savedQuestion.getUpdatedAt()).isEqualTo(savedQuestion.getCreatedAt())
        );
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).contains(savedQuestion);
    }

    @Test
    void 질문_복수개_저장_후_찾기() {
        User savedUser2 = userRepository.save(newUser("2"));
        Question question2 = newQuestion("2").writeBy(savedUser2);
        Question savedQuestion2 = questionRepository.save(question2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        assertThat(findQuestions).hasSize(2);
        assertThat(findQuestions).containsExactly(savedQuestion, savedQuestion2);
    }

    @Test
    void 질문_삭제여부_변경() {
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).contains(savedQuestion);
        savedQuestion.changeDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).isEmpty();
    }

    @Test
    void 연관관계_편의메서드_예외처리_확인() {
        User answerWriter = userRepository.save(newUser("2"));
        Answer savedAnswer = answerRepository.save(new Answer(answerWriter, savedQuestion, "answerContents1"));
        savedAnswer.toQuestion(savedQuestion);
        savedAnswer.toQuestion(savedQuestion);
        savedAnswer.toQuestion(savedQuestion);

        assertThat(savedQuestion.getAnswers()).hasSize(1);
    }
}