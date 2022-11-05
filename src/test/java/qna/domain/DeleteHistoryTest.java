package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
	void 질문_삭제_이력에_대한_정보를_DeleteHistory에_저장한다() {
		User 작성자 = 작성자_생성("작성자1");
		Question 질문 = 질문_생성("질문1", 작성자);

		DeleteHistory 삭제이력 = DeleteHistory.ofQuestion(질문, 작성자);
		deleteHistories.save(삭제이력);

		삭제이력_확인(작성자, 질문);
	}

	@Test
	void 답변_삭제_이력에_대한_정보를_DeleteHistory에_저장한다() {
		User 작성자 = 작성자_생성("작성자1");
		Question 질문 = 질문_생성("질문1", 작성자);

		Answer 답변 = 답변_생성("답변1", 질문, 작성자);

		DeleteHistory 삭제이력 = DeleteHistory.ofAnswer(답변, 작성자);
		deleteHistories.save(삭제이력);

		삭제이력_확인(작성자, 답변);
	}

	private void 삭제이력_확인(User 작성자, Question 질문) {
		assertThat(deleteHistories.findAllByDeletedById(작성자.getId())
			.stream()
			.filter(DeleteHistory::isQuestion)
			.map(DeleteHistory::getContentId))
			.contains(질문.getId());
	}

	private void 삭제이력_확인(User 작성자, Answer 답변) {
		assertThat(deleteHistories.findAllByDeletedById(작성자.getId())
			.stream()
			.filter(DeleteHistory::isAnswer)
			.map(DeleteHistory::getContentId))
			.contains(답변.getId());
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
