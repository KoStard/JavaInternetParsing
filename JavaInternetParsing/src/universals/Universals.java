package universals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Universals {
	public static String getCellString(Cell cell) {
		String res;
		switch (cell.getCellTypeEnum()) {
        case BOOLEAN:
            res = String.valueOf(cell.getBooleanCellValue());
            break;
        case STRING:
        	res = String.valueOf(cell.getRichStringCellValue().getString());
            break;
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
            	res = String.valueOf(cell.getDateCellValue());
            } else {
            	res = String.valueOf((int)cell.getNumericCellValue());
            }
            break;
        case FORMULA:
        	res = String.valueOf(cell.getCellFormula());
            break;
        case BLANK:
        	res = String.valueOf("");
            break;
        default:
        	res = String.valueOf("");
		}
		return res;
	}
	
	public static Cell[][] getData(String filename) throws Exception {
		HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(new File(filename));
		Sheet sheet = workbook.getSheetAt(0);
		Cell[][] res = new Cell[sheet.getLastRowNum()][];
		for (int r = 0; r < res.length; r++) {
			Row row = sheet.getRow(r);
			if (row == null) continue;
			Cell[] temp = new Cell[row.getLastCellNum()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = row.getCell(i);
			}
			res[r] = temp;
		}
		return res;
	}
	
	public static Cell[][] removeCopies(Cell[][] data, int uniqueColIndex) {
		List<Integer> indices = new ArrayList<Integer>();
		Cell[][] res;
		Set<String> older = new HashSet<String>();
		for (int i = 1; i < data.length; i++) {
			Cell cell = data[i][uniqueColIndex];
			String temp = getCellString(cell);
			if (!older.contains(temp)) {
				older.add(temp);
				indices.add(i);
			}
		}
		res = new Cell[indices.size()+1][];
		res[0] = data[0];
		int newIndex = 1;
		for (int index : indices) {
			res[newIndex] = data[index];
			res[newIndex][0].setCellValue(newIndex);
			newIndex++;
		}
		return res;
	}
	
	public static void exportData(Cell[][] data, String fileName) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		int rowIndex = -1;
		for (int i = 0; i < data.length; i++) {
			rowIndex ++;
			Cell[] rowCells = data[i];
			Row row = sheet.createRow(rowIndex);
			int cellIndex = 0;
			try {
			for (Cell cellData : rowCells) {
				Cell cell = row.createCell(cellIndex++);
				switch (cellData.getCellTypeEnum()) {
		        case BOOLEAN:
		            cell.setCellValue(cellData.getBooleanCellValue());
		            break;
		        case STRING:
		        	String text = cellData.getRichStringCellValue().getString();
		        	if (text == " ") text = "";
		        	cell.setCellValue(text);
		            break;
		        case NUMERIC:
		            if (DateUtil.isCellDateFormatted(cellData)) {
		            	cell.setCellValue(cellData.getDateCellValue());
		            } else {
		            	cell.setCellValue((double)cellData.getNumericCellValue());
		            }
		            break;
		        case FORMULA:
		        	cell.setCellValue(cellData.getCellFormula());
		            break;
		        case BLANK:
		        	cell.setCellValue("");
		            break;
		        default:
		        	cell.setCellValue("");
				}
			}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
	
	// Facebook
	public static String[] readStringsFromFile(String filename) throws Exception{
		FileReader fr = new FileReader(new File(filename));
		Scanner scanner = new Scanner(fr);
		List<String> res = new ArrayList<String>();
		while(scanner.hasNextLine()) {
			res.add(scanner.nextLine());
		}
		scanner.close();
		return res.toArray(new String[0]);
	}
	
	public static void writeStringsToFile(String[] strs, String filename) throws Exception{
		FileWriter fw = new FileWriter(new File("res/output.txt"));
		for (String block : strs) {
			fw.write(block+"\n");
		}
		fw.close();
	}
}