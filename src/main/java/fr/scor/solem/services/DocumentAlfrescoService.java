package fr.scor.solem.services;

import fr.scor.solem.model.AlfrescoFile;
import fr.scor.solem.model.DocumentList;

/**
 * @author Rachid
 *
 */
public interface DocumentAlfrescoService {

	public AlfrescoFile showDocument(String fileId);
	
	public String loadDocument(AlfrescoFile fileToUpload);
	
	public String deleteDocument(String fileId);
	
	public DocumentList searchDocuments(String keyword);
	
	public DocumentList myDocuments();
	
}
