package com.shophere.admin.export;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(18);
		cellStyle.setFont(font);
		createCell(row, 0, "User Id", cellStyle);
		createCell(row, 1, "Email", cellStyle);
		createCell(row, 2, "FirstName", cellStyle);
		createCell(row, 3, "LastName", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enable", cellStyle);
	}

	private void writeDataLines(List<User> listUsers) {
		int rowIndex = 1;
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		for (User user : listUsers) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			createCell(row, 0, user.getId(), cellStyle);
			createCell(row, 1, user.getEmail(), cellStyle);
			createCell(row, 2, user.getFirstName(), cellStyle);
			createCell(row, 3, user.getLastname(), cellStyle);
			createCell(row, 4, user.getRoles().toString(), cellStyle);
			createCell(row, 5, user.isEnabled(), cellStyle);
		}
	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		if (value instanceof Integer)
			cell.setCellValue((Integer) value);
		else if (value instanceof Boolean)
			cell.setCellValue((Boolean) value);
		else
			cell.setCellValue((String) value);

		cell.setCellStyle(cellStyle);
	}

	public void export(List<User> lisUsers, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "application/octet-stream", ".xlsx");
		writeHeaderLine();
		writeDataLines(lisUsers);
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);
		outputStream.close();
	}

}
