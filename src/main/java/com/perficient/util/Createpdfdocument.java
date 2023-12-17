package com.perficient.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Createpdfdocument {
	//PDFformating formating;
	public static Document document;
	static int total = 0;
	static int passCount = 0;
	static int failCount = 0;
	static int contentCounter =0;
	static String File;
	
	static String failureMessage = null;
    /*public static void iTextPDF(String className, String actualStatus) throws Exception{
    	
    	total = total + 1;
    	float[] pointColumnWidths = { 1f, 0.75f, 1.5f };
    	PdfPTable dataRowTable = new PdfPTable(pointColumnWidths);
		// Data Cells
		PdfPCell cell4 = new PdfPCell(new Paragraph("\n"+className+"\n"));
		PdfPCell cell5;
		PdfPCell cell6;
		if (actualStatus == "pass")
		{
			cell5 = new PdfPCell(new Paragraph("PASS"));
			cell5.setBackgroundColor(BaseColor.GREEN);
			cell6 = new PdfPCell(new Paragraph(""));
			passCount = passCount + 1;
		}
		
		else
		{
			cell5 = new PdfPCell(new Paragraph("FAIL"));
			cell5.setBackgroundColor(BaseColor.RED);  
			if (failureMessage == null)
			{
				cell6 = new PdfPCell(new Paragraph("Please check detailed report for the failure reason"));
			}
			else 
			{
				cell6 = new PdfPCell(new Paragraph(failureMessage));
			}
			failCount = failCount + 1;
		}
		
		dataRowTable.addCell(cell4);   
		dataRowTable.addCell(cell5);
		dataRowTable.addCell(cell6);
		dataRowTable.setWidths(pointColumnWidths);      
		document.add(dataRowTable);
		failureMessage = null;
        
    }
    
    public static void openDoc() throws FileNotFoundException, DocumentException
    {
    	SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd_hh.mm.ss");
    	String dt = date.format(new Date());
		File ="./test-output/Report-"+dt+".pdf";
    	//String File ="C:\\Users\\anushree.kharwade\\Downloads\\KaustubhZAssignment\\sample.pdf";
    	OutputStream fos = new FileOutputStream(new File(File));
		document = new Document();
		PdfWriter.getInstance(document, fos);
		document.open();
		
    }
		
	public static void defaultContent (String sampleSuite, String environment) throws DocumentException
	{
		if (contentCounter==0)
		{
			document.add(new Paragraph("               Suite Name: "+sampleSuite));
			if(environment.equalsIgnoreCase("test"))
			{
				document.add(new Paragraph("               Environment Name: Stage"));
			}
			else if(environment.equalsIgnoreCase("qa"))
			{
				document.add(new Paragraph("               Environment Name: QA"));
			}
			else if(environment.equalsIgnoreCase("dev"))
			{
				document.add(new Paragraph("               Environment Name: Dev"));
			}
			
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));

			// Header Table
			float[] pointColumnWidths = { 1f, 0.75f, 1.5f };
			PdfPTable headerRowTable = new PdfPTable(pointColumnWidths);
			// Header Cells
			PdfPCell cell1 = new PdfPCell(new Paragraph("Test Case"));
			cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			PdfPCell cell2 = new PdfPCell(new Paragraph("Status"));
			cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);        
			PdfPCell cell3 = new PdfPCell(new Paragraph("Exception"));
			cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
			headerRowTable.addCell(cell1);   
			headerRowTable.addCell(cell2);
			headerRowTable.addCell(cell3);
			headerRowTable.setWidths(pointColumnWidths);      
			document.add(headerRowTable);
			
			contentCounter = 1;
		}
		
	}
		
    
    public static void closeDoc() throws DocumentException
    {
    	float[] pointColumnWidths = { 1f, 0.75f, 1.5f };
		PdfPTable footerTable = new PdfPTable(pointColumnWidths);
		DecimalFormat frmt = new DecimalFormat();
		frmt.setMaximumFractionDigits(2);
		// Header Cells
		PdfPCell cell1 = new PdfPCell(new Paragraph("Total testcases executed " +total));
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		PdfPCell cell2 = new PdfPCell(new Paragraph("Pass : "+passCount+ " , Fail : " +failCount));
		cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		int totalCount = passCount + failCount;
		String passPercent = frmt.format(((float)passCount/ (float)totalCount) * 100);
		
		System.out.println("pass percent is "+passPercent+ "passCount " +passCount+ "total "+totalCount);
		PdfPCell cell3 = new PdfPCell(new Paragraph("Pass Percentage "+passPercent+ "%"));
		cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
		footerTable.addCell(cell1);   
		footerTable.addCell(cell2);
		footerTable.addCell(cell3);
		footerTable.setWidths(pointColumnWidths);      
		document.add(footerTable);
    	document.close();
    }
    
    public static void addFailureMessage(String msg) throws DocumentException
    {
    	//doc.add(new Paragraph("Test Failure Reason : "+msg));
    }
    
    public static void setFailureMessage(String msg)
    {
    	if (failureMessage != null)
    	{
    		failureMessage = failureMessage +" , "+msg;
    	}
    	else
    	{
    		failureMessage = msg;
    	}
    	
    }*/
	
	/*
	 * public static void main(String[] args) throws Exception {
	 * System.setProperty("webdriver.chrome.driver",
	 * "C:\\Users\\anushree.kharwade\\Downloads\\chromedriver_win32\\chromedriver.exe"
	 * ); WebDriver driver = new ChromeDriver();
	 * driver.get("https://www.google.com/"); try {
	 * driver.findElement(By.id("Bing")); }catch(Exception e) { iTextPDF();
	 * System.out.println("Error found::"); } }
	 */
	 
}

