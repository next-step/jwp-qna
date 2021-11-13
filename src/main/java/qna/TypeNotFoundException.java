package qna;

import static java.lang.String.*;
import static qna.ErrorMessage.*;

import qna.domain.ContentType;

public class TypeNotFoundException extends RuntimeException {
	public TypeNotFoundException() {
		super(format(TYPE_NOT_FOUND,ContentType.valuesString()));
	}
}
