package qna.view;

public enum Messages {

    INVALID_AUTHORIZATION("질문을 삭제할 권한이 없습니다."),
    CONTAINS_ANSWER_WRITTEN_ANOTHER_USER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    WRITER_DOES_NOT_MATCH("질문 %d의 작성자 %s 가 현 로그인 작성자 %s 와 일치하지 않습니다.");

    String msg;

    Messages(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg.toString();
    }
}
