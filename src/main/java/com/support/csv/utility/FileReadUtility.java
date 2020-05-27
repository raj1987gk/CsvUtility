package com.support.csv.utility;

import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class FileReadUtility {

	
	public static InputStream getFileAsStream(String fileName) {
	
		InputStream inputstream = null;
		
		try {
		ClassPathResource classPathResource = new ClassPathResource("/"+fileName);
		 inputstream = classPathResource.getInputStream();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputstream;
	}
	
}
