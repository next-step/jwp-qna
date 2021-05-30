package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void findByDeletedFalse() {
        Question question1 = saveQuestion("제목1", "내용1");
        Question question2 = saveQuestion("제목2", "내용2");

        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
        assertThat(questions).extracting("id").contains(question1.getId(), question2.getId());
    }

    @Test
    void findByIdAndDeletedFalse() {
        Question question1 = saveQuestion("제목1", "내용1");
        saveQuestion("제목2", "내용2");

        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question1.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(question1.getId());
    }

    private Question saveQuestion(String title, String content){
        Question question = new Question(title, content);
        questionRepository.save(question);
        return question;
    }
}