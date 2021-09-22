package com.tss.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.tss.util.Utility;

public class TestFolder {
	
	public void removeLine(String lineContent) throws IOException
	{
	    File file = new File("myFile.txt");
	    List<String> out = Files.lines(file.toPath())
	                        .filter(line -> !line.contains(lineContent))
	                        .collect(Collectors.toList());
	    Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	public static void remove(String filePath, String searchString, boolean isReplace, String replaceWith) throws Exception {
		File file = new File(filePath);
		List<String> lines = FileUtils.readLines(file);
		if(!isReplace) {
		 List<String> updatedLines = lines.stream().filter(s -> !s.contains(searchString)).collect(Collectors.toList());
		 FileUtils.writeLines(file, updatedLines, false);
		} else {
			String s = Utility.getJsonFromFile(filePath).replace(searchString, replaceWith);
			if(isReplace) 
//			s = s.replaceAll("#\\?.*?#\\?", "");
//			s = s.replaceAll("(?m)^[ \t]*\r?\n", "");
			s = (!isReplace) ? s.replaceAll("#\\?.*?#\\?", "").replaceAll("(?m)^[ \t]*\r?\n", "") : s.replaceAll("#\\?", "").replaceAll("(?m)^[ \t]*\r?\n", "");
			Utility.fileWriter(s, new File(filePath));
			
		}
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		remove("C:\\Users\\G NARESH\\Downloads\\remove.txt", "#remove#",true,"");
		System.out.println("Donee");
		
		 File dir = new File("C:\\Users\\G NARESH\\Downloads");
	        File [] files  = dir.listFiles();
	        Arrays.sort(files, new Comparator(){
	            public int compare(Object o1, Object o2) {
	                return compare( (File)o1, (File)o2);
	            }
	            private int compare( File f1, File f2){
	                long result = f2.lastModified() - f1.lastModified();
	                System.out.println(f2.lastModified());
	                if( result > 0 ){
	                    return 1;
	                } else if( result < 0 ){
	                    return -1;
	                } else {
	                    return 0;
	                }
	            }
	        });
	        List<String> isF = new ArrayList<String>();
	        
	        for(File s : files) {
//	        	String p =  FileUtils.readFileToString(s, "UTF-8");
	        	String p = s.getAbsolutePath();
	        	isF.add(p);
	        }
	        System.out.println("String fiels");
	        System.out.println(isF);
	        Map<String, Object> map = new LinkedHashMap<String, Object>();
	        
	        
 	        System.out.println( Arrays.asList(files ));
	        String s = "C:\\Users\\G NARESH\\Downloads\\RSA algorithm.txt";
	        String ss = "E:\\All Downloads\\test\\RSA algorithm.txt";
	        File source = new File(s);
	        File des = new File(ss);
	        Path path = Paths.get(isF.get(1));
	        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        
	        map.put(path.toString(), dateFormat.format(attributes.creationTime().toMillis()));
	        System.out.println("This is Map");
	        System.out.println(map);
	        System.out.println(dateFormat.format(attributes.lastModifiedTime().toMillis()));
	        System.out.println(dateFormat.format(attributes.lastAccessTime().toMillis()));
	        System.out.println(dateFormat.format(attributes.creationTime().toMillis()));
	        
	        Files.copy(source.toPath(), des.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        System.out.println("File copied");
	        if(source.delete()) {
	        	System.out.println("file delete");
	        }
	       
	}
	//this is restore method
	public static final String DESTINATION = "G:\\InputFiles\\Licenses.css";
	 public static void restoreContent(File existingFile, File auditFile) throws IOException {
 		String existingFilePath = existingFile.getAbsolutePath();
 		String auditFilePath = auditFile.getAbsolutePath();
 		File tempFile =  new File(existingFilePath.substring(0, existingFilePath.indexOf('.'))+ "temp." + existingFilePath.substring(existingFilePath.indexOf('.') + 1));
 	    Files.move(existingFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
     Files.move(auditFile.toPath(), new File(existingFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
	    Files.move(tempFile.toPath(), new File(auditFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	

}
