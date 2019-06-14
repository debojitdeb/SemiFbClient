package com.dev.fb.client.api;

import com.dev.fb.client.util.CommonUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Debojit Deb on 12-05-2019.
 *
 * Counts the number of friends from a given text file which is supposed to be populated by copying
 * The content from friends' page. The format should look like as below:
 *
     Friend
     Friends
     <User Name>
     [<number> friends] | [<number> mutual friends]

     Friend
     Friends
    <User Name>
    [<number> friends] | [<number> mutual friends]
    ...
 *
 */

public class FacebookFriendsCounter {

    public static void main(String... args) throws IOException {

        String fileName = "C:\\DEBOJIT\\BACKUP\\misc\\FacebookData\\friends.txt";
        if (args != null && args.length > 0) {
            fileName = args[0];
        }
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        int friendsCount = 0;
        boolean friendCounted = false;

        while ((line = bufferedReader.readLine()) != null) {
            if (CommonUtils.isEmpty(line) || line.startsWith("Friend") || line.endsWith("friends") ||
                    line.contains(" at ") || line.contains("University") || line.contains("Institute") ||
            line.contains("College") ) {
                friendCounted = false;
                continue;
            }

            if(friendCounted) {
                friendCounted = false;
                continue;
            }
            ++friendsCount;
            friendCounted = true;
            stringBuffer.append(line).append("\n");
        }
        fileReader.close();

        System.out.println(stringBuffer.toString());
        System.out.println("Total friends: " + friendsCount);
    }

}
