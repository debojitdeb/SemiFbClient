package com.dev.fb.client.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.dev.fb.client.util.MyUtilAppsConstants.COMMENT_INDEX;
import static com.dev.fb.client.util.MyUtilAppsConstants.LIKE_INDEX;

/**
 * Created by Debojit Deb on 12-02-2018.
 */
public class CommonUtils {

    //private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";

    public static String convertDateToLocalTime(String dateString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        try {

            Date date = formatter.parse(dateString.replaceAll("Z$", "+0000"));
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            //String timeZone = TimeZone.getDefault().getDisplayName();
            return formatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }

    }

    public static String writeStringToTempFile(String content, String ...optionalFileName) throws IOException {

        String pathname = isEmpty(optionalFileName) ?
                "C:\\Users\\deb\\AppData\\Local\\Temp\\FbPostReport.htm" : optionalFileName[0];

        Path path = FileSystems.getDefault().getPath(pathname);

        BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
        bw.write(content);
        bw.close();
        return pathname;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(Collection<?> collection) {

    	return collection == null || collection.size() == 0;
    }

    public static boolean isEmpty(String ...strings) {

    	return strings == null || strings.length == 0;
    }

    public static boolean isNumeric(CharSequence cs) {

    	if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static List<String> getCommonElementsInBothLists(List<String> list1, List<String> list2) {

        List<String> commonElements = new ArrayList<>(list1);
        commonElements.retainAll(list2);
        return commonElements;
    }


    public static List<String> getElementsAvailableOnlyInFirstList(List<String> list1, List<String> list2) {

        List<String> onlyInList1 = new ArrayList<>(list1);
        onlyInList1.removeAll(list2);
        return onlyInList1;
    }


    public static List<String> getElementsAvailableOnlyInSecondList(List<String> list1, List<String> list2) {

        List<String> onlyInList2 = new ArrayList<>(list2);
        onlyInList2.removeAll(list1);
        return onlyInList2;
    }

    public static void readCommentAndLikeFromRowIterator(Iterator<Row> rowIterator, List<String> likes, List<String> comments) {

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (row.getRowNum() > 0) { //To filter column headings
                    int columnIndex = cell.getColumnIndex();
                    if (columnIndex == COMMENT_INDEX) {
                        comments.add(cell.getStringCellValue().replace("\uFEFF", ""));
                    } else if (columnIndex == LIKE_INDEX) {
                        likes.add(cell.getStringCellValue().replace("\uFEFF", ""));
                    }
                }
            }
        }
    }

	public static void copyToClipboard(StringBuffer stringBuffer) {

		StringSelection stringSelection = new StringSelection(stringBuffer.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
	}

	public static String createTempFileWithContent(String fileName, String content) {

		File tempFile;
		try {
			//create temp file... 
			tempFile = File.createTempFile(fileName, ".tmp");
            //Write on it...
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
    	    bw.write(content);
    	    bw.close();
    	    return tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Temp file could not be created: ", e);
        }
    }

	public static void openFileInNotePad(String filePath) {

		Runtime rs = Runtime.getRuntime();
	    try {
	      rs.exec("notepad " + filePath);
	    }
	    catch (IOException e) {
	      System.err.println(e);
	    }
	  }

	
}
