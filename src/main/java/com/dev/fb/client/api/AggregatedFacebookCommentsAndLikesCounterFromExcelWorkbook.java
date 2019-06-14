package com.dev.fb.client.api;

import com.dev.fb.client.model.CountAndDatesData;
import com.dev.fb.client.model.PostData;
import com.dev.fb.client.model.UserAggregatedSummary;
import com.dev.fb.client.util.CommonUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by deb on 15-05-2019.
 */
public class AggregatedFacebookCommentsAndLikesCounterFromExcelWorkbook {

    public static void main(String... args) throws IOException {

        String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\FB Likes and Comments.xlsx";
        String tabRangeInfo = "ALL";

        if (args != null) {
            if (args.length > 0 && args[0].trim().length() > 0) {
                fileName = args[0];
            }
            if (args.length > 1) {
                tabRangeInfo = validateTabRangeInfo(args[1]);
            }
        }

        FileInputStream fis = null;
        XSSFWorkbook workbook = null;

        try {
            fis = new FileInputStream(fileName);
            workbook = new XSSFWorkbook(fis);

            int startTabIndex = tabRangeInfo.equals("ALL") ? 0 : findStartTabIndex(tabRangeInfo);
            int lastTabInTheWorkbook = workbook.getNumberOfSheets() - 1;
            int endTabIndex = tabRangeInfo.equals("ALL") ? lastTabInTheWorkbook : findEndTabIndex(tabRangeInfo);
            endTabIndex = endTabIndex > lastTabInTheWorkbook ? lastTabInTheWorkbook : endTabIndex;

            Map<String, PostData> postDataMap = new LinkedHashMap<>();
            for (int sheetCount = startTabIndex; sheetCount <= endTabIndex; sheetCount++) {

                XSSFSheet sheet = workbook.getSheetAt(sheetCount);
                Iterator<Row> rowIterator = sheet.iterator();

                List<String> likes = new ArrayList<>();
                List<String> comments = new ArrayList<>();
                CommonUtils.readCommentAndLikeFromRowIterator(rowIterator, likes, comments);
                String sheetName = workbook.getSheetName(sheetCount);
                PostData postData = new PostData(sheetName, sheetName, comments, likes);
                postDataMap.put(sheetName, postData);
            }
            Map<String, CountAndDatesData> commentMap = new HashMap<>();
            Map<String, CountAndDatesData> likeMap = new HashMap<>();
            for (Map.Entry<String, PostData> postDataEntry : postDataMap.entrySet()) {
                aggregateCommentsCountPerUser(commentMap, postDataEntry);
                aggregateLikesCountPerUser(likeMap, postDataEntry);
            }
            String pageTitle = startTabIndex + " to " + endTabIndex;
            prepareAndDisplayHtml(pageTitle, commentMap, likeMap);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(workbook != null) {
            	workbook.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

    }

    private static int findStartTabIndex(String tabRangeInfo) {

        String[] stringArray = tabRangeInfo.replace(" ", "").split("-");
        if (!CommonUtils.isEmpty(stringArray)) {
            return Integer.parseInt(stringArray[0]);
        }
        return -1;
    }

    private static int findEndTabIndex(String tabRangeInfo) {

        String[] stringArray = tabRangeInfo.replace(" ", "").split("-");
        if (!CommonUtils.isEmpty(stringArray)) {
            return Integer.parseInt(stringArray[1]);
        }
        return -1;
    }

    private static String validateTabRangeInfo(String arg) {

        arg = arg.replace(" ", "");
        //valid numeric tab range: 1 - 5 or 2-4 etc
        boolean isValidNumericTabRange = arg.matches("[\\d]+-[\\d]+");
        if (isValidNumericTabRange) return arg;
        //valid numeric tabs: 1, 5, 3 or 2,4,6,8 etc
        boolean isValidNumericTabs = arg.matches("[\\d]+,[\\d]+");
        if (isValidNumericTabs) return arg;
        boolean isValidNameTabRange = arg.matches(".*+-.*+");
        if (isValidNameTabRange) return arg;
        boolean isValidNameTabs = arg.matches(".*+-,*+");
        if (isValidNameTabs) return arg;

        return "ALL";
    }

    private static void prepareAndDisplayHtml(String pageTitle, Map<String, CountAndDatesData> commentMap, Map<String, CountAndDatesData> likeMap) throws IOException {

        Set<Map.Entry<String, CountAndDatesData>> usersWhoCommented = commentMap.entrySet();
        Set<Map.Entry<String, CountAndDatesData>> usersWhoLiked = likeMap.entrySet();
        Map<String, UserAggregatedSummary> userAggregatedSummaryMap = new HashMap<>();

        Map<String, String> friendsLinkMap = FriendFbLinkLoader.loadFriendsLinkMap();

        StringBuilder stringBuilder = new StringBuilder();
        pageTitle = String.format("Summary Report | Tab %s", pageTitle);
        stringBuilder.append(String.format("<HTML><HEAD><META CHARSET='UTF-8'><TITLE>%s</TITLE></HEAD><BODY>", pageTitle));
        stringBuilder.append("<H3 align='center'><U>").append(pageTitle).append("</U></H3>");

        //Users Who Commented
        for (Map.Entry<String, CountAndDatesData> user : usersWhoCommented) {
            String key = user.getKey().trim();
            String link = friendsLinkMap.get(key);
            userAggregatedSummaryMap.put(key, new UserAggregatedSummary(key, link, user.getValue().getCount(), 0, user.getValue().getDates(), null));
        }
        //Users Who Liked
        for (Map.Entry<String, CountAndDatesData> user : usersWhoLiked) {
            String key = user.getKey().trim();
            int numberOfLikes = user.getValue().getCount();
            UserAggregatedSummary userAggregatedSummary = userAggregatedSummaryMap.get(key);
            if(userAggregatedSummary != null) {
                userAggregatedSummary.setNumberOfLikes(numberOfLikes);
                userAggregatedSummary.setLikeDates(user.getValue().getDates());
            } else {
                userAggregatedSummary = new UserAggregatedSummary(key, friendsLinkMap.get(key), 0, numberOfLikes, null, user.getValue().getDates());
            }
            userAggregatedSummaryMap.put(key, userAggregatedSummary);
        }

        //Sort the map by values: Count of Comments and Likes to be at the top
        List<UserAggregatedSummary> usersList = sortMapByValue(userAggregatedSummaryMap);
        //Build the html content...
        int count = 0;
        for(UserAggregatedSummary userAggregatedSummary : usersList) {
            String hyperLink = getLink(userAggregatedSummary);
            String commentString = " [ Comments: " + getCountString(userAggregatedSummary.getNumberOfComments()) + ". ";
            String likeString = "Likes: " + getCountString(userAggregatedSummary.getNumberOfLikes());

            String lineContent = (count < 9 ? "<div>&nbsp;" : "<div>") + ++count + ". " +
                    hyperLink + commentString + likeString + " ] </div>";
            stringBuilder.append(lineContent);
        }
        String sb = stringBuilder.append("</BODY></HTML>").toString();
        String htmlFile = CommonUtils.writeStringToTempFile(sb, "C:\\Users\\deb\\AppData\\Local\\Temp\\FbAggregatePostReport.htm");

        System.out.println("Report written to " + htmlFile);
    }

    private static List<UserAggregatedSummary> sortMapByValue(Map<String, UserAggregatedSummary> userAggregatedSummaryMap) {

        TreeSet<UserAggregatedSummary> sortedUserAggregatedSummaryMap = new TreeSet<UserAggregatedSummary>();
        for(Map.Entry<String, UserAggregatedSummary> userAggregatedSummary : userAggregatedSummaryMap.entrySet()) {
            sortedUserAggregatedSummaryMap.add(userAggregatedSummary.getValue());
        }
        return new ArrayList<UserAggregatedSummary>(sortedUserAggregatedSummaryMap);
    }

    private static String getCountString(Integer value) {

        value = zeroIfNull(value);
        return value + (value < 2 ? " time" : " times");
    }

    private static Integer zeroIfNull(Integer integer) {

        return integer == null ? 0 : integer;
    }

    private static String getLink(UserAggregatedSummary userAggregatedSummary) {

        String link = userAggregatedSummary.getLink();
        String key = userAggregatedSummary.getName();
        List<String> commentDates = userAggregatedSummary.getCommentDates();
        List<String> likeDates = userAggregatedSummary.getLikeDates();
        String commentTooltipDateText = getAltText(commentDates);
        String likeTooltipDateText = getAltText(likeDates);
        return CommonUtils.isNotEmpty(link) ? String.format("<a target='_blank' href='%s' title='%s'>%s</a>", link, getToolTipForHyperlink(commentTooltipDateText, likeTooltipDateText), key)
        		:  	String.format("<a title='%s'>%s</a>", getToolTipForHyperlink(commentTooltipDateText, likeTooltipDateText), key);
    }

    private static String getToolTipForHyperlink(String commentTooltipDateText, String likeTooltipDateText) {

        String commentDatesAsToolTip = getCommentDatesAsToolTip(commentTooltipDateText);
        String likeDatesAsToolTip = getLikeDatesAsToolTip(likeTooltipDateText);
        String lineBreak = lineBreak(commentDatesAsToolTip, likeDatesAsToolTip);
        return commentDatesAsToolTip + lineBreak + likeDatesAsToolTip;
    }

    private static String getCommentDatesAsToolTip(String commentTooltipDateText) {

        return (commentTooltipDateText.trim().length() > 0) ?
        "Commented On: " + commentTooltipDateText : "";
    }

    private static String getLikeDatesAsToolTip(String likeTooltipDateText) {

        return (likeTooltipDateText.trim().length() > 0) ?
                "Liked On: " + likeTooltipDateText : "";
    }

    private static String lineBreak(String commentDatesAsToolTip, String likeDatesAsToolTip) {

        return (commentDatesAsToolTip.trim().length() > 0 && likeDatesAsToolTip.trim().length() > 0) ?
                "\n" : "";
    }

    private static String getAltText(List<String> dates) {

        String altText = "";
        if(dates == null) {
            return altText;
        }

        for(String date : dates) {
            altText += date + ", ";
        }
        if(altText.endsWith(", ")) {
            altText = altText.substring(0, altText.length() - 2);
        }
        return altText;
    }

    private static void aggregateCommentsCountPerUser(Map<String, CountAndDatesData> commentMap, Map.Entry<String, PostData> postDataEntry) {

        PostData postData = postDataEntry.getValue();
        for (String name : postData.getComments()) {
            aggregate(commentMap, name, postData.getPostDate());
        }
    }

    private static void aggregateLikesCountPerUser(Map<String, CountAndDatesData> likeMap, Map.Entry<String, PostData> postDataEntry) {

        PostData postData = postDataEntry.getValue();
        for (String name : postData.getLikes()) {
            String postDate = postData.getPostDate();
            aggregate(likeMap, name, postDate);
        }
    }
    private static void aggregate(Map<String, CountAndDatesData> map, String name, String postDate) {

        if (map.containsKey(name)) {

            CountAndDatesData countAndDatesData = map.get(name);
            countAndDatesData.setCount(countAndDatesData.getCount() + 1);
            countAndDatesData.getDates().add(postDate);
            map.put(name, countAndDatesData);
        } else {
            List<String> postDates = new ArrayList<>();
            postDates.add(postDate);
            map.put(name, new CountAndDatesData(1, postDates));
        }
    }

}
