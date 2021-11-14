package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @Test
    @DisplayName("저장됐는지 확인")
    void 저장() {
        // Given
        Question expected = new Question("new title", "new contents").writeBy(UserTest.JENNIE);
        
        // When
        Question saveQuestion = questions.save(expected);
        
        //Then
        assertAll(() -> assertThat(saveQuestion).isEqualTo(expected), () -> assertThat(saveQuestion.getId()).isNotNull(), () -> assertThat(saveQuestion.getId()).isEqualTo(expected.getId()));
    }

    @Test
    @DisplayName("아이디로 조회")
    void 아이디로_조회() {
        // Given
        Question expected = new Question("new title", "new contents").writeBy(UserTest.JENNIE);
        questions.save(expected);

        // When
        Optional<Question> actual = questions.findById(expected.getId());
        
        // Then
        assertAll(() -> assertThat(actual.isPresent()).isTrue(), 
                 () -> assertThat(actual.orElse(null)).isEqualTo(expected));
    }

    @Test
    @DisplayName("질문을 삭제하면 삭제 상태로 변경")
    void 삭제_상태_확인() throws CannotDeleteException {
        // Given
        Question expected = new Question("질문할게요~", "이럴땐 어떻게 해야하나요?").writeBy(UserTest.JENNIE);
        questions.save(expected);
        
        // When
        expected.delete(UserTest.JENNIE);
        
        // Then
        assertThat(questions.findById(expected.getId()).get().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제시 삭제 이력 확인")
    void 삭제_이력_확인() throws CannotDeleteException {
        // Given
        Question expected = new Question("질문할게요~", "이럴땐 어떻게 해야하나요?").writeBy(UserTest.JENNIE);
        questions.save(expected);
        
        // When
        DeleteHistories deleteHistories = expected.delete(UserTest.JENNIE);
        
        // Then
        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    @DisplayName("질문을 삭제하면 답변도 삭제 상태로 변경")
    void 삭제시_답변_삭제_확인() throws CannotDeleteException {
        // Given
        Question question = new Question("질문할게요~", "이럴땐 어떻게 해야하나요?").writeBy(UserTest.JENNIE);
        questions.save(question);
        Answer answer = new Answer(UserTest.JENNIE, question, "이렇게 한번 해보세요!!");
        answers.save(answer);
        question.addAnswer(answer);
        
        // When
        question.delete(UserTest.JENNIE);
        
        // Then
        assertThat(question.getAnswers().getAnswers().get(0).isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 사람의 질문은 삭제 불가능")
    void 삭제시_로그인_계정_확인() {
        // Given
        Question question = new Question("질문할게요~", "이럴땐 어떻게 해야하나요?").writeBy(UserTest.JENNIE);
        questions.save(question);
        Answer answer = new Answer(UserTest.JENNIE, question, "이렇게 한번 해보세요!!");
        answers.save(answer);
        question.addAnswer(answer);
        
        // When, Then
        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class);
    }
    
    @Test
    @DisplayName("질문에 다른사람의 답변이 있으면 삭제 불가능")
    void 삭제시_다른사람_답변_여부_확인() {
        // Given
        Question question = new Question("질문할게요~", "이럴땐 어떻게 해야하나요?").writeBy(UserTest.JENNIE);
        questions.save(question);
        Answer answer = new Answer(UserTest.SOOKI, question, "이렇게 한번 해보세요!!");
        answers.save(answer);
        question.addAnswer(answer);
        
        // When, Then
        assertThatThrownBy(() -> question.delete(UserTest.JENNIE)).isInstanceOf(CannotDeleteException.class);
    }
    
    @Test
    @DisplayName("답변이 질문에 잘 달렸나 확인")
    void 질문_답변_매핑_확인() {
        // Given
        Question question = new Question("질문할게요~", "이럴땐 어떻게 해야하나요?").writeBy(UserTest.JENNIE);
        questions.save(question);
        Answer firstAnswer = new Answer(UserTest.SOOKI, question, "이렇게 한번 해보세요!!");
        Answer secondAnswer = new Answer(UserTest.JENNIE, question, "감사합니다~!");
        answers.save(firstAnswer);
        answers.save(secondAnswer);
        
        // When
        question.addAnswer(firstAnswer);
        question.addAnswer(secondAnswer);
        
        // Then
        Question actual = questions.findById(question.getId()).get();
        Answers actualAnswers = actual.getAnswers();
        assertThat(actualAnswers.countAnswers()).isEqualTo(2);
    }

}
