package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.TestAnswerFactory;
import qna.domain.TestQuestionFactory;
import qna.domain.TestUserFactory;
import qna.domain.User;
import qna.repository.QuestionRepository;

@SpringBootTest
public class QnaServiceIntegrationTest {

    @Autowired
    private QnaService qnaService;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 질문자와_답변자_달라_예외_발생하면_질문_삭제여부는_거짓() {
        //given
        User writer = TestUserFactory.create("sanjigi");
        User fakeWriter = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = TestAnswerFactory.create(writer, question);
        Answer answer2 = TestAnswerFactory.create(fakeWriter, question);
        questionRepository.save(question);

        //when
        assertThat(question.isDeleted()).isFalse();
        assertThat(question.answersCount()).isEqualTo(2);
        assertThatThrownBy(() -> qnaService.deleteQuestion(writer, question.getId()))
                .isInstanceOf(CannotDeleteException.class);

        //then
        assertThat(question.isDeleted()).isFalse();
        assertThat(answer1.isDeleted()).isFalse();
    }
}
