package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Question question;

    @BeforeEach
    public void setup(){
        User user = userRepository.findByUserId("javajigi").orElse(userRepository.save(UserTest.JAVAJIGI));
        question = new Question("Save Question Title", "Save Question Content").writeBy(user);
    }

    @Test
    @DisplayName("질문 저장")
    public void saveQuestion() {
        Question saveQuestion = questionRepository.save(question);

        Question selectQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId()).orElseThrow(NotFoundException::new);
        assertThat(selectQuestion.equals(saveQuestion)).isTrue();
    }

    @Test
    @DisplayName("질문 검색")
    public void selectQuestion() {
        Question saveQuestion = questionRepository.save(question);

        Question selectQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId()).orElseThrow(NotFoundException::new);
        assertThat(selectQuestion.equals(saveQuestion)).isTrue();
    }
}
