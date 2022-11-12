package qna.error;

public enum ErrorMessage {
    NO_AUTH_DELETE_QUESTION("질문을 삭제할 권한이 없습니다."),
    CANT_DELETE_ANSWER_IF_EXISTS_OTHER_WRITER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    private String message;

    ErrorMessage(String message){
        this.message = message;
    }

    public String message(){
        return this.message;
    }
}
