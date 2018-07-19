package universals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

public class SortRowsIndicesByNthElement implements Comparator<Integer>{
	int index = 0;
	Cell[][] data;
	int bigger = 1, smaller = -1;
	public SortRowsIndicesByNthElement(int index, Cell[][] data, boolean reversed) {
		this.index = index;
		this.data = data;
		if (reversed) {
			bigger = -1;
			smaller = 1;
		}
	}
	public int compare(Integer ai, Integer bi) {
		Cell[] a = data[ai];
		Cell[] b = data[bi];
		switch (a[index].getCellTypeEnum()) {
        case BOOLEAN:
            return (a[index].getBooleanCellValue()&b[index].getBooleanCellValue()?bigger:smaller);
        case STRING:
        	if (index == 0 && b[index].getCellTypeEnum() != CellType.STRING) {
        		return smaller;
        	}
//        	String[] temp = new String[] {a[index].getStringCellValue(), b[index].getStringCellValue()};
//        	Arrays.sort(temp);
//            return (temp[1] == a[index].getStringCellValue()?bigger:smaller);
        	return a[index].getStringCellValue().compareTo(b[index].getStringCellValue())*bigger;
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(a[index])) {
//            	Date[] temp1 = new Date[] {a[index].getDateCellValue(), b[index].getDateCellValue()};
//            	Arrays.sort(temp1);
//                return (temp1[1] == a[index].getDateCellValue()?bigger:smaller);
            	return a[index].getDateCellValue().compareTo(b[index].getDateCellValue())*bigger;
            } else {
				if (b[index].getCellTypeEnum() == CellType.STRING) {
            		return bigger;
            	}
            	return (a[index].getNumericCellValue() - b[index].getNumericCellValue()>0?bigger:smaller);
            }
        case FORMULA:
//        	String[] temp3 = new String[] {a[index].getCellFormula(), b[index].getCellFormula()};
//        	Arrays.sort(temp3);
//        	return (temp3[1] == a[index].getStringCellValue()?bigger:smaller);
        	return a[index].getStringCellValue().compareTo(b[index].getStringCellValue())*bigger;
        case BLANK:
        	return bigger;
        default:
        	return bigger;
		}
	}
}
