package com.gn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.CredentialException;

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

public class GoogleSheet {
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Google sheet Example";
	private static String SPREADSHEET_ID = "1dAQ-s3mpcNwvJsfZypKk8CbLkxEB4pOonhMWqbrbQWQ";
	public static final String CREDENTIAL = "/credentialss.json";
	
	private static Credential authorize() throws IOException, GeneralSecurityException {
		InputStream in = GoogleSheet.class.getResourceAsStream(CREDENTIAL);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
				);
		System.out.println(in);
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		System.out.println(scopes);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
				.setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
				.setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
				.authorize("user");
		return credential;
	}
	
	
	public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
		Credential credential = authorize();
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		sheetsService = getSheetsService();
		String range = "test!A2:B11";
		ValueRange response = sheetsService.spreadsheets().values()
				.get(SPREADSHEET_ID, range)
				.execute();
		List<List<Object>> values = response.getValues();
		if(values == null || values.isEmpty()) {
			System.out.println("No Data found");
		}
		else {
			for(List row :values) {
				System.out.printf("%s %s from %s\n", row.get(1),row.get(0));
			}
		}
	}

}
