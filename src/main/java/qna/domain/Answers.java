package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
	private List<Answer> answers = new ArrayList<>();

	public Answers() {
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		for (Answer answer : answers) {
			DeleteHistory deleteAnswerHistory = answer.delete(loginUser);
			deleteHistories.add(deleteAnswerHistory);
		}
		return deleteHistories;
	}

	public Integer size() {
		return answers.size();
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
}
