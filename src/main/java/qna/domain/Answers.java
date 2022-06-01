package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question")
	private List<Answer> answers = new ArrayList<>();

	protected Answers() {
		answers = new ArrayList<>();
	}

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public void delete(User loginUser) throws CannotDeleteException {
		for (Answer answer : answers) {
			if (!answer.isOwner(loginUser)) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}
	}
}
