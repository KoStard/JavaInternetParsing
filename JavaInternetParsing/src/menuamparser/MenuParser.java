package menuamparser;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MenuParser {
//	Cell[][] data;
	public MenuParser(String restaurantName, String URL, Sheet sheet) throws Exception {
		// "productTitle", "productDescription", "price", "sectionTitle", "restaurantName", "URL"
		Document doc = Jsoup.connect(URL).get();
		Element wrapper = doc.getElementsByClass("wrapper").get(0);
		
		for (Element section : wrapper.getElementsByClass("sections")) {
			String sectionTitle = section.getElementsByClass("menu-cat-title").get(0).text();
			for (Element product : section.getElementsByClass("product")) {
				String productURL = product.child(0).attr("href");
				Element prodContent = product.child(1);
				String productTitle = prodContent.child(0).text();
				String productDescription = prodContent.child(2).text();
				int price = Integer.parseInt(product.child(2).child(1).text().split(" ")[0]);
				Row row = sheet.createRow(sheet.getLastRowNum()+1);
				row.createCell(0).setCellValue(productTitle);
				row.createCell(1).setCellValue(productDescription);
				row.createCell(2).setCellValue(price);
				row.createCell(3).setCellValue(sectionTitle);
				row.createCell(4).setCellValue(restaurantName);
				row.createCell(5).setCellValue(URL);
			}
		}
	}
	public static void parseMenu(String restaurantName, String URL, Sheet sheet) throws Exception {
		Document doc = Jsoup.connect(URL).get();
//		FileWriter fw = new FileWriter(new File("res/doc.html"));
//		fw.write(doc.html());
//		fw.close();
//		return;
		
		
//		Can't use sections, because only first one contains products, others are outside the div
//		for (Element section : wrapper.getElementsByClass("sections")) {
////			if (section.children().size() == 0) continue;
//			String sectionTitle = section.child(1).text();
		
		
		FileWriter fw = new FileWriter(new File("res/errorLog.html"));
		for (Element product : doc.getElementsByClass("product")) {
			try {
			product = product.child(0);
			String productURL = product.child(0).attr("href");
			String sectionTitle = product.child(0).attr("keywords").split(",")[0];
			Element prodContent = product.child(1);
			String productTitle = prodContent.child(0).text();
			String productDescription = prodContent.child(2).text();
			int price = Integer.parseInt(product.child(2).child(1).text().split(" ")[0]);
			Row row = sheet.createRow(sheet.getLastRowNum()+1);
			row.createCell(0).setCellValue(productTitle);
			row.createCell(1).setCellValue(productDescription);
			row.createCell(2).setCellValue(price);
			row.createCell(3).setCellValue(sectionTitle);
			row.createCell(4).setCellValue(restaurantName);
			row.createCell(5).setCellValue(productURL);
			} catch (Exception e) {
				fw.write(product.html()+"\n");
			}
		}
		fw.close();
//		}
	}
}
