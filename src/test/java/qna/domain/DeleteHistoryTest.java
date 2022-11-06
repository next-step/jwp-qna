package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@DataJpaTest
class DeleteHistoryTest {

	@Autowired
	DeleteHistoryRepository deleteHistories;

	@Autowired
	AnswerRepository answers;

	@Autowired
	UserRepository users;

	@Autowired
	QuestionRepository questions;

	@Test
	void 질문_답변_삭제_이력에_대한_정보를_DeleteHistory에_저장한다() {
		User 작성자 = 작성자_생성("작성자1");
		Question 질문 = 질문_생성("질문1", 작성자);
		Answer 답변1 = 답변_생성("답변1", 질문, 작성자);
		Answer 답변2 = 답변_생성("답변2", 질문, 작성자);

		DeleteHistories 삭제이력 = DeleteHistory.of(질문);
		deleteHistories.saveAll(삭제이력.getDeleteHistories());

		삭제이력_확인(질문, Lists.newArrayList(답변1, 답변2));
	}

	private void 삭제이력_확인(Question 질문, List<Answer> 답변) {
		Long 작성자_아이디 = 질문.getWriterId();
		List<DeleteHistory> 삭제이력 = deleteHistories.findAllByDeletedById(작성자_아이디);
		질문_삭제됨(삭제이력, 질문.getId());

		답변_삭제됨(삭제이력, 답변);
	}

	private void 질문_삭제됨(List<DeleteHistory> 삭제이력, Long 질문_아이디) {
		assertThat(삭제이력.stream()
			.filter(DeleteHistory::isQuestion)
			.map(DeleteHistory::getContentId))
			.contains(질문_아이디);
	}

	private void 답변_삭제됨(List<DeleteHistory> 삭제이력, List<Answer> 답변) {
		List<Long> 답변_아이디 = 답변.stream().map(Answer::getId).collect(Collectors.toList());
		assertThat(삭제이력.stream()
			.filter(DeleteHistory::isAnswer)
			.map(DeleteHistory::getContentId))
			.containsExactlyInAnyOrderElementsOf(답변_아이디);
	}

	private User 작성자_생성(String 이름) {
		return users.save(UserTest.사용자(이름));
	}

	private Question 질문_생성(String 질문, User 작성자) {
		return questions.save(QuestionTest.질문(질문).writeBy(작성자));
	}

	private Answer 답변_생성(String 제목, Question 질문, User 작성자) {
		return answers.save(AnswerTest.답변(제목, 질문, 작성자));
	}
}
