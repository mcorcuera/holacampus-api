package com.holacampus.api.security;

/* 
 * Password Hashing With PBKDF2 (http://crackstation.net/hashing-security.htm).
 * Copyright (c) 2013, Taylor Hornby
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

import com.holacampus.api.domain.Credentials;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * PBKDF2 salted password hashing.
 * Author: havoc AT defuse.ca
 * www: http://crackstation.net/hashing-security.htm
 */

/**
 * Clase que implementa el Hash+Salt de las contraseñas
 * @author Mikele
 */

public class PasswordHash
{
    // Algoritmo usado para realizar el Hash

    /**
     * Nombre del algoritmo usado
     */
        public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * Tamaño del Salt
     */
    public static final int SALT_BYTE_SIZE = 32;

    /**
     * Tamaño del Hash
     */
    public static final int HASH_BYTE_SIZE = 32;

    /**
     * Iteraciones realizadas por el algoritmo
     */
    public static final int PBKDF2_ITERATIONS = 1000;

    /**
     * Indice de iteraciones del algoritmo
     */
    public static final int ITERATION_INDEX = 0;

    /**
     * Indice del salt
     */
    public static final int SALT_INDEX = 1;

    /**
     * Indice de PBKDF2_INDEX
     */
    public static final int PBKDF2_INDEX = 2;

    /**
     * Devuelve el hash usando PBKDF2 y un salt de la contraseña.
     *
     * @param   password    la contraseña sobre la que aplicar el hash
     * @param credentials los credenciales donde se almacenarán la configuración 
     * del algoritmo
     * @return              la contraseña tras haber aplicado sobre ella el hash
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static String createHash(String password, Credentials credentials)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return createHash(password.toCharArray(), credentials);
    }

    /**
     * Devuelve el hash usando PBKDF2 y un salt de la contraseña.
     *
     * @param   password    la contraseña sobre la que aplicar el hash
     * @param credentials los credenciales donde se almacenarán la configuración 
     * del algoritmo
     * @return               la contraseña tras haber aplicado sobre ella el hash
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static String createHash(char[] password, Credentials credentials)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        
        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        
        credentials.setSalt(salt);
        credentials.setHashedPassword(hash);
        credentials.setIterations(PBKDF2_ITERATIONS);
        
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
    }

    /**
     * Valida una contraseña con respecto a los credenciales de un usuario
     *
     * @param   password        La contraseña a validar
     * @param   credentials    los credenciales del usuario sobre los que comprobar la contraseña
     * @return                  true si la contraseña coincide, false si no.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static boolean validatePassword(String password, Credentials credentials)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return validatePassword(password.toCharArray(), credentials);
    }

    /**
     * Valida una contraseña con respecto a los credenciales de un usuario
     *
     * @param   password        La contraseña a validar
     * @param   credentials    los credenciales del usuario sobre los que comprobar la contraseña
     * @return                  true si la contraseña coincide, false si no.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static boolean validatePassword(char[] password, Credentials credentials)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Decode the hash into its parameters
        int iterations = credentials.getIterations();
        byte[] salt = credentials.getSalt();
        byte[] hash = credentials.getHashedPassword();
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Compara dos cadenas de bytes de manera lenta. 
     * 
     * @param   a       la primera cadena de bytes
     * @param   b       la segunda cadena de bytes
     * @return          true si son iguales, false si no lo son.
     */
    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     * Calcula el hash PBKDF2 de una contraseña
     *
     * @param   password    la contraseña sobre la que aplicar el hash
     * @param   salt        el Salt
     * @param   iterations  las iteraciones que realiza el algoritmo
     * @param   bytes       El tamño del hash
     * @return              El hash PBKDF2 de la contraseña
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Convierte una cadena de caracteres que representan un número en hexadecimal
     * en una cadena de bytes
     *
     * @param   hex         la cadena del número en hexadecimal
     * @return              la cadena de bytes decodificada
     */
    private static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    /**
     * Convierte una cadena de bytes en una cadena de caracteres que representa esta 
     * cadena en codificación hexadecimal.
     *
     * @param   array       la cadena de caracteres a convertir
     * @return             la cadena representando los bytes en hexadecimal.
     */
    public static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

}
