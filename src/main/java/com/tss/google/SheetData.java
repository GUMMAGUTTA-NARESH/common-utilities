/**
 * 
 */
package com.tss.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * @author NARESH.G
 *Jun 3, 2021
 */
public class SheetData {
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Google Sheet Example";
	private static String SPREADSHEET_ID = "18849_SE5YuseF0tOtMvv_t4aBZdncqAg-3qANl7nGss";
	private static Credential authorize() throws IOException, GeneralSecurityException {
//		InputStream stream = SheetData.class.getResourceAsStream("\\credentials.json");
		InputStream stream = new FileInputStream("C:\\javaworkplaces\\javaworkproject\\utilities-common\\credentials.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(stream));
		List<String> scopes = java.util.Arrays.asList(SheetsScopes.SPREADSHEETS);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
				.setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
				.setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(
				flow, new LocalServerReceiver())
				.authorize("user");
		stream.close();
		return credential;
	}
	//AIzaSyAYuPnk7XNUB6D30IlUMkwajtKuhSKz3-8
public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
	Credential credential = authorize();
	return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
			.setApplicationName(APPLICATION_NAME)
			.build();
	}
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		sheetsService = getSheetsService();
		String range = "Naresh!A3:B3";
		ValueRange respose = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();

		List<List<Object>> values = respose.getValues();
		if (values == null || values.isEmpty()) {
			System.out.println("No Data found");
		} else {
			for (@SuppressWarnings("rawtypes") List row : values) {
//				System.out.println("from " + row.get(0) + "" + row.get(1) + "" + row.get(2));
				 System.out.printf("%s, %s\n", row.get(0), row.get(3));
			}
		}
	}
}
