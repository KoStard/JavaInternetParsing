package menuamparser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import universals.Universals;

public class MenuAmParser {
	public MenuAmParser() throws Exception {
//		For parcing the restaurants list
		new RestaurantsListParser("https://www.menu.am/yerevan/delivery/restaurant/type.html?status=all&sort=default", "res/Restaurants.xls");
		
		
//		For sorting restaurants list
		
//		Cell[][] data = Universals.getData("res/Հայկական.xls");
//		Cell[] headers = data[0];
//		Cell[][] clearData = Arrays.copyOfRange(data, 1, data.length);
//		Arrays.sort(clearData, new SortRowsByNthElement(2, true));
//		Universals.exportData(ArrayUtils.addAll(new Cell[][] {headers}, clearData), "res/ՀայկականSortedByNotes.xls");
		
//		For parsing the menu
//		parseMenu("res/Restaurants.xls", "res/AllWithURLs.xls");
		

		Cell[][] data = Universals.getData("res/Restaurants.xls");
		for (int i = 1; i < data.length; i++) {
			data[i][5].setCellValue("https://www.menu.am" + (data[i][5].getStringCellValue()).replaceAll("/yerevan/restaurant/", "/yerevan/delivery/restaurant/"));
		}
		Universals.exportData(data, "res/RestaurantsCorrect.xls");
		
	}
	
	
//	Now starting the main process of parsing the items
	public static void parseMenu(String inputFileName, String outputFileName) throws Exception {
		Cell[][] data = Universals.getData(inputFileName);
		Cell[][] clearData = Arrays.copyOfRange(data, 1, data.length);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		
		int cycleIndex = 0;
		for (Cell[] row : clearData) {
			String URL = "https://www.menu.am" + (row[5].getStringCellValue()).replaceAll("/yerevan/restaurant/", "/yerevan/delivery/restaurant/");
			MenuParser.parseMenu(row[1].getStringCellValue(), URL, sheet);
			System.out.println(String.format(">>>  Checkpoint N:%d/%d - %d", ++cycleIndex, clearData.length, sheet.getLastRowNum()));
			Thread.sleep(500);
		}
		System.out.println("Finished creating workbook");
		FileOutputStream fileout = new FileOutputStream(new File(outputFileName));
		workbook.write(fileout);
		fileout.close();
		workbook.close();
		System.out.println("Saved");
	}
}