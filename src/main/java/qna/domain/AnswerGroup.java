package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class AnswerGroup {

	@OneToMany(mappedBy = "question", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Answer> answers = new ArrayList<>();

	protected AnswerGroup() {
	}

	public static AnswerGroup generate() {
		return new AnswerGroup();
	}

	public int size() {
		return answers.size();
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public boolean containsAll(AnswerGroup answerGroup) {
		return answers.containsAll(answerGroup.answers);
	}

	public boolean isEmpty() {
		return answers.isEmpty();
	}

	public void validateIsSameWithUserAndWriter(User loginUser) throws CannotDeleteException {
		for (Answer answer : answers) {
			answer.validateIsOwner(loginUser);
		}
	}

	public void deleteAll() {
		answers.forEach(Answer::delete);
	}

	public List<DeleteHistory> generateDeleteHistoryAllOfAnswers() {
		return answers.stream()
			.map(answer -> new DeleteHistory(
				ContentType.ANSWER,
				answer.id(),
				answer.writer(),
				LocalDateTime.now()))
			.collect(Collectors.toList());
	}

	public List<Answer> answers() {
		return answers;
	}
}
