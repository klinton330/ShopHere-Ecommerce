package com.shophere.admin.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserPDFExporter extends AbstractExporter {
	public void export(List<User> lisUsers, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "application/pdf", ".pdf");
		Document document=new Document(PageSize.A4);
		PdfWriter.getInstance(document, httpServletResponse.getOutputStream());
		document.open();
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		Paragraph paragraph=new Paragraph("List Of Users",font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		
		
		  PdfPTable table=new PdfPTable(6);
		  table.setWidthPercentage(100f);
		  table.setSpacingBefore(10);
		  table.setWidths(new float[] {1.5f,3.5f,3.0f,3.0f,3.0f,1.5f});
		  writeTableHeader(table); 
		
		  writeTableData(table,lisUsers);
		  document.add(table);
		  document.close();
	}

	private void writeTableData(PdfPTable table, List<User> lisUsers) {
		for(User user:lisUsers)
		{
			table.addCell(String.valueOf(user.getId()));
			table.addCell(String.valueOf(user.getEmail()));
			table.addCell(String.valueOf(user.getFirstName()));
			table.addCell(String.valueOf(user.getLastname()));
			table.addCell(String.valueOf(user.getRoles().toString()));
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell=new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(16);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("User ID",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("E-Mail",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Roles",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled",font));
		table.addCell(cell);
		
	}
}
