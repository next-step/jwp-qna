package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private List<Answer> answerList = new ArrayList<>();

	protected Answers() {
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void add(Answer answer) {
		answerList.add(answer);
	}

	public DeleteHistory delete(Answer answer, User user) throws CannotDeleteException {
		answer.deleteByUser(user);
		answerList.remove(answer);
		return new DeleteHistory(ContentType.ANSWER, answer.getId(), user);
	}

	public void addAll(List<Answer> answers) {
		answerList.addAll(answers);
	}

	public DeleteHistories deleteAll(User user) throws CannotDeleteException {
		DeleteHistories deleteHistories = new DeleteHistories();
		for (Answer answer : answerList) {
			answer.deleteByUser(user);
			deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), user));
		}
		return deleteHistories;
	}
}