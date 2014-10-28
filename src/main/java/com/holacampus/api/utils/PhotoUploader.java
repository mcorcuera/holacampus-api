/*
 * Copyright (C) 2014 Mikel Corcuera <mik.corcuera@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.holacampus.api.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.imageio.ImageIO;
import org.glassfish.jersey.internal.util.Base64;

/**
 * Clase que provee de utilidad para la subida y almacenamiento de fotos. Entre
 * sus funciones se encuentra el analizar los datos de entrada y comprobar que 
 * se trata de datos de image. Después crea una versión en miniatura de la imagen
 * y tras ello almacena ambas imagenes en disco para que puedan ser accedidas por
 * el servidor HTTP instalado en la maquina
 *  @author Mikel Corcuera <mik.corcuera@gmail.com>  
 */
public class PhotoUploader {

    /**
     * Carpeta donde almacenar las imágenes
     */
    public static final String  IMAGE_FOLDER         = "C:/Program Files/Apache Software Foundation/Apache2.2/htdocs/holacampus/images";

    /**
     * URL donde se encuentran las imágenes
     */
    public static final String  IMAGE_BASE_URL       = "http://localhost/holacampus/images";
    
    /**
     * Carpeta donde alamcenar las miniaturas de las imagenes
     */
    public static final String  THUMB_FOLDER         = "C:/Program Files/Apache Software Foundation/Apache2.2/htdocs/holacampus/thumbnails";

    /**
     * URL donde se encuentrar las miniaturas de las imagenes
     */
    public static final String  THUMB_BASE_URL       = "http://localhost/holacampus/thumbnails";
   
    /**
     * Tamaño máximo de las miniaturas
     */
    public static final int     THUMB_DIMENSION      = 170;
    
    /**
     * Esta función almacena la imagen y la miniatura de esta en disco. Para ello 
     * necesita de los datos binarios de la imagen codificados en base64. Para guardar
     * la imagen, le dará a esta un nombre de archivo que coincidirá con el resultado
     * de aplicar el algoritmo MD5 al contenido de la foto.
     * @param photo datos binarios de la foto a almacenar codificados en Base64
     * @return URLs donde encontrar la imagen y la miniatura de esta
     * @throws NoSuchAlgorithmException
     * @throws MimeTypeParseException
     * @throws IOException
     */
    public static PhotoUrls uploadPhoto( String photo) throws NoSuchAlgorithmException, MimeTypeParseException, IOException {
       
        PhotoUrls photoUrls         = null;
        StringBuffer hexString      = new StringBuffer();
        byte[] photoBytes           = Base64.decode(photo.getBytes());
        InputStream is              = new BufferedInputStream(new ByteArrayInputStream( photoBytes));
        MimeType mimeType           = new MimeType( URLConnection.guessContentTypeFromStream(is));
        MessageDigest md            = MessageDigest.getInstance("MD5");
        
        // Only stores data if it is a valid image format
        if( mimeType.getPrimaryType().equals("image") && mimeType.getSubType() != null) {
            byte[] hash = md.digest( photoBytes);
            
            BufferedImage img = ImageIO.read(new ByteArrayInputStream( photoBytes));

            // Convert to HEX String
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0"
                            + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            String extension = mimeType.getSubType();
            
            /* Calculate thumbnail size */
            float relation      =  ( (float) img.getWidth()) / ( (float) img.getHeight());
            int thumbnailWidth  = 0;
            int thumbnailHeight = 0;
            
            if( relation > 1) {
                // If width > height is a horizontal image
                thumbnailHeight = PhotoUploader.THUMB_DIMENSION;
                thumbnailWidth = (int) ( thumbnailHeight * relation);
            }else{
                thumbnailWidth = PhotoUploader.THUMB_DIMENSION;
                thumbnailHeight = (int) ( thumbnailWidth / relation);
            }
            
            Image thumbnail = img.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_DEFAULT);
            
            File imageFile = new File( IMAGE_FOLDER + "/" + hexString + "." + extension);
            ImageIO.write( img, extension, imageFile);
            
            File thumbnailFile = new File( THUMB_FOLDER + "/" + hexString + "." + extension);
            ImageIO.write( toBufferedImage( thumbnail), extension, thumbnailFile);
            /*
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream( IMAGE_FOLDER + "/" + hexString + "." + extension);
                fo.write( photoBytes);
                 fo.flush();
            } catch (FileNotFoundException ex) {
                throw ex;
            } catch (IOException ex) {
                throw ex;
            }finally {
                if( fo != null) {
                    fo.close();
                }
            
            */
            photoUrls = new PhotoUrls();
            
            photoUrls.url = IMAGE_BASE_URL + "/" + hexString + "." + extension;
            photoUrls.thumbnailUrl = THUMB_BASE_URL + "/" + hexString + "." + extension;
        }
        
        return photoUrls;
    }
    
    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    /**
     * Esta clase contiene las urls de la imagen y la de su miniatura
     */
    public static class PhotoUrls {

        /**
         * url de la imagen
         */
        public String url;

        /**
         * url de la miniatura
         */
        public String thumbnailUrl;
    }
}
