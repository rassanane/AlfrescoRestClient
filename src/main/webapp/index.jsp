 <%@ page import = "fr.scor.solem.services.*, fr.scor.solem.model.* , java.util.*"%>
<%


DocumentAlfrescoService service = new DocumentAlfrescoServiceImpl();

DocumentList documentList = service.myDocuments();

StringBuffer strDocumentsList = new StringBuffer("");

strDocumentsList.append("<TABLE width=\"900px\" style=\"border-collapse: separate; border-spacing: 10px;\">");
for (DocumentEntry documentEntry : documentList.list.entries) {
	  if(documentEntry.entry.isFile){
	      strDocumentsList.append("<TR><TD width=\"300px\">" + documentEntry.entry.name + "</TD><TD width=\"300px\"><a href='/Alfresco-Solem-Prototype/documents?action=show&fileId=" + documentEntry.entry.id + "' target='_blanck'>Afficher</a></TD><TD width=\"300px\"><a href='/Alfresco-Solem-Prototype/documents?action=del&fileId=" + documentEntry.entry.id + "'>Supprimer</a></TD></TR>");
	  }
}      
strDocumentsList.append("</TABLE>");


%>

<html>

	<head>

		<script type="text/javascript" src="jquery-1.12.0.min.js"></script>

		<script type="text/javascript">
		
			
		</script>
		
		<style>
		body {background-color: #FAFAFA; color: #135EAB; font-family:Arial;}
		a:link {
		 color: #135EAB;
		 text-decoration: underline;
		 }
		a:visited {
		 color: #135EAB;
		 }
		</style>
	
	</head>


	<body onload="">

	<br><br>
		
		<h4>Exemple de chargement d'un document dans le dossier Mes fichiers de l'utilisateur Solem :</h4>
		
		<form name="form1" id="form1" method="post" action="/Alfresco-Solem-Prototype/documents" enctype="multipart/form-data">
		
		  <TABLE width="500px" style="border-collapse: separate; border-spacing: 10px;">
		  <TR>
			<TD width="250px">Fichier à charger :</TD>
			<TD width="250px"><input type="file" name="filedata"></TD>
		  </TR>
		  <TR>
			<TD width="250px"></TD>
			<TD width="250px"><input type="submit" value="Envoyer"></TD>
		  </TR>
		  </TABLE>
			
		</form>
		
	<br><br>
		
		<h4>Liste des documents dans le dossier Mes fichiers de l'utilisateur Solem :</h4>
		<%=strDocumentsList %>
		




		
	<br><br>
		
		<h4>Exemple de chargement d'un document dans le dossier Mes fichiers de l'utilisateur Solem <br>avec possibilité de créer des sous dossiers et de changer le nom du fichier : </h4>
		
		
		<form name="form2" id="form2" method="post" action="/Alfresco-Solem-Prototype/documents" enctype="multipart/form-data">
		
		
		  <TABLE width="800px" style="border-collapse: separate; border-spacing: 10px;">
		  <TR>
			<TD width="250px">Fichier à charger :</TD>
			<TD width="550px"><input type="file" name="filedata"></TD>
		  </TR>
		  <TR>
			<TD width="250px">Nom de fichier :</TD>
			<TD width="550px"><input type="text" name="name"></TD>
		  </TR>
		  <TR>
			<TD width="250px">Dossiers à créer :</TD>
			<TD width="550px"><input type="text" name="relativePath"> Ex : Test/Images</TD>
		  </TR>
		  <TR>
			<TD width="250px"></TD>
			<TD width="550px"><input type="submit" value="Envoyer"></TD>
		  </TR>
		  </TABLE>
			
		</form>
		
		<br>
		
		<h4>Recherche par mot clé dans le dossier Mes fichiers de l'utilisateur Solem : </h4>

		<form name="form3" id="form3" method="get" action="/Alfresco-Solem-Prototype/documents" >
		  <TABLE width="500px" style="border-collapse: separate; border-spacing: 10px;">
		  <TR>
			<TD width="250px">Mot clé :</TD>
			<TD width="250px"><input type="text" name="keyword"> <input type="hidden" name="action" value="search"></TD>
		  </TR>
		  <TR>
			<TD width="250px"></TD>
			<TD width="250px"><input type="submit" value="Chercher"></TD>
		  </TR>
		  </TABLE>
		
		</form>

		<br><br><br>

	</body>



</html>



	