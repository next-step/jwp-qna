package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionRepositoryTest extends QnATest {

    @Test
    @DisplayName("데이터가 정상적으로 저장되는지 확인")
    void save() {
        // given
        Question question = createQuestions(createUsers().get(0), TITLE_1);

        // when
        Question findQuestion = questionRepository.findById(question.getId()).get();

        // then
        assertThat(findQuestion.equalsTitleAndContentsAndNotDeleted(question)).isTrue();
    }

    @Test
    @DisplayName("전체 데이터수 - (삭제여부 true) = 전체 건수 검증")
    void given_questions_when_changeDeleteToTrue_then_excludeDeleteIsTrue() {

        // given
        createQuestions(createUsers().get(0), TITLE_1, TITLE_2);

        // when
        List<Question> questions = questionRepository.findAll();
        Question question = questions.get(0);
        question.setDeleted(true);

        // then
        List<Question> expectedQuestion = questionRepository.findByDeletedFalse();
        assertThat(expectedQuestion.size()).isEqualTo(1);
        assertThat(expectedQuestion.get(0)).isNotEqualTo(question);
    }



	@Test
	void 타이틀이름으로_조회한_데이터의_답글수_확인() {

        // given
        User user = createUsers().get(0);
        Question questions = createQuestions(user, TITLE_1);
        List<Answer> answers = createAnswers(user, createQuestions(user, TITLE_1));

        // when
        Question question = questionRepository.findById(questions.getId()).get();
        List<Answer> answers1 = question.getAnswers();

        // then
		assertThat(answers1.size()).isEqualTo(answers.size());
	}



}
