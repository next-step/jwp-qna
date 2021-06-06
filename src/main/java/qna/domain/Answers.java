package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private final List<Answer> answers = new ArrayList<>();

	public List<Answer> getAnswers() {
		return answers;
	}

	public void addAnswer(Answer answer) {
		answers.add(answer);
	}

	private void validateAnswersOwner(User loggedInUser) throws CannotDeleteException {
		for (Answer answer : this.answers) {
			if (!answer.isOwner(loggedInUser)) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}
	}

	public List<DeleteHistory> deleteAnswers(User loggedInUser) throws CannotDeleteException {
		validateAnswersOwner(loggedInUser);
		return answers.stream().map(Answer::delete)
				.collect(Collectors.toList());
	}
}
