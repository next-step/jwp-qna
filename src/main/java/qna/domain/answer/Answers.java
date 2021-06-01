package qna.domain.answer;

import java.util.List;

import qna.CannotDeleteException;
import qna.domain.user.User;

public class Answers {

	List<Answer> value;

	public Answers(List<Answer> value) {
		this.value = value;
	}

	public void markDeleteWhenUserOwner(User user) throws CannotDeleteException {
		for (Answer answer : this.value) {
			if (answer.isOwner(user) == false) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}
	}
}
