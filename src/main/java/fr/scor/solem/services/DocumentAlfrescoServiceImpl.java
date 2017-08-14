package fr.scor.solem.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.util.Maps;

import fr.scor.solem.model.AlfrescoFile;
import fr.scor.solem.model.DocumentEntry;
import fr.scor.solem.model.DocumentList;

/**
 * @author Rachid
 *
 */
public class DocumentAlfrescoServiceImpl extends AlfrescoServiceImpl implements DocumentAlfrescoService {

    
    private static final String NODES_URL_DOC 		= "/public/alfresco/versions/1/nodes/";
    
    private static final String QUERIES_URL_DOC 	= "/public/alfresco/versions/1/queries/nodes?term=";
	
    private static final String MY_DOCS 			= "/public/alfresco/versions/1/nodes/-my-/children";
	
    private static String homeNetwork				= null;
    
	/* (non-Javadoc)
	 * 
	 * Retrieve document by the id
	 * 
	 * @see fr.scor.solem.services.DocumentAlfrescoService#showDocument(java.lang.String)
	 */
	@Override
	public AlfrescoFile showDocument(String fileId) {
		
		try {
			
			if(homeNetwork==null){
				homeNetwork = getHomeNetwork();
			}
	
	        GenericUrl docUrl = new GenericUrl(getAlfrescoAPIUrl() +
	                                             homeNetwork +
	                                             NODES_URL_DOC + fileId + "/content?attachment=false");
	        
	        HttpRequest request = getRequestFactory().buildGetRequest(docUrl);
	        
	        //request.execute().parseAs(SiteList.class);
	                                
            InputStream stream = request.execute().getContent();
            String type = request.execute().getContentType();
            
            AlfrescoFile alfrescoFile = new AlfrescoFile(stream, type);
	        	        
	        return alfrescoFile;
	        
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
        return null;
	}
    
	/* (non-Javadoc)
	 * 
	 * Load document on Alfresco repository
	 * 
	 * @see fr.scor.solem.services.DocumentAlfrescoService#loadDocument(fr.scor.solem.model.AlfrescoFile)
	 */
	@Override
	public String loadDocument(AlfrescoFile fileToUpload) {
		
//      //Test création fichier text vide : OK  ########################################################
//      GenericData data = new GenericData();
//      data.put("name", "My text file.txt");
//      data.put("nodeType", "cm:content");
//      JsonHttpContent content = new JsonHttpContent(new JacksonFactory(), data);	        
//      HttpRequest request = getRequestFactory().buildPostRequest(docUrl, content);	        

      
      
      
//      //Test création fichier text vide : OK ########################################################
//      String test = "{\"name\":\"My text file.txt\",\"nodeType\":\"cm:content\"}";
//      HttpRequest request = getRequestFactory().buildPostRequest(docUrl, new ByteArrayContent("application/json", test.getBytes()));

		try {
			
				if(homeNetwork==null){
					homeNetwork = getHomeNetwork();
				}
	
		        GenericUrl docUrl = new GenericUrl(getAlfrescoAPIUrl() +
		                homeNetwork +
		                NODES_URL_DOC + 
		                
		                //The folder id in solem site
		                //"6d258469-b517-4270-b37e-d27d164825a3" +
		                
		                //we can also use this aliases : -my- -shared- -root-
		                "-my-" +
		                
		                "/children"
		                );
				
		        File file = fileToUpload.getFileToLoad();
		        
		        MimetypesFileTypeMap  mimeTypesMap = new MimetypesFileTypeMap();
		        FileContent fileContent = new FileContent(mimeTypesMap.getContentType(file), file);
		
				Map<String, String> parameters = Maps.newHashMap();
				if(fileToUpload.getName()!=null && !fileToUpload.getName().equals("")){
					parameters.put("name", fileToUpload.getName());
				}
				if(fileToUpload.getFoldersToCreate()!=null && !fileToUpload.getFoldersToCreate().equals("")){
					parameters.put("relativePath", fileToUpload.getFoldersToCreate());
				}
		
				MultipartContent content = new MultipartContent().setMediaType(
				        new HttpMediaType("multipart/form-data")
				                .setParameter("boundary", "__END_OF_PART__"));
				for (String name : parameters.keySet()) {
				    MultipartContent.Part part = new MultipartContent.Part(
				            new ByteArrayContent(null, parameters.get(name).getBytes()));
				    part.setHeaders(new HttpHeaders().set(
				            "Content-Disposition", String.format("form-data; name=\"%s\"", name)));
				    content.addPart(part);
				}				
								
				MultipartContent.Part part = new MultipartContent.Part(fileContent);
				part.setHeaders(new HttpHeaders().set(
				        "Content-Disposition", 
				        String.format("form-data; name=\"filedata\"; filename=\"%s\"", fileToUpload.getFileToLoad().getName())));
				content.addPart(part);
	
				HttpRequest request = getRequestFactory().buildPostRequest(docUrl, content);
								
				DocumentEntry doc = request.execute().parseAs(DocumentEntry.class);
								
				return doc.entry.name;
				
		}catch (IOException e) {
			   e.printStackTrace();
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * 
	 * Delete document by id
	 * 
	 * @see fr.scor.solem.services.DocumentAlfrescoService#deleteDocument(java.lang.String)
	 */
	@Override
	public String deleteDocument(String fileId) {
		
		try {
			
			if(homeNetwork==null){
				homeNetwork = getHomeNetwork();
			}
	
	        GenericUrl docUrl = new GenericUrl(getAlfrescoAPIUrl() +
	                                             homeNetwork +
	                                             NODES_URL_DOC + fileId + "?permanent=false");
	        
	        HttpRequest request = getRequestFactory().buildDeleteRequest(docUrl);
	        
	        HttpResponse reponse = request.execute();
	        	        
	        return reponse.parseAsString();
	        
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see fr.scor.solem.services.DocumentAlfrescoService#searchDocuments(java.lang.String)
	 * 
	 * Search in Solem My files directory by keyword
	 */
	@Override
	public DocumentList searchDocuments(String keyword) {
		
		DocumentList documentList = null;
		
		try {
			
			if(homeNetwork==null){
				homeNetwork = getHomeNetwork();
			}
	
	        GenericUrl docsUrl = new GenericUrl(getAlfrescoAPIUrl() +
	                                             homeNetwork +
	                                             QUERIES_URL_DOC + keyword + "*&rootNodeId=-my-");
	        
	        HttpRequest request = getRequestFactory().buildGetRequest(docsUrl);
	        	        
	        documentList = request.execute().parseAs(DocumentList.class);
	        
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return documentList;
	}
	
	/* (non-Javadoc)
	 * @see fr.scor.solem.services.DocumentAlfrescoService#myDocuments()
	 * 
	 * Display the list of files in Solem my files directory
	 * 
	 */
	@Override
	public DocumentList myDocuments() {
		
		DocumentList documentList = null;
		
		try {
			
			if(homeNetwork==null){
				homeNetwork = getHomeNetwork();
			}
	
	        GenericUrl docsUrl = new GenericUrl(getAlfrescoAPIUrl() +
	                                             homeNetwork +
	                                             MY_DOCS);
	        
	        HttpRequest request = getRequestFactory().buildGetRequest(docsUrl);
	        	        
	        documentList = request.execute().parseAs(DocumentList.class);
	        
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return documentList;
	}
	
}
