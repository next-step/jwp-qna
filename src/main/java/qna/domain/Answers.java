package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private List<Answer> answers;

	public Answers() {
		this.answers = new ArrayList<>();
	}

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<DeleteHistory> deleteAnswer(User loginUser) throws CannotDeleteException {
		validateDeleteAnswer(loginUser);
		return answers.stream().peek(answer -> answer.deleted(true))
			.map(answer -> DeleteHistory.ofAnswer(answer.getId(), answer.getWriter()))
			.collect(Collectors.toList());
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	private void validateDeleteAnswer(User loginUser) throws CannotDeleteException {
		if(answers.stream().anyMatch(answer -> !answer.isOwner(loginUser))) {
			throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
		}
	}
}
