package com.dev.fb.client.api;

import com.dev.fb.client.util.CommonUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Debojit Deb on 15-05-2019
 * <p>
 * Reads names of likes and comments columns
 * and prepares HTML report with the names that are
 * hyper-linked to the associated FB profile in a new tab.
 * Categorized by "Likes & Comments", "Only Likes" and "Only Comments"
 */

public class FacebookCommentsAndLikesHtmlReportPerPost {

    private static String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\FB Likes and Comments.xlsx";

    public static void main(String... args) throws IOException {

        String sheetNameOrIndex = "";

        if (args != null && args.length > 0 && args[0].trim().length() > 0) {
            fileName = args[0];
        }
        if (args != null && args.length > 1) {
            sheetNameOrIndex = args[1];
        }
        
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;

        String sheetName = "";

        try {

        	fis = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fis);

            sheetName = getSheetName(sheetNameOrIndex, workbook);

            XSSFSheet sheet = workbook.getSheet(sheetName);

            if(sheet == null) {
                System.err.println(String.format("Sheet name '%s' is not found. Please check the excel!", sheetNameOrIndex));
                System.exit(1);
            }
            Iterator<Row> rowIterator = sheet.iterator();

            List<String> likes = new ArrayList<String>();
            List<String> comments = new ArrayList<String>();
            CommonUtils.readCommentAndLikeFromRowIterator(rowIterator, likes, comments);

            prepareAndDisplayHtml(sheetName, comments, likes);

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
    }

	private static String getSheetName(String sheetNameOrIndex, XSSFWorkbook workbook) {

		int sheetIndex;
		if (CommonUtils.isNotEmpty(sheetNameOrIndex)) {
			if (CommonUtils.isNumeric(sheetNameOrIndex)) {
				sheetIndex = Integer.parseInt(sheetNameOrIndex);
				return workbook.getSheetName(sheetIndex);
			} else {
				return sheetNameOrIndex;
			}
		}
		return workbook.getSheetName(0);
	}

    private static void prepareAndDisplayHtml(String sheetName, List<String> comments, List<String> likes) throws IOException {

        Map<String, String> friendsLinkMap = FriendFbLinkLoader.loadFriendsLinkMap();

        StringBuilder stringBuilder = new StringBuilder();
        //Users Who Commented

        List<String> usersWhoBothLikedAndCommented = CommonUtils.getCommonElementsInBothLists(comments, likes);
        stringBuilder.append("<TABLE>");
        stringBuilder.append("<TR>" +
                "<TH>Users Who Commented & Liked</TH>" +
                "<TH>Users Who Only Commented</TH>" +
                "<TH>Users Who Only Liked</TH>" +
                "</TR>");
        stringBuilder.append("<TR>" +
                "<TD valign='top'>");
        stringBuilder.append(buildHtmlContentFromList(usersWhoBothLikedAndCommented, friendsLinkMap));
        stringBuilder.append("</TD>");

        List<String> usersWhoOnlyCommented = CommonUtils.getElementsAvailableOnlyInFirstList(comments, likes);
        stringBuilder.append("<TD valign='top'>");
        stringBuilder.append(buildHtmlContentFromList(usersWhoOnlyCommented, friendsLinkMap));
        stringBuilder.append("</TD>");

        stringBuilder.append("<TD valign='top'>");
        List<String> usersWhoOnlyLiked = CommonUtils.getElementsAvailableOnlyInSecondList(comments, likes);
        stringBuilder.append(buildHtmlContentFromList(usersWhoOnlyLiked, friendsLinkMap));
        stringBuilder.append("</TD></TR></TABLE>");

        String htmlContent = stringBuilder.toString();
        String htmlHeader = String.format("<HTML><HEAD><META CHARSET='UTF-8'><TITLE>FB Post (%s) Report</TITLE></HEAD>" +
                "<BODY><FONT FACE='arial' size='2'><H2 align='center'>%s</H2>", sheetName, sheetName);
        String htmlFooter = String.format("<H5>File Source:<a href='%s'>%s</a><br/>" +
                "<H3 ALIGN='center'>End of Report</H3></FONT></BODY></HTML>", fileName, fileName);
        try {
            String htmlFile = CommonUtils.writeStringToTempFile(htmlHeader + htmlContent + htmlFooter);
            System.out.println("Report written to " + htmlFile);
        } catch (IOException e) {
            System.err.println("Exception while preparing report: " + e.getMessage());
        }

    }

    private static String buildHtmlContentFromList(List<String> comments, Map<String, String> friendsLinkMap) {

        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String userName : comments) {
            String link = friendsLinkMap.get(userName);

            String lineContent = (count < 9 ? "<div>&nbsp;" : "<div>") + ++count + ". " +
                    getLink(userName, link) + "</div>";
            stringBuilder.append(lineContent);
        }
        return stringBuilder.toString();
    }

    private static String getLink(String key, String link) {

        return CommonUtils.isNotEmpty(link) ? "<a target='_blank' href='" + link + "'>" + key + "</a>" : key;
    }


}
