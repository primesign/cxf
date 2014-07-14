/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.rs.security.oauth2.jwe;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.cxf.rs.security.oauth2.jwt.JwtHeadersWriter;
import org.apache.cxf.rs.security.oauth2.jwt.JwtTokenReaderWriter;
import org.apache.cxf.rs.security.oauth2.utils.Base64UrlUtility;


public class JweCompactProducer {
    private StringBuilder jweContentBuilder;
    private String encodedEncryptedContent;
    private String encodedAuthTag;
    public JweCompactProducer(JweHeaders headers,
                       byte[] encryptedContentEncryptionKey,
                       byte[] cipherInitVector,
                       byte[] encryptedContentNoTag,
                       byte[] authenticationTag) {    
        this(headers, null, encryptedContentEncryptionKey, 
             cipherInitVector, encryptedContentNoTag, authenticationTag);
    }
    
    public JweCompactProducer(JweHeaders headers,
                       JwtHeadersWriter writer,
                       byte[] encryptedContentEncryptionKey,
                       byte[] cipherInitVector,
                       byte[] encryptedContentNoTag,
                       byte[] authenticationTag) {
        jweContentBuilder = startJweContent(new StringBuilder(), headers, writer, 
                                   encryptedContentEncryptionKey, cipherInitVector);
        this.encodedEncryptedContent = Base64UrlUtility.encode(encryptedContentNoTag);
        this.encodedAuthTag = Base64UrlUtility.encode(authenticationTag);
        
    }
    
    public JweCompactProducer(JweHeaders headers,
                       byte[] encryptedContentEncryptionKey,
                       byte[] cipherInitVector,
                       byte[] encryptedContentWithTag,
                       int authTagLengthBits) {    
        this(headers, null, encryptedContentEncryptionKey, 
             cipherInitVector, encryptedContentWithTag, authTagLengthBits);
    }
    public JweCompactProducer(JweHeaders headers,
                       JwtHeadersWriter writer,
                       byte[] encryptedContentEncryptionKey,
                       byte[] cipherInitVector,
                       byte[] encryptedContentWithTag,
                       int authTagLengthBits) {
        jweContentBuilder = startJweContent(new StringBuilder(), headers, writer,
                                   encryptedContentEncryptionKey, cipherInitVector);
        this.encodedEncryptedContent = Base64UrlUtility.encodeChunk(
            encryptedContentWithTag, 
            0, 
            encryptedContentWithTag.length - authTagLengthBits / 8);
        this.encodedAuthTag = Base64UrlUtility.encodeChunk(
            encryptedContentWithTag, 
            encryptedContentWithTag.length - authTagLengthBits / 8, 
            authTagLengthBits / 8);
        
    }
    public static String startJweContent(JweHeaders headers,
                                                JwtHeadersWriter writer, 
                                                byte[] encryptedContentEncryptionKey,
                                                byte[] cipherInitVector) {
        return startJweContent(new StringBuilder(), 
                               headers, writer, encryptedContentEncryptionKey, cipherInitVector).toString();       
    }
    public static StringBuilder startJweContent(StringBuilder sb,
                                        JweHeaders headers,
                                        JwtHeadersWriter writer, 
                                        byte[] encryptedContentEncryptionKey,
                                        byte[] cipherInitVector) {
        writer = writer == null ? new JwtTokenReaderWriter() : writer;
        String encodedHeaders = Base64UrlUtility.encode(writer.headersToJson(headers));
        String encodedContentEncryptionKey = Base64UrlUtility.encode(encryptedContentEncryptionKey);
        String encodedInitVector = Base64UrlUtility.encode(cipherInitVector);
        sb.append(encodedHeaders)
            .append('.')
            .append(encodedContentEncryptionKey == null ? "" : encodedContentEncryptionKey)
            .append('.')
            .append(encodedInitVector == null ? "" : encodedInitVector)
            .append('.');
        return sb;
    }
    
    public static void startJweContent(OutputStream os,
                                       JweHeaders headers,
                                       JwtHeadersWriter writer, 
                                       byte[] encryptedContentEncryptionKey,
                                       byte[] cipherInitVector) throws IOException {
        writer = writer == null ? new JwtTokenReaderWriter() : writer;
        byte[] jsonBytes = writer.headersToJson(headers).getBytes("UTF-8");
        Base64UrlUtility.encodeAndStream(jsonBytes, 0, jsonBytes.length, os);
        byte[] dotBytes = new byte[]{'.'};
        os.write(dotBytes);
        Base64UrlUtility.encodeAndStream(encryptedContentEncryptionKey, 0, 
                                         encryptedContentEncryptionKey.length, os);
        os.write(dotBytes);
        Base64UrlUtility.encodeAndStream(cipherInitVector, 0, cipherInitVector.length, os);
        os.write(dotBytes);         
    }
    
    public String getJweContent() {
        return jweContentBuilder.append(encodedEncryptedContent)
                 .append('.')
                 .append(encodedAuthTag)
                 .toString();
    }
}