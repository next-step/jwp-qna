package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void findByQuestionIdAndDeletedFalse() {
        User user = new User(1L, "userId", "비밀번호", "홍길동", "h@email.com");
        Question question = new Question(1L, "제목", "내용");
        Answer answer = new Answer(user, question, "내용");
        answerRepository.save(answer);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());

        assertThat(answers.size()).isEqualTo(1);
        assertThat(answers.get(0).getId()).isEqualTo(answer.getId());
    }

    @Test
    void findByIdAndDeletedFalse() {
        User user = new User(1L, "userId", "비밀번호", "홍길동", "h@email.com");
        Question question = new Question(1L, "제목", "내용");
        Answer answer = new Answer(user, question, "내용");
        answerRepository.save(answer);

        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(answer.getId());
    }
}