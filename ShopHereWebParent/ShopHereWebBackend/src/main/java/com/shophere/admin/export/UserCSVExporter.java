package com.shophere.admin.export;

import java.util.Date;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCSVExporter extends AbstractExporter {

	public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv");
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "User ID", "E-Mail", "First Name", "Last Name", "Roles", "Enabled" };
		String[] fieldMapping = { "id", "email", "firstName", "lastname", "roles", "enabled" };
		csvBeanWriter.writeHeader(csvHeader);
		for (User user : listUsers) {
			csvBeanWriter.write(user, fieldMapping);
		}

		csvBeanWriter.close();

	}
}
