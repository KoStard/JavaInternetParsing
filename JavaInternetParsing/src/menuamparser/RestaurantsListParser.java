package menuamparser;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RestaurantsListParser {
	public RestaurantsListParser(String URL, String filename) throws Exception {
		Document doc = Jsoup.connect(URL).get();
		Elements items = doc.getElementsByClass("item");
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		String[] headers = new String[] {"Rating", "Title", "Notes", "OpenTime", "Delievery Notes", "URL"};
		Row headersRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			headersRow.createCell(i).setCellValue(headers[i]);
		}
		int rowIndex = 1;
		for (Element item : items) {
			double rating = -1;
			try {
				String ratingText = item.getElementsByClass("new_favorites_and_rates_block").get(0).getElementsByClass("fr").get(0).getElementsByClass("new_rates_block").get(0).text();
				rating = Double.parseDouble(ratingText);
			} catch (Exception e){
			}
			String itemURL = item.getElementsByClass("list-logo").get(0).getElementsByTag("a").attr("href").replaceAll("/{2,}", "/");
			Element titleCont = item.getElementsByClass("list-title").get(0);
			String title = titleCont.getElementsByClass("title").get(0).text();
			String notes = titleCont.getElementsByClass("restType").get(0).text();
			String openTime = titleCont.getElementsByClass("new_list_time_block").get(0).getElementsByClass("new_list_time_block_inner").get(0).text();
			String delieveryNotes = item.getElementsByClass("new_list_delivery_block").get(0).getElementsByClass("item_restDistance").get(0).text();
			
			Row row = sheet.createRow(rowIndex);
			if (rating > 0)
				row.createCell(0).setCellValue(rating);
			else 
				row.createCell(0).setCellValue(""); // Was " " and was working
			row.createCell(1).setCellValue(title);
			row.createCell(2).setCellValue(notes);
			row.createCell(3).setCellValue(openTime);
			row.createCell(4).setCellValue(delieveryNotes);
			row.createCell(5).setCellValue(itemURL);
			
			rowIndex ++;
		}
		FileOutputStream fileout = new FileOutputStream(new File(filename));
		workbook.write(fileout);
		fileout.close();
		workbook.close();
	}
}
