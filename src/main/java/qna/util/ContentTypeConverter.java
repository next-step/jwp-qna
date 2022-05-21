package qna.util;

import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import qna.domain.ContentType;

@Converter
public class ContentTypeConverter implements AttributeConverter<ContentType, String> {
    @Override
    public String convertToDatabaseColumn(final ContentType contentType) {
        if (contentType == null) {
            return null;
        }
        return contentType.getType();
    }

    @Override
    public ContentType convertToEntityAttribute(final String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return null;
        }
        return Stream.of(ContentType.values())
                .filter(e -> e.getType().equals(contentType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
