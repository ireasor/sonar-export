package io.github.ireasor.sonarexport.excelbuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import io.github.ireasor.sonarexport.entity.Component;
import io.github.ireasor.sonarexport.entity.Issue;
import io.github.ireasor.sonarexport.entity.Measure;

public class ExcelBuilder {

	private HSSFWorkbook workbook;

	public ExcelBuilder() {
		workbook = new HSSFWorkbook();
	}

	public void addIssueSheet(String project, List<Issue> searchResults) {
		HSSFSheet sheet = workbook.createSheet(project.replaceAll(":", " ") + " Issues");
		populateIssueSheet(sheet, searchResults);
	}

	private void populateIssueSheet(HSSFSheet sheet, List<Issue> searchResults) {
		Row headerRow = sheet.createRow(0);
		Cell headerCell0 = headerRow.createCell(0);
		headerCell0.setCellValue("Type");
		Cell headerCell1 = headerRow.createCell(1);
		headerCell1.setCellValue("Component");
		Cell headerCell2 = headerRow.createCell(2);
		headerCell2.setCellValue("Rule");
		Cell headerCell3 = headerRow.createCell(3);
		headerCell3.setCellValue("Message");
		Cell headerCell4 = headerRow.createCell(4);
		headerCell4.setCellValue("Severity");

		int rowNum = 1;
		for (Issue issue : searchResults) {
			Row row = sheet.createRow(rowNum);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue(issue.getType());

			Cell cell1 = row.createCell(1);
			cell1.setCellValue(issue.getComponent());

			Cell cell2 = row.createCell(2);
			cell2.setCellValue(issue.getRule());

			Cell cell3 = row.createCell(3);
			cell3.setCellValue(issue.getMessage());

			Cell cell4 = row.createCell(4);
			cell4.setCellValue(issue.getSeverity());

			rowNum++;
		}

		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
	}
	
	public void addCoverageSheet(String project, List<Component> components) {
		HSSFSheet sheet = workbook.createSheet(project.replaceAll(":", " ") + " Coverage");
		populateCoverageSheet(sheet, components);
	}
	
	private void populateCoverageSheet(HSSFSheet sheet, List<Component> components) {
		Row headerRow = sheet.createRow(0);
		Cell headerCell0 = headerRow.createCell(0);
		headerCell0.setCellValue("Path");
		Cell headerCell1 = headerRow.createCell(1);
		headerCell1.setCellValue("Language");
		Cell headerCell2 = headerRow.createCell(2);
		headerCell2.setCellValue("Coverage");
		Cell headerCell3 = headerRow.createCell(3);
		headerCell3.setCellValue("Uncovered Lines");
		Cell headerCell4 = headerRow.createCell(4);
		headerCell4.setCellValue("Uncovered Conditions");

		int rowNum = 1;
		for (Component component : components) {
			Row row = sheet.createRow(rowNum);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue(component.getPath());

			Cell cell1 = row.createCell(1);
			cell1.setCellValue(component.getLanguage());

			Measure[] measures = component.getMeasures();
			
			for (Measure measure : measures) {
				if (measure.getMetric().equals(Measure.COVERAGE)) {
					Cell cell2 = row.createCell(2);
					cell2.setCellValue(measure.getValue());
				}
				if (measure.getMetric().equals(Measure.UNCOVERED_LINES)) {
					Cell cell3 = row.createCell(3);
					cell3.setCellValue(measure.getValue());
				}
				if (measure.getMetric().equals(Measure.UNCOVERED_CONDITIONS)) {
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(measure.getValue());
				}
			}

			rowNum++;
		}

		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
	}

	public void saveWorkbook(String filePath) throws FileNotFoundException {

		FileOutputStream out = new FileOutputStream(new File(filePath));

		try {
			workbook.write(out);
			System.out.println(filePath + " was successfully saved");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
