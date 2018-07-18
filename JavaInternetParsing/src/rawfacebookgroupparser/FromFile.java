package rawfacebookgroupparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import universals.Universals;

public class FromFile {
	public static void main(String[] args) throws Exception{
//		String[] res = getPostTextsFromFile("res/YSMU Confessions - Главная _ Facebook.html"); - This line will not work, because the file was compressed
		String[] lines = Universals.readStringsFromFile("res/rawfacebookgroupparserRes/output.txt");
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Pattern pattern = Pattern.compile("^\\s*#(\\d+) ([\\s\\S]+)$");
		Matcher matcher;
		for (int i = 0; i < lines.length; i++) {
			matcher = pattern.matcher(lines[i]);
			if (!matcher.matches()) continue;
			int index = Integer.parseInt(matcher.group(1));
			String content = matcher.group(2);
			Row row = sheet.createRow(i);
			row.createCell(0).setCellValue(index);
			row.createCell(1).setCellValue(content);
		}
		FileOutputStream fileout = new FileOutputStream(new File("res/rawfacebookgroupparserRes/confessions.xls"));
		workbook.write(fileout);
		fileout.close();
		workbook.close();
	}
	
	public static String[] getPostTextsFromFile(String filename) throws Exception{
		Document doc = Jsoup.parse(new FileInputStream(new File(filename)), "UTF-8", "");
		Elements blocks = doc.getElementsByClass("_5pbx userContent _3ds9 _3576");
		String[] res = new String[blocks.size()];
		int index = 0;
		for (Element block : blocks) {
			res[index++] = block.text();
		}
		return res;
	}
}
