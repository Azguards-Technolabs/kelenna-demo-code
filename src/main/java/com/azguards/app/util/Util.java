package com.azguards.app.util;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Util {

	public static Integer perPageCount = 10;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");

	public static String getOneTimePassword() throws Exception {
		Random random = new Random();
		return "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9);
	}

	public static String getOneTimePasswordForReset() throws Exception {
		Random random = new Random();
		return "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + ""
				+ random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9);
	}

	public static int getYearsBetweenDates(Date first, Date second) {
		return (int) ChronoUnit.YEARS.between(first.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				second.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public static String[] getEnumNames(Class<? extends Enum<?>> e) {
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}

	public static boolean containsIgnoreCase(List<String> list, String searchKey) {
		return list.stream().anyMatch(e -> e.equalsIgnoreCase(searchKey));
	}

	public static boolean contains(List<String> list, String searchKey) {
		return list.stream().anyMatch(e -> e.equals(searchKey));
	}

	public static boolean contains(List<Date> list, Date searchDate) {
		return list.stream().anyMatch(e -> e.toInstant().compareTo(searchDate.toInstant()) == 0);
	}
}
