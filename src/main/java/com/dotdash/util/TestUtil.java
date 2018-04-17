package com.dotdash.util;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebElement;

import com.dotdash.testbase.TestBase;



public class TestUtil extends TestBase {

	public static long PAGE_LOAD_TIMEOUT = 30;
	public static long IMPLICIT_WAIT = 30;
	public static XSSFWorkbook book;
	public static XSSFSheet sheet;
	public static String TESTDATA_PATH = "C:\\Users\\Meena.Somasundaram\\eclipse-workspace\\Cenlar\\src\\main\\java\\com\\cenlar\\qa\\data\\CenlarLogin.xlsx";

	public static void pageScroll() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight);");
	}

	public static void pageScrollToView(WebElement elem) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true)", elem);
	}

	public static void pageScrollUp() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(document.body.scrollHeight,0);");
	}

	public static Object[][] getTestData() {
		FileInputStream file = null;
		try {
			file = new FileInputStream(TESTDATA_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			book = new XSSFWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sheet = book.getSheetAt(0);
		int rows = sheet.getLastRowNum();
		int cols = sheet.getRow(0).getLastCellNum();
		Object[][] data = new Object[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
			}
		}

		return data;
	}

	public static void takeScreenshotAtEndOfTest() throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");

		FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"));

	}
}
