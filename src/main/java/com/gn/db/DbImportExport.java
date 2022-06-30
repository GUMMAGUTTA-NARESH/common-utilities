package com.gn.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gn.service.Constants;
import com.gn.service.GnMap;
import com.gn.util.GnUtil;


/**
 * This class is used to Import the db from one server to another server
 * Export the Dbs
 * Import the Dbs
 * @author NARESH GUMMAGUTTA
 *
 */
public class DbImportExport {
	
	private static Logger log = Logger.getLogger(DbImportExport.class.toString());
	
	/**
	 * This method is used to backup dbs from server and import to local server
	 * @param reqData
	 * @throws Exception
	 * @author G NARESH
	 */
	public static void getDbBackUp(GnMap reqData) throws Exception {
		String fullPath = "";
		if(Constants.IS_WINDOWS) {
		 fullPath = Constants.DB_BACKUP_BASE_PATH_WINDOWS.replace("{{path}}",reqData.getS("path"));
		} else {
			fullPath = Constants.DB_BACKUP_BASE_PATH_UBUNTU.replace("{{path}}",reqData.getS("path"));
		}
		String database = !reqData.getS("database").contains("_") ? "v35".equalsIgnoreCase(reqData.getS("server"))
						? Constants.V35+reqData.getS("database") : "zc_"+reqData.getS("database") :reqData.getS("database");
		String addPath = fullPath + File.separator+getClient(database)+"_"+GnUtil.getCurrentDateTime();
		getDbBackup(addPath, reqData.getS("db_host"), reqData.getI("db_port"),reqData.getS("db_username"), reqData.getS("db_password"),
				new String[] { database, database + "_admin" });
		System.out.println("-------BackUp Completed---------");
		if(reqData.getB("doImport")) {
			doImport(getClient(database), new File(addPath).listFiles());
			System.out.println("------Import Completed---------");
		}
		
	}
	
	/**
	 * this method only for backup the dbs
	 * @param folderPath
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param dbName
	 * @throws Exception
	 * @author G NARESH
	 */
	public static void getDbBackup(String folderPath, String host, int port,String username, String password, String... dbName) throws Exception {
		File f1 = new File(folderPath);
		if(!f1.exists()) f1.mkdir();
		if (f1.isDirectory() && f1.exists()) {
			if (f1.listFiles().length == 0) {
				for (String database : dbName) {
					String savePath = "\"" + folderPath + File.separator + database + ".sql\"";
					String executeCmd = "mysqldump -u" + username + " -p" + GnUtil.decrypt(password) + " -h" + host + " -P" + port
							+ " --events --routines --comments " + database + " -r " + savePath;
					log.info("-----------------------------------------");
					log.info(executeCmd);
					log.info("-----------------------------------------");
					StringBuffer res = new StringBuffer();
					boolean execFlag = false;
					if(Constants.IS_WINDOWS) {
							execFlag = exec(res,0, executeCmd);
					} else {
							execFlag = exec(res,0, "/bin/sh","-c",executeCmd);
					}
					log.info(res.toString());
					if (!execFlag) {
						throw new Exception("backup is not done");
					}
				}
//				doImport(client, new File(folderPath).listFiles());
//				GnUtil.zip(folderPath, true, f1.listFiles());
//				GnUtil.deleteFolder(new File(folderPath));
			} else throw new Exception("The given path already contains files in it : "+f1);
		} else throw new Exception("The given path is not a folder or it doesnot exist :"+f1);
	}
	
	/**
	 * import the dbs to local server
	 * @param client
	 * @param files
	 * @throws Exception
	 * @author G NARESH
	 */
	public static void doImport(String client,File...files) throws Exception {
		for (File database : files) {
			if(!database.exists() && !"sql".equalsIgnoreCase(GnUtil.getFileExtension(database.getPath()))) {
				throw new Exception("Sql Files not found..");
			}
			String db = getFileName(database).contains("admin")? "zc_"+client+"_admin":"zc_"+client;
//			mysql -h 127.0.0.1 -u admin -p12345 zc_brd <D:\Threshold_Training\db backups\BRD\brd_20210830_152918\zc_brd.sql
//			String executeCmd = "mysql -h " + Constants.MYSQL_HOST + " -u " + Constants.MYSQL_USER_NAME + " -p" + Constants.MYSQL_PASSWORD + " -P" + Constants.MYSQL_PORT
//					+" "+ db+" < " + database.getPath();
			String[] executeCmd = new String[] { "mysql", "--user=" + Constants.MYSQL_USER_NAME, "--password=" + Constants.MYSQL_PASSWORD, db, "-e",
					" source " + database.getPath() };
			log.info("-----------------------------------------");
			String ss = "";
			for(String s :executeCmd) {
				ss += " "+s+" ";
			}
			log.info(ss);
			log.info("-----------------------------------------");
			StringBuffer res = new StringBuffer();
			boolean execFlag = false;
			if(System.getProperty("os.name").toLowerCase().indexOf("windows")>-1) {
					execFlag = exec(res,0,executeCmd);
			}else {
//					execFlag = exec(res,0, "/bin/sh","-c",executeCmd);
			}
			log.info(res.toString());
			if (!execFlag) {
				throw new Exception("import is not done");
			}
		}
		log.info("...........Imported Successfully completed.....................");
	}
	
	private static String getFileName(File file) {
		return GnUtil.getFileExtension(Paths.get(file.getPath()).getFileName().toString(),true);
	}
	
	public static boolean exec(StringBuffer res, long timeout, String... cmds) throws Exception {
		boolean status = false;
		log.info("Starting cmd Exec");
		Process process = null;
		process = (cmds.length==1) ? Runtime.getRuntime().exec(cmds[0]) : Runtime.getRuntime().exec(cmds);
		int processComplete = process.waitFor();
		status = processComplete == 0 ? false :true;
		log.info("Completed cmd Execution AND status: "+ status);
		String outPut = "";
		if (process != null) {
			InputStream stdout = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					outPut += line + "\n";
				}
			} catch (IOException e) {
				log.info("Exception in reading output..:" + e.toString());
				return false;
			}
		}
		System.out.println(outPut);
		if (res != null)
			res.append("Output Is:[START]\n" + outPut + "[END]");
		return status;
	}
	
	private static String getClient(String s) {
		if(s.contains("admin")) s = "";
		return GnUtil.isBlank(s)? "" :s.substring(s.lastIndexOf("_") + 1);
	}
	
	public static void createSchema(String...names) throws ClassNotFoundException, SQLException {
		List<String> queries = new ArrayList<String>();
		for(String s : names) {
			queries.add("CREATE SCHEMA `"+s+"` DEFAULT CHARACTER SET utf8 ;");
		}
		Connection con = DbUtils.getConnection(Constants.MYSQL_HOST, Constants.MYSQL_PORT, "", Constants.MYSQL_USER_NAME, Constants.MYSQL_PASSWORD);
		DbUtils.batchUpdate(con, queries);
	log.info("Schema Created Successfully");
	}

}