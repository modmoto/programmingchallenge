package domain.documents;

import domain.ValidationResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentId {
    private String documentId;

    private DocumentId(String documentId) {
        this.documentId = documentId;
    }

    public static ValidationResult<DocumentId> create(String id) {
        String regex = "^[a-zA-Z0-9]{20}+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        if (!matcher.matches()) {
            return new ValidationResult<>(DocumentErrors.DocumentIdHasToBeA20CharacterAlphanumericString());
        }

        return new ValidationResult<>(new DocumentId(id));
    }

    public String getDocumentId() {
        return documentId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != DocumentId.class) return false;
        DocumentId documentIdParsed = (DocumentId) obj;
        return documentIdParsed.getDocumentId().equals(documentId);
    }
}
