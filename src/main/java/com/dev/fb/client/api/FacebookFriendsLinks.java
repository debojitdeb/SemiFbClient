package com.dev.fb.client.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Debojit Deb on 12-05-2019.
 *
 * Finds the links of the Fried's profile from the html content taken from Friend's page
 *
 */

public class FacebookFriendsLinks {


    public static void main(String... args) throws IOException {

        String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\friends_html.txt";
        if (args != null && args.length > 0) {
            fileName = args[0];
        }
        File file = new File(fileName);
        if(!file.exists()) {
            System.err.println("File doesn't exist! Please check if you have copied the HTML source content " +
                    "from Facebook Friends page onto " + fileName);
            return;
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        int linkCount = 0;

        while ((line = bufferedReader.readLine()) != null) {
            String searchPattern = "<a class=\"_5q6s _8o _8t lfloat _ohe\" href=\"";
            int startIndex = line.indexOf(searchPattern);
            if(startIndex > -1) {
                String link = line.substring(startIndex + searchPattern.length());

                int endIndex = link.indexOf("&amp;fref=pb");
                if(endIndex == -1) {
                    endIndex = link.indexOf("?fref=pb");
                }
                link = link.substring(startIndex, endIndex);
                ++linkCount;

                stringBuffer./*append(linkCount).append(". ").*/append(link).append("\n");
            }
        }
        fileReader.close();

        System.out.println(stringBuffer.toString());
        System.out.println("Total links: " + linkCount);
    }

}
