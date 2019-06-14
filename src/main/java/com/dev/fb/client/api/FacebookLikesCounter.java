package com.dev.fb.client.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.dev.fb.client.util.CommonUtils;

/**
 * Created by deb on 05-05-2019.
 */
public class FacebookLikesCounter {


    /**
     * Created by Debojit Deb on 05-05-2019.
     * This utility assumes that it would be provided an input text file that would have text in the following format:
     * <User_name>
     * <number_of_mutual_friends> mutual friends
     * Message
     *
     * <User_name>
     * <number_of_mutual_friends> mutual friends
     * Message
     * ...
     * User the above format, it generates the numbered list of users
     */
    public static void main(String... args) throws IOException {

        String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\likes.txt";
        if (args != null && args.length > 0) {
            fileName = args[0];
        }
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        int likeCount = 0;
        while ((line = bufferedReader.readLine()) != null) {
            if (CommonUtils.isEmpty(line) || line.startsWith("Message") || line.contains("mutual friend") ||
                    line.startsWith("Friend") || line.equals("Add Friend") || line.equals("Respond to Friend Request") ||
                    line.equals("Follow")) {
                continue;
            }

            ++likeCount;
            stringBuffer./*append(likeCount).append(". ").*/append(line).append("\n");
        }
        fileReader.close();
        String filePath = CommonUtils.createTempFileWithContent("Likes", stringBuffer.toString());
        CommonUtils.openFileInNotePad(filePath);
        //CommonUtils.copyToClipboard(stringBuffer);
        System.out.println(stringBuffer.toString());
        System.out.println("Total likes: " + likeCount);
    }

}
