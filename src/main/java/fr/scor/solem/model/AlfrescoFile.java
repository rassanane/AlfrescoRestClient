package fr.scor.solem.model;

import java.io.File;
import java.io.InputStream;

/**
 * @author Rachid
 *
 */
public class AlfrescoFile {

	private InputStream stream 			= null;
	private String contentType 			= "";
	private File fileToLoad 			= null;
	private String name 				= "";
	private String foldersToCreate 		= "";
	
	public String getFoldersToCreate() {
		return foldersToCreate;
	}

	public void setFoldersToCreate(String foldersToCreate) {
		this.foldersToCreate = foldersToCreate;
	}

	public AlfrescoFile(){
		this.fileToLoad 	= null;
		this.name 			= null;
	}
	
	public AlfrescoFile(InputStream stream, String contentType){
		this.stream = stream;
		this.contentType = contentType;
	}
	
	public InputStream getStream() {
		return stream;
	}
	
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public File getFileToLoad() {
		return fileToLoad;
	}

	public void setFileToLoad(File fileToLoad) {
		this.fileToLoad = fileToLoad;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
