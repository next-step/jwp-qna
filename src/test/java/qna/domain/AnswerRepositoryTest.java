package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 새로운_객체가_insert_된_후에_조회되어야_한다() {
        // given
        final Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer");

        // when
        answerRepository.save(answer);

        // then
        assertThat(answer.getId()).isNotNull();
        assertThat(answer).isEqualTo(answerRepository.findById(answer.getId()).get());
    }

    @Test
    void 객체를_수정한_뒤_조회하면_수정된_내용이_반영되어있어야_한다() {
        // given
        final Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer");
        answerRepository.save(answer);
        answerRepository.flush();
        final Question newQuestion = QuestionTest.Q2;

        // when
        answer.toQuestion(newQuestion);

        // then
        assertThat(answer.getQuestionId()).isEqualTo(newQuestion.getId());
        assertThat(answer.getQuestionId()).isEqualTo(answerRepository.findById(answer.getId()).get().getQuestionId());
    }

    @Test
    void 질문글의_아이디로_삭제되지_않은_답변들을_조회할_수_있어야_한다() {
        // given
        questionRepository.save(QuestionTest.Q1);
        final Answer answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer1");
        final Answer answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "answer2");
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        // when
        final List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        // then
        assertThat(answers.containsAll(Arrays.asList(answer1, answer2))).isTrue();
    }

    @Test
    void 삭제되지_않은_답변을_답변_아이디로_조회할_수_있어야_한다() {
        // given
        final Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer1");
        answerRepository.save(answer);

        // when
        final Answer selected = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        // then
        assertThat(selected).isNotNull();
        assertThat(selected).isEqualTo(answer);
    }
}
