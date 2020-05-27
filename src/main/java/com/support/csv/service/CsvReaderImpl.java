package com.support.csv.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CsvReaderImpl {

	//          final CSVPrinter printer = CSVFormat.DEFAULT.withHeader("H1", "H2").print(out)

	@PostConstruct
	public void readCsv() {
		try {
			String fileName = "data.csv";
			ClassPathResource classPathResource = new ClassPathResource("/"+fileName);
			File file = classPathResource.getFile();
			Reader reader = new FileReader(file);
			CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
			List<String> headerList = parser.getHeaderNames();
			Iterator<CSVRecord> records =  parser.iterator();
			CSVRecord csvRecordLine1 =  records.next();//for header
			System.err.println("Header: "+csvRecordLine1.toString());
			
			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get("export.csv"));
			CSVFormat csvFormat =  CSVFormat.DEFAULT.withHeader("policyID,statecode,county,eq_site_limit,hu_site_limit,fl_site_limit,"
					+ "fr_site_limit,tiv_2011,tiv_2012,eq_site_deductible,"
					+ "hu_site_deductible,fl_site_deductible,fr_site_deductible,point_latitude,point_longitude,line,construction,point_granularity");
			CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);
			
			while (records.hasNext()) {
				CSVRecord csvRecord = (CSVRecord) records.next();
				HashMap<String, String> map = new HashMap<>();
				StringBuffer buffer = new StringBuffer();
				for (int index=0;index<headerList.size();index++) {
					String recordVal = csvRecord.get(headerList.get(index));
					buffer.append(recordVal);
					if(index+1<headerList.size())
						buffer.append(";");
				}    
				csvPrinter.printRecord(buffer);
			}
			
			csvPrinter.flush();
			csvPrinter.close();
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
}
