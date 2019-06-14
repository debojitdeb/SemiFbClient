package com.dev.fb.client.api;

import com.dev.fb.client.util.CommonUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by deb on 05-05-2019.
 */
public class FacebookCommentsCounter {


    /**
     * Created by Debojit Deb on 05-05-2019.
     * This utility assumes that it would be provided an input text file that would have text in the following format:
     * <User_name> <Comment>
     * <number_of_likes>
     * Manage
     * Like · Reply · 17w
     * <User_name>
     * <User_name> <Comment>
     * <number_of_likes>
     * Manage
     * Image may contain: text
     * 1
     * Like · Reply · 17w
     * ...
     * User the above format, it generates the numbered list of users
     */
    public static void main(String... args) throws IOException {

        String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\comments.txt";
        if (args != null && args.length > 0) {
            fileName = args[0];
        }
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line, lineRead = "";
        int commentCount = 0;
        boolean shouldIgnoreNextLine = false;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.replace("Active Now", "");
            if(matchesTextPatternsToIgnore(line) || shouldIgnoreNextLine) {
                shouldIgnoreNextLine = false;
                continue;
            }

            if(lineRead.startsWith(line) || (lineRead.length()>0 && line.startsWith(lineRead))) {
                shouldIgnoreNextLine = line.matches(".*[\\d]+ mutual friends"); //matches any string followed by any number followed by "mutual friends"
                continue;
            }
            lineRead = line;
            ++commentCount;
            stringBuffer.append(line).append("\n");
        }
        fileReader.close();
        String filePath = CommonUtils.createTempFileWithContent("Comments", stringBuffer.toString());
        CommonUtils.openFileInNotePad(filePath);
        //CommonUtils.copyToClipboard(stringBuffer);
        System.out.println(stringBuffer.toString());
        System.out.println("Total comments: " + commentCount);
    }

    private static boolean matchesTextPatternsToIgnore(String line) {

        return CommonUtils.isNumeric(line) || line.startsWith("Manage") ||
                line.startsWith("Like") || line.startsWith("Love") ||line.startsWith("Haha") ||
                line.startsWith("Wow") || line.startsWith("Sad") ||line.startsWith("Angry") ||
                line.startsWith("LikeShow more reactions") ||
                line.equals("Respond to Friend Request") || line.equals("Write a reply...") ||
                line.contains("Delete or hide this") || line.contains("Edit or delete this") ||
                line.matches("[\\?]+") ||
                //line.matches(" . Reply . ") || line.matches(" . Reply . [\\d]+[mhdwy]") ||
                line.matches(".*Reply.*[\\d]*[smhdwy]") ||
                line.matches(" . Reply . See Translation . [\\d]+[mhdwy]") ||
                line.matches(" . Reply . [\\d]+[mhdwy] . Edited") ||
                line.matches("View [\\d]+ more reply") ||
                line.startsWith("Debojit Deb");
    }

}
