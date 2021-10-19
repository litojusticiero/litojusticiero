/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package myaplicacion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 *
 * @author Fer
 */
/*Clase que se utilizará para leer el hash de los diferentes archivos.*/
public class HashReader {
          
    public String md5OfFile(File file) throws Exception{
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fs = new FileInputStream(file);
        BufferedInputStream bs = new BufferedInputStream(fs);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while((bytesRead = bs.read(buffer, 0, buffer.length)) != -1){
        md.update(buffer, 0, bytesRead);
        }
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for(byte bite : digest){
        sb.append(String.format("%02x", bite & 0xff));
        bs.close();
        }
        return sb.toString();
        
    }
}
