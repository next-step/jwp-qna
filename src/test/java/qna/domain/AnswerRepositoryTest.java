package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    public static final Answer A1 = new Answer(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.Q1, "Answers Contents2");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 댓글_등록() {
        final Answer answer = answerRepository.save(A1);
        assertThat(answer).isNotNull();
    }

    @Test
    void 댓글_조회() {
        final Answer expected = answerRepository.save(A1);
        final Answer actual = answerRepository.findById(expected.getId()).get();
        assertThat(actual).isNotNull();
    }

    @Test
    void 질문으로_댓글_조회() {
        final Question question = questionRepository.save(new Question("제목", "내용"));
        answerRepository.save(new Answer(UserRepositoryTest.JAVAJIGI, question, "답변 내용"));

        List<Answer> answers = answerRepository.findByQuestionId(question.getId());
        assertThat(answers).hasSize(1);
    }

    @Test
    void 사용자로_댓글_조회() {
        final User user = userRepository.save(new User("donghee.han", "password", "donghee", "donghee@slipp.net"));
        answerRepository.save(new Answer(user, QuestionRepositoryTest.Q1, "답변 내용"));

        List<Answer> answers = answerRepository.findByWriterId(user.getId());
        assertThat(answers).hasSize(1);
    }

    @Test
    void 댓글_수정() {
        final Answer answer = answerRepository.save(new Answer(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.Q1, "댓글 작성"));
        answer.setContents("댓글 수정");
        assertThat(answer.getContents()).isEqualTo("댓글 수정");
    }

    @Test
    void 댓글_삭제() {
        final Answer answer = answerRepository.save(new Answer(UserRepositoryTest.JAVAJIGI, QuestionRepositoryTest.Q1, "댓글 작성"));
        final Optional<Answer> find = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(find).isNotEmpty();

        answerRepository.delete(answer);
        answerRepository.flush();

        final Answer expected = answerRepository.findById(answer.getId()).get();
        assertThat(expected.isDeleted()).isTrue();
    }
}
