package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.userA;
import static qna.domain.UserTest.userB;

@DataJpaTest
public class AnswerTest {
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
        Answer answer = answer(5);
        answer.mappingToWriter(userRepository.save(userB()));
        answer.mappingToQuestion(questionRepository.save(question(6)));

        //when
        Answer result = answerRepository.save(answer);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        //given
        Answer answer = answer(6);
        answer.mappingToWriter(userRepository.save(userB()));
        answer.mappingToQuestion(questionRepository.save(question(6)));

        //when
        Answer save = answerRepository.save(answer);
        Answer result = answerRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(result).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        //given
        Answer answer = answer(6);
        answer.mappingToWriter(userRepository.save(userB()));
        answer.mappingToQuestion(questionRepository.save(question(6)));

        //when
        Answer save = answerRepository.save(answer);
        save.updateAnswerContents("답변 수정!!");
        save.mappingToQuestion(questionRepository.save(question(5)));
        Answer result = answerRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(result.getContents()).isEqualTo("답변 수정!!");
        assertThat(result.getQuestion().getContents()).isEqualTo(question(5).getContents());
    }

    @DisplayName("Update 테스트 2")
    @Test
    void update2() {
        //when
        Answer answer = answerRepository.findById(1L).orElse(null);
        Answer save = answerRepository.save(answer);
        save.updateAnswerContents("답변 수정!!");
        Answer result = answerRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(result.getContents()).isEqualTo("답변 수정!!");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        //given
        Answer answer = answer(6);
        answer.mappingToWriter(userRepository.save(userB()));
        answer.mappingToQuestion(questionRepository.save(question(6)));

        //when
        Answer save = answerRepository.save(answer);
        answerRepository.delete(save);
        Answer found = answerRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isNull();
    }

    @DisplayName("Delete 테스트 2")
    @Test
    void delete2() {
        Answer answer = answerRepository.findById(1L).orElse(null);
        answerRepository.delete(answer);
        Answer result = answerRepository.findById(1L).orElse(null);
        //then
        assertThat(result).isNull();
    }

    @DisplayName("답변 작성자와 로그인 유저가 다르면 CannotDeleteException 에러 발생")
    @Test
    void sameAsUserError() {
        Answer answer = answer(5);
        assertThatThrownBy(()->
                answer.deleteBy(userB()))
                .isInstanceOf(CannotDeleteException.class).hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("답변 작성자와 로그인 유저가 같으면 CannotDeleteException 에러 발생 안함")
    @Test
    void sameAsUser() throws CannotDeleteException {
        Answer answer = answer(5);
        answer.deleteBy(userA());
    }

    static Answer answer(int number) {
        return new Answer(userA(), question(number), "답변" + number);
    }
}
