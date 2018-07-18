package listamparser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CarParser {
	public String hostURL = "https://www.list.am/category/23/";
	public int parseInt(String text) {
		int res = 0, sign = 1;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (c) {
			case '-':
				if (sign == 1) sign = -1;
				break;
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
				res = res*10 + Integer.parseInt(""+c);
				break;
			}
		}
		return res;
	}
	public CarParser(String fileName) throws Exception{
		int currentIndex = 1;
		int maxIndex = 250;
		
//		String fileName = "res/data.xls";
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("No.");
		header.createCell(1).setCellValue("Name");
		header.createCell(2).setCellValue("Price");
		header.createCell(3).setCellValue("URL");
		
		Set<String> older = new HashSet<String>();
		
		int rowIndex = 0;
		
		while (currentIndex <= maxIndex) {
			Document doc;
			try {
				System.out.println("Trying "+currentIndex);
				doc = Jsoup.connect(hostURL+currentIndex++).get();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			Element contentr = doc.getElementById("contentr");
			Elements dls = contentr.getElementsByClass("dl");
			Elements gls = null;
			for (Element dl : dls) {
				if (gls == null) gls = dl.getElementsByClass("gl");
				else gls.addAll(dl.getElementsByClass("gl"));
			}
			Elements as = null;
			for (Element gl : gls) {
				if (as == null) as = gl.getElementsByTag("a");
				else as.addAll(gl.getElementsByTag("a"));
			}
			for (Element a : as) {
				String URL = a.attr("href");
				if (older.contains(URL)) {
					continue;
				}
				older.add(URL);
				++rowIndex;
				String name = a.getElementsByClass("l").get(0).text();
				String priceString = a.getElementsByClass("l2").get(0).text();
				int price = parseInt(priceString);
				HSSFRow row = sheet.createRow(rowIndex);
				row.createCell(0).setCellValue(rowIndex);
				row.createCell(1).setCellValue(name);
				row.createCell(2).setCellValue(price);
				row.createCell(3).setCellValue("https://www.list.am"+URL);
			}
			System.out.println("Found "+rowIndex+" cases.");
			Thread.sleep(500);
		}
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
}
