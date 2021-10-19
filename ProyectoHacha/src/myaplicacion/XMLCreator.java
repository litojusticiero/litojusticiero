package myaplicacion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLCreator {
	
	private final String rutaXMLRegistro;
	private final String rutaXMLComprobante;
	private String nombreArchivo;
	private int numerodepartes;
	private double tamanyoArchivo;
	//private String nombreDePartes;
	static Document doc;
    static Element rootElement;
    private String hash;
    private String listapartes;
    String fecha;
    
    /*Constructor de la clase XMLCreator, el cual creará el objeto necesario para crear los archivos XML, recibiendo los parámetros de la 
      clase Separador.
    */
    public XMLCreator(String nombreArchivo, int numerodepartes, double tamanyoArchivo, String filepath, String hash, String listapartes) {
        
        this.nombreArchivo = nombreArchivo;
        this.numerodepartes = numerodepartes;
        this.tamanyoArchivo = tamanyoArchivo;
        rutaXMLRegistro=(filepath)+"/"+"registro.xml";
        rutaXMLComprobante=(filepath)+"/"+nombreArchivo+".part.xml";
        this.hash=hash;
        this.listapartes = listapartes;
        
    }
    
    /*Este método creará dos archivos XML, uno será donde se guardará todos los registros de las diferentes divisiones de archivos, y
      el otro XML servirá como comprobante de que la division y union se ha realizado correctamente. en los dos se guardará la misma información
      pero el archivo XML comprobante no podrá ser reescrito ni se le podrá agregar nuevos datos.	
    */
	public void creadorXMLRegistro() throws SAXException, IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		fecha = dtf.format(now); 
		
    try {
    	
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      Path comprobantePath = Paths.get(rutaXMLComprobante);
      Path registroPath = Paths.get(rutaXMLRegistro);
      
      if (Files.exists(registroPath)) {
    	  
    	  doc = docBuilder.parse(rutaXMLRegistro);
    	  rootElement = doc.getDocumentElement();
    	  try {
			bodyXML(nombreArchivo,tamanyoArchivo, numerodepartes, fecha, hash, listapartes, rutaXMLRegistro);
		
    	  } catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      else {
    	  
    	  doc = docBuilder.newDocument();
    	  rootElement = doc.createElement("hacha");
    	  doc.appendChild(rootElement);
    	  try {
			bodyXML(nombreArchivo,tamanyoArchivo, numerodepartes, fecha, hash, listapartes, rutaXMLRegistro);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      } 

	      if (Files.exists(comprobantePath)) {  	  	  
	    	  JOptionPane.showMessageDialog(null,"Error al crear XML. Ya existe un XML con el mismo nombre", "Dialog",JOptionPane.ERROR_MESSAGE);

	      }
	      else {
	    	  doc = docBuilder.newDocument();
	    	  rootElement = doc.createElement("hacha");
	    	  doc.appendChild(rootElement);
	    	  try {
				bodyXML(nombreArchivo,tamanyoArchivo, numerodepartes, fecha, hash, listapartes, rutaXMLComprobante);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
	  
	      }
      
    	} catch (ParserConfigurationException pce) {
    		
    	}
	}
	private static void bodyXML(String nombrearchivo, double tamanyoarchivo, int numerodepartes, String fecha, String hash, String listapartes, String rutaxml) throws TransformerException {  	  
	      
        Element archivo = doc.createElement("archivo");
        rootElement.appendChild(archivo);
          
        Element nombre = doc.createElement("nombre");
        nombre.setTextContent(nombrearchivo);
        archivo.appendChild(nombre);
          
        Element tamanyo = doc.createElement("Tamaño");
        tamanyo.setTextContent(""+(Math.round(tamanyoarchivo/(1024*1024)*100.0)/100.0)+" MB");
        archivo.appendChild(tamanyo);
          
        Element numpartes = doc.createElement("numpartes");
        numpartes.setTextContent(""+numerodepartes);
        archivo.appendChild(numpartes);
          
        Element fechacreacion = doc.createElement("fechacreacion");
        fechacreacion.setTextContent(fecha);
        archivo.appendChild(fechacreacion);
        
        Element hashcod = doc.createElement("hash");
        hashcod.setTextContent(""+hash);
        archivo.appendChild(hashcod);
          
        Element partes = doc.createElement("partes");
        partes.setTextContent(listapartes);
        archivo.appendChild(partes);
        
          //Se escribe el contenido del XML en un archivo
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        //sustituir el nombre por la ruta
        StreamResult result = new StreamResult(new File(rutaxml));
        transformer.transform(source, result);
        
      }
	
}

