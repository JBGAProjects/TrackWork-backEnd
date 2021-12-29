package com.developsjb.trackwork.backend.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

	private final static String UPLOAD_DIRECTORY = "uploads";

	@Override
	public Resource load(String namePhoto) throws MalformedURLException {
		Path routeFile = getPath(namePhoto);
		log.info(routeFile.toString());
		
		Resource resource = new UrlResource(routeFile.toUri());

		if (!resource.exists() && !resource.isReadable()) {
			 resource = new UrlResource(routeFile.toUri());
			throw new RuntimeException("Error the image could not be loaded.");
		}

		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String nameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
		Path routeFile = getPath(nameFile);
		Files.copy(file.getInputStream(), routeFile);
		return nameFile;
	}

	@Override
	public boolean deletePhoto(String namePhoto) {
		if(namePhoto != null && namePhoto.length() > 0) {
			Path routeLastPhoto = getPath(namePhoto);
			File fileLastPhoto = routeLastPhoto.toFile();
			if(fileLastPhoto.exists() && fileLastPhoto.canRead()) {
				fileLastPhoto.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String namePhoto) {
		return Paths.get(UPLOAD_DIRECTORY).resolve(namePhoto).toAbsolutePath();
	}

}
