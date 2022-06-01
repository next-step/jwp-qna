package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User writer;
    private Question question;
    private Answer answer1, answer2;

    @BeforeEach
    void init() {
        //given
        writer = userRepository.save(UserFixtures.JAVAJIGI);
        question = questionRepository.save(QuestionFixtures.createDefaultByUser(writer));
        answer1 = answerRepository.save(AnswerFixtures.create(writer, question, "Answer Contents1"));
        answer2 = answerRepository.save(AnswerFixtures.create(writer, question, "Answer Contents2"));
    }

    @Test
    @DisplayName("답변 저장 및 값 비교 테스트")
    void save() {
        //when
        final Answer actual = answerRepository.save(answer1);
        question.addAnswer(actual);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(answer1.getWriter()),
                () -> assertThat(actual.getQuestion()).isEqualTo(answer1.getQuestion()),
                () -> assertThat(actual.getContents()).isEqualTo(answer1.getContents())
        );
    }

    @Test
    @DisplayName("질문 작성자 id로 삭제되지 않은 답변 목록 조회")
    void findByQuestionIdAndDeletedFalse() throws CannotDeleteException {
        //when
        answer1.delete(writer);
        List<Answer> founds = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestion().getId());

        //then
        assertAll(
                () -> assertThat(founds).hasSize(1),
                () -> assertThat(founds).doesNotContain(answer1),
                () -> assertThat(founds).containsExactly(answer2)
        );
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 목록 조회")
    void findByIdAndDeletedFalse() throws CannotDeleteException {
        //when
        answer1.delete(writer);
        Optional<Answer> foundsAnswer1 = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        Optional<Answer> foundsAnswer2 = answerRepository.findByIdAndDeletedFalse(answer2.getId());

        //then
        assertAll(
                () -> assertThat(foundsAnswer1.isPresent()).isFalse(),
                () -> assertThat(foundsAnswer2.isPresent()).isTrue()
        );
    }

    @Test
    @DisplayName("질문 작성자 일치하는지 확인")
    void isOwner() {
        //then
        assertAll(
                () -> assertThat(answer1.isOwner(writer)).isTrue(),
                () -> assertThat(answer2.isOwner(writer)).isTrue()
        );
    }

    @Test
    @DisplayName("질문 작성자 불일치하는지 확인")
    void isNotOwner() {
        //when
        User anotherWriter = userRepository.save(UserFixtures.SANJIGI);

        //then
        assertAll(
                () -> assertThat(answer1.isOwner(anotherWriter)).isFalse(),
                () -> assertThat(answer2.isOwner(anotherWriter)).isFalse()
        );
    }
}
