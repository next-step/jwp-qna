package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answers = new ArrayList<>();

	public void add(Answer answer) {
		answers.add(answer);
	}

	public boolean contains(Answer answer) {
		return answers.contains(answer);
	}

	public void validateAnswersWriter(User loginUser) throws CannotDeleteException {
		if (answers.stream().anyMatch(answer -> !answer.isOwner(loginUser))) {
			throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
		}
	}

	public void delete(User loginUser) {
		answers.stream().forEach(answer -> answer.delete(loginUser));
	}

	public List<DeleteHistory> getDeleteHistories() {
		List<DeleteHistory> deleteHistories = answers.stream()
			.flatMap(x -> x.getDeleteHistories().stream())
			.collect(Collectors.toList());

		return deleteHistories;
	}
}
