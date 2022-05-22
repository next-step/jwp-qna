package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void initialize() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("Question 저장")
    void save(){
        Question saved = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Question 조회: by DeletedFalse")
    void Question_조회_by_DeletedFalse(){
        generateQuestionDeletedTrue();
        Question questionDeletedFalse = questionRepository
                .save(new Question("title1", "contents1").writeBy(user));
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).containsExactly(questionDeletedFalse);
    }

    @Test
    @DisplayName("Question 조회: by Id, DeletedFalse")
    void Question_조회_byId_DeletedFalse(){
        Question questionDeletedTrue = generateQuestionDeletedTrue();
        Question questionDeletedFalse = questionRepository
                .save(new Question("title1", "contents1").writeBy(user));
        assertAll(
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(questionDeletedTrue.getId()))
                        .isEmpty(),
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(questionDeletedFalse.getId())).get()
                        .isEqualTo(questionDeletedFalse)
        );
    }

    private Question generateQuestionDeletedTrue() {
        Question question = new Question("title1", "contents1").writeBy(user);
        question.setDeleted(true);
        return questionRepository.save(question);
    }
}
