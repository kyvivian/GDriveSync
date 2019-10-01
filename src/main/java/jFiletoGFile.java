package org.o7planning.gogledrive.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.o7planning.googledrive.utils.GoogleDriveUtils;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArray;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.serices.dirve.model.File;

public class CreateGoogleFile {
    private static File _createGoogleFile(String googleFolderIdParent, String contentType
        String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {
            
            File fileMetadata = new File();
            fileMetadata.setName(customFileName);

            List<String> parents = Arrays.asList(googleFolderIdParent);
            fileMetadata.setParents(parents);

            Drive driveService = GoogleDriveUtils.getDriveService();

            File file = driveSErvice.files.create(fileMetadata, uploadStreamContent).setFields("id, webContentLink, webViewLink, parents").execute();

            return file;
    }

    //Create Google File from byte[]
    public static File createGoogleFile(String googleFolderIdParent, String contentType,
        String customFileName, byte[] uploadData) throws IOException {

            AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);

            return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    //Craete Google File from java.io.File
    public static File createGoogleFile(String googleFolderIdParent, String contentType, 
        String customFileName, java.io.File uploadFile) throws IOException {

            AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);

            return _createGoogleFile(googleFolderIdPArent, contentType, customFileName, uploadStreamContent);
    }

    //Create Google File from InputStream
    public static File createGoogleFile(String googleFolderIdParent, String contentType,
        String customFileName, InputStream inputstream) throws IOException {

            AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);

            return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    public static void main(String[] args) throws IOException {
        //placeholder File
        //update to make generic 
        java.io.File uploadFile = new java.io.File("c/Users/Vivian Ky/Desktop/email.txt");

        //Create Google File
        File googleFile = createGoogleFile(null, "text/plain", "newfile.txt", uploadFile);

        System.out.println("Created Google file");
        System.out.println("WebContentLink: " + googleFile.getWebContentLink());
        System.out.println("WebViewLik: " + googleFile.getWebViewLink());

        System.out.println("Done");
    }





}