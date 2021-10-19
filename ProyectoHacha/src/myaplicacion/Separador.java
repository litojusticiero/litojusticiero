package myaplicacion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

public class Separador {
	private long sourceSize;
    private String filePath;
    private String filename;
    private File archivoOriginal; 
    private int partes; 
    private XMLCreator XMLFILE;
    private String hash;     
    private String nombrePartes [];
    private int maxBuffer;
    
    
    /*Constructor el cual nos permitirá crear el objeto de la clase utilizando dos parámetros, filepath, 
     * que es la ruta del archivo a separar y partes, que es la cantidad de
     * partes en las que se dividirá el archivo.
    */
     public Separador(String filePath, int partes) {
    	 
        this.filePath = filePath;
        this.partes = partesNozero(partes);
        archivoOriginal = new File(filePath);  
        sourceSize = (long) archivoOriginal.length();  
        filename = archivoOriginal.getName();
        nombrePartes =new String [partes+1];
        maxBuffer= 8*1024;
     }
    /*Método que nos permitirá separar el archivo,
     *  
     */
	public void Separar() throws FileNotFoundException, IOException, SAXException, NoSuchAlgorithmException {
                 
	      try (FileInputStream fis = new FileInputStream(filePath);
	             BufferedInputStream bis = new BufferedInputStream(fis)) {
	            
	            long bytesPerSplit = sourceSize/partes ;
	            long remainingBytes = sourceSize % partes;

	            for(int numerodeParte=0; numerodeParte < partes; numerodeParte++) {
	            	
	                BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(filePath+".part."+(numerodeParte)));
	                nombrePartes [numerodeParte]= filename+".part."+(numerodeParte);
	               
	                if(bytesPerSplit > maxBuffer) {
	                	
	                    long numReads = bytesPerSplit/maxBuffer;
	                    long numRemainingRead = bytesPerSplit % maxBuffer;
	                    
	                    for(int i=0; i<numReads; i++) {
	                    	
	                    	byte[] buf = new byte[maxBuffer];
	                    	int valor = bis.read(buf);
	                    	if(valor != -1) {
		                        bw.write(buf);
		                    }
	                    	
	                    }
	                    if(numRemainingRead > 0) {
	                    	
	                    	byte[] buf = new byte[(int)numRemainingRead];
	                    	int valor = bis.read(buf);
	                    	if(valor != -1) {
		                        bw.write(buf);
		                    }
	                    }
	                }else {
	                	byte[] buf = new byte[(int)bytesPerSplit];
                    	int valor = bis.read(buf);
                    	if(valor != -1) {
	                        bw.write(buf);
	                    }
	                }
	                bw.close();
	            }
	            
	            if(remainingBytes > 0) {
	                BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(filePath+".part."+(partes-1), true));
	                byte[] buf = new byte[(int)remainingBytes];
                	int valor = bis.read(buf);
                	if(valor != -1) {
                        bw.write(buf);
                        bw.close();
                	}
                	
	            }
	            
	         nombrePartes [partes]= filename+".part.xml";
             HashReader code = new HashReader();
            try {
                hash =code.md5OfFile(archivoOriginal);
            } catch (Exception ex) {
                Logger.getLogger(JDialogHashview.class.getName()).log(Level.SEVERE, null, ex);
            }
	      }
             String listapartes = Arrays.toString(nombrePartes).replace("[", "").replace("]", "");
             
             XMLFILE = new XMLCreator(filename, partes, archivoOriginal.length(), archivoOriginal.getParent(), hash, listapartes);
             
             XMLFILE.creadorXMLRegistro();
}
	//Método para evitar que el número introducido por el usuario sea de 0 o inferior. 
    private static int partesNozero(int numero){
            if (numero < 1){ 
                numero = 1;
            }
            return numero;
        }
    
        
    
}
