package qna;

import qna.domain.User;

public class CannotDeleteException extends Exception {

    private static final long serialVersionUID = 1L;

    public CannotDeleteException(User answerWriter) {
        super(String.format(
                "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다. [댓글작성자: %s]",
                answerWriter.getUserId()));
    }

}
