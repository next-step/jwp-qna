package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AnswerRepositoryTest extends QnATest {

    @Test
    void 삭제되지_않은_대상건중_한건을_true변경시_제외한_대상건만_조회() {
        //given
        User user = createUser();
        Question question = createQuestion(user, TITLE_1, CONTENTS_1);
        createAnswer(user, question, ANSWER_1);
        createAnswer(user, question, ANSWER_2);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        Answer answer = answers.get(0);
        answer.setDeleted(true);

        List<Answer> expectedAnswers = answerRepository.findByDeletedFalse();

        // then
        assertThat(expectedAnswers.size() == 1).isTrue();
    }

    @Test
    void 여러건을_저장할때_리스트에_데이터를_담는경우_동일한_객체라도_모두_저장된다() {
        User user = createUser();
        Question question = createQuestion(user, TITLE_1, CONTENTS_1);
        createAnswer(user, question, ANSWER_1);
        createAnswer(user, question, ANSWER_1);
        createAnswer(user, question, ANSWER_2);

        List<Answer> findAnswer = answerRepository.findAll();
        assertThat(findAnswer.size()).isEqualTo(3);
    }

}
