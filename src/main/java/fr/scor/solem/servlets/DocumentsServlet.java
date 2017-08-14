package fr.scor.solem.servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.api.client.util.IOUtils;

import fr.scor.solem.model.AlfrescoFile;
import fr.scor.solem.model.DocumentEntry;
import fr.scor.solem.model.DocumentList;
import fr.scor.solem.services.DocumentAlfrescoService;
import fr.scor.solem.services.DocumentAlfrescoServiceImpl;

/**
 * @author Rachid
 *
 */
public class DocumentsServlet extends HttpServlet {
 
	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  String action = request.getParameter("action");
	  String fileId = request.getParameter("fileId");
	  String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";
	  
      //Document
	  DocumentAlfrescoService service = new DocumentAlfrescoServiceImpl();
	  
	  if(action.equals("show")){
		  
		  AlfrescoFile alfrescoFile = service.showDocument(fileId);
	      
		  response.setContentType(alfrescoFile.getContentType());
		  
		  OutputStream outputStream = response.getOutputStream();
		  
		  IOUtils.copy(alfrescoFile.getStream(), outputStream );
		  
	  }
	  
	  if(action.equals("del")){
		  
		  String strResponse = service.deleteDocument(fileId);
		  
	      response.setContentType("text/html");

	      PrintWriter out = response.getWriter();
          out.println("<html><head><style>body {background-color: #FAFAFA; color: #135EAB; font-family:Arial;}a:link {color: #135EAB;text-decoration:underline;}a:visited {color: #135EAB;}</style></head><body><br><br>Le document a été supprimé avec succes.<br><br><br><br><a href='/Alfresco-Solem-Prototype/'>Retour</a></body></html>");
		  
	  }
	  
	  if(action.equals("search")){
		  
		  DocumentList documentList = service.searchDocuments(keyword);
		  
		  StringBuffer strDocumentsList = new StringBuffer("<html><head><style>body {background-color: #FAFAFA; color: #135EAB; font-family:Arial;}a:link {color: #135EAB;text-decoration:underline;}a:visited {color: #135EAB;}</style></head><body><br>Liste des documents : <br><br>");
		  
	      for (DocumentEntry documentEntry : documentList.list.entries) {
	    	  if(documentEntry.entry.isFile){
	    		  strDocumentsList.append("<a href='/Alfresco-Solem-Prototype/documents?action=show&fileId=" + documentEntry.entry.id + "' target='_blanck'>"+documentEntry.entry.name+"</a><br>");
	    	  }
	      }

	      strDocumentsList.append("<br><br><br><br><a href='/Alfresco-Solem-Prototype/'>Retour</a></body></html>");
	      
	      response.setContentType("text/html");

	      PrintWriter out = response.getWriter();
	      out.println(strDocumentsList);
		  
	  }
	  
	  if(action.equals("myDocs")){
		  
		  DocumentList documentList = service.myDocuments();
		  
		  StringBuffer strDocumentsList = new StringBuffer("<html><head><style>body {background-color: #FAFAFA; color: #135EAB; font-family:Arial;}a:link {color: #135EAB;text-decoration:underline;}a:visited {color: #135EAB;}</style></head><body><br>Liste des documents dans mon répertoire : <br><br>");
		  
	      strDocumentsList.append("<TABLE width=\"900px\" style=\"border-collapse: separate; border-spacing: 10px;\">");
	      for (DocumentEntry documentEntry : documentList.list.entries) {
	    	  if(documentEntry.entry.isFile){
	    	      strDocumentsList.append("<TR><TD width=\"300px\">" + documentEntry.entry.name + "</TD><TD width=\"300px\"><a href='/Alfresco-Solem-Prototype/documents?action=show&fileId=" + documentEntry.entry.id + "' target='_blanck'>Afficher</a></TD><TD width=\"300px\"><a href='/Alfresco-Solem-Prototype/documents?action=del&fileId=" + documentEntry.entry.id + "' target='_blanck'>Supprimer</a></TD></TR>");
	    	  }
	      }      
	      strDocumentsList.append("</TABLE>");

		  
		  
	      strDocumentsList.append("<br><br><br><br><a href='/Alfresco-Solem-Prototype/'>Retour</a></body></html>");
	      
	      response.setContentType("text/html");

	      PrintWriter out = response.getWriter();
	      out.println(strDocumentsList);
		  
	  }
	  
	}
  
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
      if(ServletFileUpload.isMultipartContent(request)){
          try {
        	  
              List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
              
              AlfrescoFile fileToLoad = new AlfrescoFile();
              
              for(FileItem item : multiparts){
            	  
                  if(!item.isFormField()){
                	 
                	  File file = new File(item.getName());
                	  item.write(file);
                	  
                	  fileToLoad.setFileToLoad(file);
                	                        
                  }else{
                	  
                	  if(item.getFieldName().equals("name") && !item.getString().equals("")){
                		  fileToLoad.setName(item.getString());
                	  }
                	                  	  
                	  if(item.getFieldName().equals("relativePath") && !item.getString().equals("")){
                		  fileToLoad.setFoldersToCreate(item.getString());
                	  }
                	                  	  
                  }
                  
              }
         
              //Document
        	  DocumentAlfrescoService service = new DocumentAlfrescoServiceImpl();
        	  String documentAddedName = service.loadDocument(fileToLoad);
              
             response.setContentType("text/html");

             PrintWriter out = response.getWriter();
             out.println("<html><head><style>body {background-color: #FAFAFA; color: #135EAB; font-family:Arial;}a:link {color: #135EAB;text-decoration:underline;}a:visited {color: #135EAB;}</style></head><body><br><br>Le document " + documentAddedName + " a été ajouté avec succes.<br><br><br><br><a href='/Alfresco-Solem-Prototype/'>Retour</a></body></html>");
             
          } catch (Exception ex) {
             request.setAttribute("message", "File upload faild : " + ex);
          }          
       
      }else{
    	  
    	  System.out.println("############################");
    	  
    	  
      }
        
	}
  
	public void destroy(){
	}
  
}
