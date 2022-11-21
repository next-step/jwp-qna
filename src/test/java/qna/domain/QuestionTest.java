package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("질문 엔티티")
public class QuestionTest extends JpaSliceTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    private static String makeStringWithLength(final int length) {
        return String.join("", Collections.nCopies(length, "A"));
    }

    @DisplayName("작성자를 설정하면 작성자의 DB pk를 저장한다.")
    @Test
    void writer() {
        final Question newQuestion = new Question("궁금한 점", "이 있습니다.");
        final Question savedQuestion = questionRepository.save(newQuestion);
        assertThat(savedQuestion.getId()).isNotNull();
    }

    @DisplayName("저장하면 저장한 일시가 생성된다.")
    @Test
    void createdAt() {
        final Question newQuestion = new Question("궁금한 점", "이 있습니다.");
        final Question savedQuestion = questionRepository.save(newQuestion);

        assertAll(
                () -> assertThat(savedQuestion.getCreatedAt()).isNotNull(),
                () -> assertThat(savedQuestion.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("제목은 길이 제약을 넘을 수 없다.")
    @Test
    void maxTitle() {
        final String longTitle = makeStringWithLength(101);
        final Question newQuestion = new Question(longTitle, "제목이 길어!");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> questionRepository.save(newQuestion));
    }

    @DisplayName("제목은 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullTitle(final String title) {
        final Question newQuestion = new Question(title, "제목이 길어!");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> questionRepository.save(newQuestion));
    }

    @DisplayName("질문이 수정되면, 수정일시가 변경된다.")
    @Test
    void updatedDateTime() {
        final Question newQuestion = questionRepository.save(new Question("궁그매요!", "제목에 오타가!?"));
        final LocalDateTime firstUpdatedAt = newQuestion.getUpdatedAt();

        newQuestion.setTitle("궁금해요!");
        final Question updatedQuestion = questionRepository.saveAndFlush(newQuestion);

        assertThat(updatedQuestion.getUpdatedAt()).isNotEqualTo(firstUpdatedAt);
    }

    @DisplayName("작성자를 수정할 수 있다.")
    @Test
    void updateWriter() {
        final User someone = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        final User other = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        final Question question = questionRepository.save(new Question("궁그매요!", "제목에 오타가!?").writeBy(someone));
        assertThat(question.getWriter()).isEqualTo(someone);

        final Question updatedQuestion = questionRepository.saveAndFlush(question.writeBy(other));
        assertThat(updatedQuestion.getWriter()).isEqualTo(other);
    }

    @DisplayName("질문의 작성자를 알 수 있다.")
    @Test
    void isOwner() {
        final User writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        final User other = userRepository.save(new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net"));
        final Question question = questionRepository.save(new Question("궁그매요!", "제목에 오타가!?").writeBy(writer));

        assertAll(
                () -> assertThat(question.isOwner(writer)).isTrue(),
                () -> assertThat(question.isOwner(other)).isFalse()
        );
    }

    @DisplayName("질문에 달린 답변을 알 수 있다.")
    @Test
    void answers() {
        final Question question = questionRepository.save(new Question("어떤 언어가 좋으세요?", ""));
        final long questionId = question.getId();
        final User answerUser = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        final long answer1Id = answerRepository.save(new Answer(answerUser, question, "자바요!")).getId();
        final long answer2Id = answerRepository.save(new Answer(answerUser, question, "코틀린이요!")).getId();
        entityManager.clear();
        final Question savedQuestion = questionRepository.findByIdAndDeletedFalse(questionId).get();

        final List<Answer> answersOfQuestion = savedQuestion.getAnswers();

        assertThat(answersOfQuestion)
                .extracting("id")
                .containsExactlyInAnyOrder(answer1Id, answer2Id);
    }

    @DisplayName("삭제된 질문은 조회되지 않는다.")
    @Test
    void deletedAnswer() {
        final Question question = questionRepository.save(new Question("어떤 언어가 좋으세요?", ""));
        final long questionId = question.getId();
        final User answerUser = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        final Answer answer = new Answer(answerUser, question, "자바요!");
        answer.setDeleted(true);
        answerRepository.save(answer);
        entityManager.clear();
        final Question savedQuestion = questionRepository.findByIdAndDeletedFalse(questionId).get();

        final List<Answer> answersOfQuestion = savedQuestion.getAnswers();

        assertThat(answersOfQuestion).isEmpty();
    }
}
