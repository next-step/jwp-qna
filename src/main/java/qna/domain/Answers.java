package qna.domain;

import qna.CannotDeleteException;

import java.util.List;

public class Answers {
	private final List<Answer> answers;

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public void validateAnswersOwner(User loggedInUser) throws CannotDeleteException {
		for (Answer answer : this.answers) {
			if (!answer.isOwner(loggedInUser)) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}
	}
}
