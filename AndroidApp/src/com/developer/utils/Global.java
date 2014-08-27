package com.developer.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Global {
	public static String getTime(long mtime) {
		return (new SimpleDateFormat("hh:mm:ss a")).format(new Date(mtime));
	}
}
