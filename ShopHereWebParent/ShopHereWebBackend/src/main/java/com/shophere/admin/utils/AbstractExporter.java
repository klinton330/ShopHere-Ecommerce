package com.shophere.admin.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {

	public void setResponseHeader(HttpServletResponse httpServletResponse, String contentType, String extention) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = "users_" + timeStamp + extention;
		httpServletResponse.setContentType(contentType);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		httpServletResponse.setHeader(headerKey, headerValue);
	}

}
