package myaplicacion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Unificador {
	
	private String filepath;
	private String filepathDestino;
    private String path;
        
	public Unificador(String filepath, String filepathDestino) {
            
		this.filepath = filepath.substring(0, filepath.lastIndexOf("."));
		this.filepathDestino = filepathDestino;
        this.path = filepath; 
        
	}
	
	public void juntar()  throws FileNotFoundException, IOException, Exception {
		
		  int numerodelaParte = 0;
		  int cantidadPartes = 0;
		  Path firstPath = Paths.get(filepath+"."+numerodelaParte);
		  
		  
		  while(Files.exists(firstPath)) {
		  numerodelaParte++;
	      firstPath = Paths.get(filepath+"."+numerodelaParte);
        	    	
		  }  
		  
          //File f = new File(filepathDestino);
	      //HashReader hashcod = new HashReader();
	      
	     // File fpath = new File(path);
	     
		  path = filepath+".xml";
	      
	      
		  File filexml = new File(path);
		  //Comprobamos que el archivo XML existe.
	      
		  if (filexml.exists()) {
	    	
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	      DocumentBuilder db = dbf.newDocumentBuilder();  
	      Document doc = db.parse(filexml);  
	      doc.getDocumentElement().normalize();
	      NodeList nodeList = doc.getElementsByTagName("archivo");
  
      
		  boolean flag=false;
		  cantidadPartes = numerodelaParte;
		  numerodelaParte=0;
		  String newHash;
		  String originalHash="";
		  
		  for (int itr = 0; itr < nodeList.getLength(); itr++) {
			  Node node = nodeList.item(itr);
          
			  if (node.getNodeType() == Node.ELEMENT_NODE) { 
             	Element eElement = (Element) node;
              
             	if ((""+cantidadPartes).equals(eElement.getElementsByTagName("numpartes").item(0).getTextContent())){

            	  flag=true;
            	  originalHash=eElement.getElementsByTagName("hash").item(0).getTextContent();
                  break;
             	}     
			  }	
		  }
		  
		  if (flag == true) {
			  	FileOutputStream fos = new FileOutputStream(filepathDestino);
			  	BufferedOutputStream escribir = new BufferedOutputStream(fos);
			  	
			  	while(true) {
		            firstPath = Paths.get(filepath+"."+numerodelaParte);
		            if (Files.exists(firstPath)) {
		                try (FileInputStream fis = new FileInputStream(filepath+"."+numerodelaParte);
		                BufferedInputStream bis = new BufferedInputStream(fis)) {
		        	
		            numerodelaParte++;
		        	//escribir.write(bis.readAllBytes());
		            byte[] buffer = new byte[8 * 1024];
		            int read;
		            while((read=bis.read(buffer, 0, buffer.length))!= -1) {
		            	escribir.write(buffer, 0, read);
		            }
		            
		        	escribir.flush();
		                }
		            }
		            else {
		        	escribir.close();
		        	break;
		        	}
			  	}
			  	File archivoFinal=new File(filepathDestino);
			  	HashReader code = new HashReader();
			  	newHash = code.md5OfFile(archivoFinal);
			  	if(newHash.equals(originalHash)) {
			  		JOptionPane.showMessageDialog(null,"Archivo unido correctamente", "Dialog", JOptionPane.INFORMATION_MESSAGE);
			  	}
			  	else {
			  		JOptionPane.showMessageDialog(null,"Hash error" + newHash + " y "+ originalHash + " no coinciden", "Dialog", JOptionPane.ERROR_MESSAGE);
			  	}

      }
	 if (flag==false) {
	    	 JOptionPane.showMessageDialog(null,"Error al unir archivo", "Dialog",JOptionPane.ERROR_MESSAGE);
	 }
	      }else {
	    	  JOptionPane.showMessageDialog(null,"Error al unir archivo, no se encuentra XML", "Mensaje de los 90",JOptionPane.ERROR_MESSAGE);
	      }
		  
   }
	
	
}
