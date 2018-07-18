package listamparser;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import universals.SortRowsIndicesByNthElement;
import universals.Universals;	

public class Finder {
	List<Integer> resultIndexes = new ArrayList<Integer>();
	Cell[][] data;
	Cell[] headers;
	public String text = "";
	public Finder(Cell[][] data) throws Exception {
		this.headers = data[0];
		this.data = new Cell[data.length-1][];
		for (int i = 1; i < data.length; i++) {
			this.data[i-1] = data[i];
		}
	}
	
	public Finder search(String text, int col) {
		this.text += "";
		resultIndexes.clear();
		return appendingSearch(text, col);
	}
	
	public Finder appendingSearch(String text, int col) {
		this.text += text;
		for (int i = 0; i < data.length; i++) {
			if (col == -1) {
				for (Cell cell : data[i]) {
					if (Universals.getCellString(cell).indexOf(text)!=-1) {
						resultIndexes.add(i);
					}
				}
			} else {
				if (col < data[i].length && Universals.getCellString(data[i][col]).indexOf(text)!=-1) {
					resultIndexes.add(i);
				}
			}
		}
		return this;
	}
	public Finder printRes() {
		for (int index : resultIndexes) {
			for (Cell c : data[index]) {
				System.out.print(Universals.getCellString(c));
			}
			System.out.println();
		}
		return this;
	}
	public Finder exportResTo() throws Exception{
		return exportResTo("res/listamparserRes/"+text+".xls");
	}
	public Finder exportResTo(String fileName) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row header = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			header.createCell(i).setCellValue(Universals.getCellString(headers[i]));
		}
		int rowIndex = 0;
		for (int index: resultIndexes) {
			Cell[] rowCells = data[index];
			rowIndex ++;
			Row row = sheet.createRow(rowIndex);
			int cellIndex = 0;
			for (Cell cellData : rowCells) {
				Cell cell = row.createCell(cellIndex++);
				switch (cellData.getCellTypeEnum()) {
		        case BOOLEAN:
		            cell.setCellValue(cellData.getBooleanCellValue());
		            break;
		        case STRING:
		        	cell.setCellValue(cellData.getRichStringCellValue().getString());
		            break;
		        case NUMERIC:
		            if (DateUtil.isCellDateFormatted(cellData)) {
		            	cell.setCellValue(cellData.getDateCellValue());
		            } else {
		            	cell.setCellValue((int)cellData.getNumericCellValue());
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
		}
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		return this;
	}
	
	public Cell[][] getRes(){
		Cell[][] res = new Cell[resultIndexes.size()][];
		int currIndex = 0;
		for (int index : resultIndexes) {
			res[currIndex++] = data[index];
		}
		return res;
	}
	
	public Finder sortBy(int colIndex) {
		resultIndexes.sort(new SortRowsIndicesByNthElement(colIndex, this.data, true));
		return this;
	}
}
