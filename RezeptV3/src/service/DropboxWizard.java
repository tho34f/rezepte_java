package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.DownloadBuilder;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.SearchV2Result;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

import data.Category;
import data.Rezept;

public class DropboxWizard {
	
	private static final String ACCESS_TOKEN = "XXX";
	private static final String BACKSLASH = "/";
	private String userPath;
	
	DbxClientV2 dbxClient;

	public DropboxWizard() {
		// Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        this.dbxClient = new DbxClientV2(config, ACCESS_TOKEN);
        String userName = System.getProperty("user.name");
        this.userPath = "C:/Users/" + userName + "/Documents/";
	}
	
	public String getAccountInformation() throws DbxException {
		FullAccount account = this.dbxClient.users().getCurrentAccount();
		return account.toString();
	}

	public boolean uploadToDropbox(String fileName) throws IOException, DbxException {
		FileMetadata uploadFile = null;
		boolean isUploadOk = false;
		String path = this.userPath + fileName;
		try(InputStream in = new FileInputStream(path)){
			uploadFile = this.dbxClient.files().uploadBuilder(BACKSLASH + fileName).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
			isUploadOk = uploadFile.getIsDownloadable();
		}
		
		return isUploadOk;
	}

	public List<Category> listDropboxFolders(String folderPath) throws DbxException {
		List<Category> folders = new ArrayList<>();
		ListFolderResult listing = null;
		if(!folderPath.isBlank()) {		
			listing = this.dbxClient.files().listFolderBuilder(BACKSLASH + folderPath).start();
		} else {
			listing = this.dbxClient.files().listFolderBuilder("").start();
		}
		for(Metadata listItem : listing.getEntries()) {
			if(!listItem.getPathDisplay().replace(BACKSLASH, "").equals("rezepte.txt")) {
				Category cat = new Category(listItem.getPathDisplay().replace("/", ""), listItem.getPathDisplay());
				folders.add(cat);
			}
		}
		return folders;
	}
	
	public List<Rezept> listDropboxRezepte(String folderPath) throws DbxException {
		List<Rezept> folders = new ArrayList<>();
		ListFolderResult listing = null;
		if(!folderPath.isBlank()) {		
			listing = this.dbxClient.files().listFolderBuilder(BACKSLASH + folderPath).start();
		} else {
			listing = this.dbxClient.files().listFolderBuilder("").start();
		}
		for(Metadata listItem : listing.getEntries()) {
			Rezept cat = new Rezept(listItem.getPathDisplay().replace(BACKSLASH, "").replace(folderPath, ""), listItem.getPathDisplay());
			folders.add(cat);
		}
		return folders;
	}

	public boolean createFolder(String folderName) throws DbxException {
		CreateFolderResult folder = this.dbxClient.files().createFolderV2(BACKSLASH + folderName);
		FolderMetadata fmd = folder.getMetadata();
		return fmd.getName().equals(folderName);
	}
	
	public boolean checkFolder(String folderPath) throws DbxException {
		SearchV2Result search = this.dbxClient.files().searchV2Builder(folderPath).start();
		return search.getHasMore();
	}

	public boolean downloadFromDropbox(String fileName, String dataName, boolean isMetadata) throws DbxException, IOException {
		FileMetadata downloadedFile = null;
		File downloadPath = null;
		DownloadBuilder dwBuilder = null;
		boolean isDownloadOk = false;
		
		if(isMetadata) {
			downloadPath = new File(this.userPath + dataName + ".txt");
		} else {
			downloadPath = new File(this.userPath + dataName);
		}
		
		try(FileOutputStream outputStream = new FileOutputStream(downloadPath)) {
			if(isMetadata) {
				dwBuilder = this.dbxClient.files().downloadBuilder(BACKSLASH  + dataName + ".txt");
			} else {
				dwBuilder = this.dbxClient.files().downloadBuilder(BACKSLASH +  fileName + BACKSLASH + dataName);
			}
			downloadedFile = dwBuilder.download(outputStream);
			isDownloadOk = downloadedFile.getIsDownloadable();
		}
		
		return isDownloadOk;
	}
}
