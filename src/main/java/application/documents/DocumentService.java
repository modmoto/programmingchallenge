package application.documents;

import domain.RepositoryResult;
import domain.ValidationResult;
import domain.documents.Document;
import domain.documents.DocumentId;
import domain.documents.DocumentRepository;
import domain.documents.DocumentType;

import javax.ws.rs.NotFoundException;

public class DocumentService {
    private DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document CreateDocument(CreateDocumentCommand command) {
        DocumentType documentType = DocumentType.create(command.getDocumentType()).getEntity();
        DocumentId documentId = DocumentId.create(command.getDocumentId()).getEntity();
        Document document = Document.create(documentId, command.getContent(), documentType).getEntity();

        RepositoryResult<Document> result = documentRepository.insert(document);
        return result.getEntity();
    }

    public void UpdateDocument(UpdateDocumentCommand command) {
        DocumentId documentId = parseDocumentIdAndThrowIfInvalid(command.getDocumentId());
        DocumentType documentType = DocumentType.create(command.getDocumentType()).getEntity();
        Document document = documentRepository.get(documentId).getEntity();

        ValidationResult<Document> documentValidationResult = document.updateDocument(command.getDocumentContent(), documentType);

        Document documentUpdated = documentValidationResult.getEntity();
        documentRepository.update(documentUpdated);
    }

    public Document GetDocument(GetDocumentQuerry command) {
        DocumentId documentId = parseDocumentIdAndThrowIfInvalid(command.getDocumentId());

        return documentRepository.get(documentId).getEntity();
    }

    public Document DeleteDocument(DeleteDocumentCommand command) {
        DocumentId documentId = parseDocumentIdAndThrowIfInvalid(command.getDocumentId());
        Document document = documentRepository.get(documentId).getEntity();

        ValidationResult<Document> deleteResult = document.delete();

        var result = documentRepository.insert(deleteResult.getEntity());
        return result.getEntity();
    }

    private DocumentId parseDocumentIdAndThrowIfInvalid(String documentId) {
        ValidationResult<DocumentId> documentIdResult = DocumentId.create(documentId);
        if (documentIdResult.failed()) throw new NotFoundException(documentId);
        return documentIdResult.getEntity();
    }
}
