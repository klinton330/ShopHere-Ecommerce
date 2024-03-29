package com.shophere.admin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.shophere.admin.controller.CategoryController;

public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	// saveFile(user-photos,Bret-Har.png,multipartFile)
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		// If directory not present create a new Directory
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		// Convert the Multipart to InputStream
		try (InputStream inputStream = multipartFile.getInputStream()) {
			// represents the directory where you want to store the uploaded file.
			Path filePath = uploadPath.resolve(fileName);
			// This line copies the content of the inputStream
			// StandardCopyOption.REPLACE_EXISTING->option specifies that if a file with the
			// same name already exists at the destination, it should be replaced.
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new IOException("could not save file:" + fileName + e.getMessage());
		}
	}

	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted the file:"+file);
					} catch (IOException e) {
						LOGGER.error("could not remove directory:" + dir);
						LOGGER.error(e.getMessage());
					}
				}
				;
			});
		} catch (IOException e) {
			LOGGER.error("could not remove directory:" + dir);
			LOGGER.error(e.getMessage());
		}
		;
	}

	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (Exception e) {
			LOGGER.error("could not remove directory:" + dir);
			LOGGER.error(e.getMessage());
		}
	}
}
