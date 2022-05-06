package com.gn.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import com.gn.model.Profile;
import com.gn.service.GnMap;
import com.gn.util.GnUtil;

public class FileOperations {

	public static List<String> getFiles(String path) {
		List<String> list = new ArrayList<String>();
		File dir = new File(path);
		for (File f : dir.listFiles()) {
			String p = f.getAbsolutePath();
			list.add(p);
		}
		return list;
	}

	public static List<String> getFileExtensions(String path) throws IOException {
		List<String> list = new ArrayList<String>();
		File dir = new File(path);
		for (File f : dir.listFiles()) {
			if (!f.isDirectory()) {
				String p = FilenameUtils.getExtension(f.getAbsolutePath());
				list.add(p);
			}
		}
		return list;
	}

	private static String getSimpleDateFormat(long l) {
		return new SimpleDateFormat("dd-MM-yyyy").format(l);
	}

	public static Map<String, Object> getCreationTime(String source, boolean isExtensions) throws IOException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Path path = null;
		BasicFileAttributes attributes;
		for (String src : getFiles(source)) {
			path = Paths.get(src);
			attributes = Files.readAttributes(path, BasicFileAttributes.class);
			String time = getSimpleDateFormat(attributes.lastModifiedTime().toMillis());
			if (!isExtensions)
				map.put(src, time);
			else
				map.put(FilenameUtils.getExtension(src), time);
		}
		return map;
	}

	public static Map<String, Object> getExtensionsByTime(String source) throws IOException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Path path = null;
		BasicFileAttributes attributes;
		for (String src : getFiles(source)) {
			path = Paths.get(src);
			attributes = Files.readAttributes(path, BasicFileAttributes.class);
			String time = getSimpleDateFormat(attributes.lastModifiedTime().toMillis());
			map.put(FilenameUtils.getExtension(src), time);
		}
		return map;
	}

//	public static String createFolders(String source, String path) throws IOException {
//		Map<String, Object> map = getCreationTime(path);
//		    for (Map.Entry<String, Object> entry : map.entrySet()) {
//		        System.out.println(entry.getKey() + ":" + entry.getValue());
//		        String s=  Utility.getDirectory(source, entry.getValue().toString());
//		        System.out.println(s);
//		    }
//			return "Done";
//	}
	public static String createFolders(String source, String path, boolean isExtensions) throws IOException {
		Map<String, Object> map;
		Set<Object> set;
		if (isExtensions) {
			map = getCreationTime(path, true);
			set = GnUtil.convertMapToSet(map, true);
		} else {
			map = getCreationTime(path, false);
			set = GnUtil.convertMapToSet(map, false);
		}
		for (Object entry : set) {
//		        System.out.println(entry.getKey() + ":" + entry.getValue());
			String s = GnUtil.getDirectory(source, entry.toString());
			System.out.println(s);
		}
		return "Done";
	}

	private static File file(String path) {
		return new File(path);
	}

	public static String moveFiles(String source, String destination, boolean flag) throws IOException {
		Map<String, Object> map = getCreationTime(source, false);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Path fileName = Paths.get(entry.getKey()).getFileName();
			Files.copy(file(entry.getKey()).toPath(),
					file(destination + "\\" + entry.getValue() + "\\" + fileName).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		}
		if (flag) {
			file(source).delete();
		}
		return "Moved";
	}

	public static String transferFiles(String source, String destination, boolean flag) throws IOException {
		if (flag) {
			Map<String, Object> map = getCreationTime(source, false);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Path fileName = Paths.get(entry.getKey()).getFileName();
				Files.move(file(entry.getKey()).toPath(),
						file(destination + "\\" + entry.getValue() + "\\" + fileName).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}
		} else {
			Map<String, Object> map = getCreationTime(source, false);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Path fileName = Paths.get(entry.getKey()).getFileName();
				Files.copy(file(entry.getKey()).toPath(),
						file(destination + "\\" + entry.getValue() + "\\" + fileName).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
		return "Moved";
	}
	
	public static GnMap search(String dir, String fileExtension, boolean needFile) throws IOException {
		GnMap map = new GnMap();
		Path path = Paths.get(dir);
		List<String> result = null;
		if (!Files.isDirectory(path)) throw new IllegalArgumentException("Path must be a directory!");
		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(p -> !Files.isDirectory(p)).map(p -> p.toString().toLowerCase())
					.filter(f -> f.endsWith(fileExtension)).collect(Collectors.toList());
		}
		if (needFile) {
			File file = new File("G:\\searchedFiles");
			if (!file.exists())
				file.mkdirs();
			FileWriter writer = new FileWriter(file + "\\searched for " + fileExtension + ".txt");
			writer.write("Searched On " + new Date() + System.lineSeparator());
			writer.write("========================================" + System.lineSeparator());
			for (String str : result) {
				writer.write(str + System.lineSeparator());
			}
			writer.write("========================================" + System.lineSeparator());
			writer.write("Searched By " + Profile.getMyName() + System.lineSeparator());
			writer.write("IP Address: " + Profile.getMyIp() + System.lineSeparator());
			writer.write("Operating System: " + Profile.getOs() + System.lineSeparator());
			writer.write("========================================" + System.lineSeparator());
			writer.close();
		}
		map.put("listData", result);
		return map;
	}

	@SuppressWarnings("unlikely-arg-type")
	public static List<File> getDirs(File folder) {
		File[] fileList = folder.listFiles(File::isDirectory);
		List<File> dirs = Arrays.asList(fileList);
		dirs.remove("G:\\System Volume Information");
		return (fileList == null) ? null : dirs;
	}
}
