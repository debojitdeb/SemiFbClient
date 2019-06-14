package com.dev.fb.client.api;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.dev.fb.client.util.MyUtilAppsConstants.LINK_INDEX;
import static com.dev.fb.client.util.MyUtilAppsConstants.NAME_INDEX;

/**
 * Created by deb on 18-05-2019.
 */
public class FriendFbLinkLoader {

    private static final String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\Friends.xlsx";

    public static Map<String, String> loadFriendsLinkMap() throws IOException {


        FileInputStream fis = null;
        Map<String, String> userLinkMap = new LinkedHashMap<>();
        XSSFWorkbook workbook = null;

        try {
            fis = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                String name = "", link = "";
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (row.getRowNum() > 0) { //To filter column headings
                        int columnIndex = cell.getColumnIndex();
                        if (columnIndex == NAME_INDEX) {
                            name = cell.getStringCellValue().replace("\uFEFF", "");
                        } else if (columnIndex == LINK_INDEX) {
                            link = cell.getStringCellValue().replace("\uFEFF", "");
                        }
                        userLinkMap.put(name, link);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if(workbook != null) {
            	workbook.close();
            }
        }
        return userLinkMap;
    }
}
