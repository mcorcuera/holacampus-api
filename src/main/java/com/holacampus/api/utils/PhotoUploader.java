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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import org.glassfish.jersey.internal.util.Base64;

public class PhotoUploader {
    public static final String IMAGE_FOLDER         = "C:/Program Files/Apache Software Foundation/Apache2.2/htdocs/holacampus/images";
    public static final String IMAGE_BASE_URL       = "http://localhost/holacampus/images";
   
    public static String uploadPhoto( String photo) throws NoSuchAlgorithmException, MimeTypeParseException, IOException {
       
        String url                  = null;
        StringBuffer hexString      = new StringBuffer();
        byte[] photoBytes           = Base64.decode(photo.getBytes());
        InputStream is              = new BufferedInputStream(new ByteArrayInputStream( photoBytes));
        MimeType mimeType           = new MimeType( URLConnection.guessContentTypeFromStream(is));
        MessageDigest md            = MessageDigest.getInstance("MD5");
        
        // Only stores data if it is a valid image format
        if( mimeType.getPrimaryType().equals("image") && mimeType.getSubType() != null) {
            byte[] hash = md.digest( photoBytes);

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
            }
            url = IMAGE_BASE_URL + "/" + hexString + "." + extension;
        }
        
        return url;
    }
}
