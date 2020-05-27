package com.support.csv.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsvUtilityController {

	
	public String readCsv() {
		
		return "processed successfully";
	}
	
	
	public static void main(String[] args) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmSSS");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
	}
}
