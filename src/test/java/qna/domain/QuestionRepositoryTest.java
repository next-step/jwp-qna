package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void insert_이후_select() {
        // given
        final Question question = new Question("title", "contends");

        // when
        questionRepository.save(question);

        // then
        assertThat(question.getId()).isNotNull();
        assertThat(question).isEqualTo(questionRepository.findById(question.getId()).get());
    }

    @Test
    void update_이후_select() {
        // given
        final Question question = new Question("title", "contents");
        questionRepository.save(question);
        final User newWriter = new User("new writer", "new password", "new name", "new email");
        userRepository.save(newWriter);

        // when
        question.writeBy(newWriter);

        // then
        assertThat(question.getWriter()).isEqualTo(newWriter);
        assertThat(question.getWriter()).isEqualTo(questionRepository.findById(question.getId()).get().getWriter());
    }

    @Test
    void 삭제되지_않은_질문글들을_조회할_수_있어야_한다() {
        // given
        final Question question1 = new Question("title1", "contents1");
        final Question question2 = new Question("title2", "contents2");
        questionRepository.save(question1);
        questionRepository.save(question2);

        // when
        final List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertThat(questions.containsAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2)));
    }

    @Test
    void 삭제되지_않은_질문글을_질문글_아이디로_조회할_수_있어야_한다() {
        // given
        final Question question = new Question("title", "contents");
        questionRepository.save(question);

        // when
        final Question selected = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

        // then
        assertThat(selected).isNotNull();
        assertThat(selected).isEqualTo(question);
    }
}
