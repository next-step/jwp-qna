package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.answer;
import static qna.domain.UserTest.userA;
import static qna.domain.UserTest.userB;

@DataJpaTest
public class QuestionTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        //given
        Question question = question(5);
        question.mappingToWriter(userRepository.save(userA()));
        question.mappingToAnswer(answerRepository.save(answer(5)));

        //when
        Question result = questionRepository.save(question);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getWriter()).isNotNull(),
                () -> assertThat(result.getAnswers()).isNotEmpty()
        );
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        //given
        Question question = question(5);
        question.mappingToWriter(userRepository.save(userA()));
        question.mappingToAnswer(answerRepository.save(answer(5)));

        //when
        Question expected = questionRepository.save(question);
        Question result = questionRepository.findById(expected.getId()).orElse(null);

        //then
        assertAll(
                () -> assertThat(result).isEqualTo(expected),
                () -> assertThat(result.getAnswers().get(0).getContents()).isEqualTo("답변5")
        );

    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        //given
        Question question = question(5);
        question.mappingToWriter(userRepository.save(userA()));
        question.mappingToAnswer(answerRepository.save(answer(5)));

        //when
        Question expected = questionRepository.save(question);
        expected.updateQuestionContents("질문 수정!!");
        expected.mappingToWriter(userB());
        Question result = questionRepository.findById(expected.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(result.getContents()).isEqualTo("질문 수정!!");
        assertThat(result.getWriter().getUserId()).isEqualTo("javajigi");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        //given
        Question question = question(5);
        question.mappingToWriter(userRepository.save(userA()));
        question.mappingToAnswer(answerRepository.save(answer(5)));

        //when
        Question save = questionRepository.save(question);
        questionRepository.delete(save);
        Question found = questionRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isNull();
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete2() {
        Question question = questionRepository.findById(3L).orElse(null);
        questionRepository.delete(question);
        Question result = questionRepository.findById(3L).orElse(null);
        //then
        assertThat(result).isNull();
    }

    @DisplayName("질문 작성자와 로그인 유저가 다르면 CannotDeleteException 에러 발생")
    @Test
    void sameAsUserError() {
        Question question = question(5).writeBy(userA());
        assertThatThrownBy(()->
                question.delete(userB()))
                    .isInstanceOf(CannotDeleteException.class).hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("질문 작성자와 로그인 유저가 같으면 CannotDeleteException 에러 발생 안함")
    @Test
    void sameAsUser() {
        Question question = question(5).writeBy(userA());
        question.delete(userA());
    }

    static Question question(int number) {
        return new Question("질문 타이틀" + number, "질문" + number);
    }
}
