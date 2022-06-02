package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
	private final String CAN_NOT_DELETE_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

	@OneToMany(mappedBy = "question")
	private List<Answer> answers;

	protected Answers() {
		answers = new ArrayList<>();
	}

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
		for(Answer answer: answers) {
			validateAuth(answer, loginUser);
		}

		return answers.stream()
			.map(Answer::delete)
			.collect(Collectors.toList());
	}

	private void validateAuth(Answer answer, User loginUser) throws CannotDeleteException {
		if (!answer.isOwner(loginUser)) {
			throw new CannotDeleteException(CAN_NOT_DELETE_MESSAGE);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answers answers1 = (Answers)o;
		return Objects.equals(answers, answers1.answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(answers);
	}
}
