/**
 * 
 */
package com.tss.dropbox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

/**
 * @author NARESH GUMMAGUTTA
 *@since 2 Aug, 2021
 */
public class TestDropBox {
	public static final String  ACCESS_TOKEN = "sl.A1wNcm1Tr30RXkJJFAabUtw8tGLnYd_SldgE_Tln_bu7yYEvirWLwdlMlS2e5Nrx3CufsRl-LHdgh0V5uukYOH_thk6ZTHd58mQXgJUeDwVkI44v3xU95ZStHEG6sWEw-UhlvZW9W3c";
	
	public static void main(String[] args) throws DbxApiException, DbxException, FileNotFoundException, IOException {
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/Gummagutta_Sample").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());
        
     // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
     // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("C:\\Users\\G NARESH\\Desktop\\firstupload.txt")) {
            FileMetadata metadata = client.files().uploadBuilder("/javafolder/firstupload.txt")
                .uploadAndFinish(in);
        }

	}

}
