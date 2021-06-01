package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변ID 와 삭제여부로 조회")
    void findByIdAndDeletedFalse() {
        //Given
        User questioner = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");
        User answerer = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        Answer answer = new Answer(answerer, question, "답변입니다.");
        answerRepository.save(answer);

        //When
        Answer savedAnswers = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        //Then
        assertAll(
            () -> assertThat(savedAnswers.getId()).isNotNull(),
            () -> assertThat(savedAnswers).isEqualTo(answer)
        );
    }

    @Test
    @DisplayName("질문ID 와 삭제여부로 조회")
    void findByQuestionIdAndDeletedFalse() {
        //Given
        User questioner = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");
        User answerer = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "훈희가 질문한 내용입니다.", questioner);
        Answer answer = new Answer(answerer, question, "길동이의 답변입니다.");
        answerRepository.save(answer);

        //When
        List<Answer> savedAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        //Then
        assertAll(
            () -> assertThat(savedAnswers).hasSize(1),
            () -> assertThat(savedAnswers).contains(answer)
        );
    }

    @Test
    @DisplayName("writer 연관관계")
    void relation_writer() {
        //Given
        User questioner = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");
        User answerer = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        Answer answer = new Answer(answerer, question, "답변입니다.");
        answerRepository.save(answer);

        //When
        Answer savedAnswer = answerRepository.findById(answer.getId()).get();
        User writer = savedAnswer.getWriter();

        //Then
        assertAll(
            () -> assertThat(writer).isNotNull(),
            () -> assertThat(writer).isEqualTo(answerer)
        );
    }

    @Test
    @DisplayName("question 연관관계")
    void relation_question() {
        //Given
        User questioner = new User("test2", "1234", "정훈희", "jhh992000@gmail.com");
        User answerer = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        Answer answer = new Answer(answerer, question, "답변입니다.");
        answerRepository.save(answer);

        //When
        Answer savedAnswer = answerRepository.findById(answer.getId()).get();
        Question savedQuestion = savedAnswer.getQuestion();

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

}
