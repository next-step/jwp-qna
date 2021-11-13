package qna;

import static java.lang.String.*;
import static qna.ErrorMessage.*;

import qna.domain.ContentType;

public class TypeNotFoundException extends RuntimeException {
	public TypeNotFoundException() {
		super("contentType은 필수 입력입니다");
	}
}
