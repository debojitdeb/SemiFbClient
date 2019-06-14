package com.dev.fb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.dev.fb.client.api.AggregatedFacebookCommentsAndLikesCounterFromExcelWorkbook;
import com.dev.fb.client.api.FacebookCommentsAndLikesHtmlReportPerPost;
import com.dev.fb.client.api.FacebookCommentsCounter;
import com.dev.fb.client.api.FacebookFriendsCounter;
import com.dev.fb.client.api.FacebookLikesCounter;

/**
 * Main project gateway App
 *
 */
public class App
{
	public static void main(String[] args) throws IOException {

		System.out.println( "Welcome to Fb Client App!" );

		Scanner scan = new Scanner(System.in);
		while (true) {
			showOptions();
			String programIndex = readOption();
			int index = Integer.parseInt(programIndex);
			doAction(args, index);


			while(true) {
				System.out.print("Do you want to exit?: ");
				String input = scan.nextLine();

				if (input.toUpperCase().matches("Y|YES|1")) {
					scan.close();
					System.exit(1);
				} else if (input.toUpperCase().matches("N|NO|0")) {
					break;
				} else {
					System.err.println("\nInvalid option! Please select Yes (Y) to exit or No (N) to continue.\n");
				}
			}
		}
	}

	private static void doAction(String[] args, int index) throws IOException {

		switch(index) {
			case 1:
				handleReportPerPost();
				break;
			case 2:
				handleReportForMultiplePosts();
				break;
			case 3:
				FacebookCommentsCounter.main(args);
				break;
			case 4:
				FacebookLikesCounter.main(args);
				break;
			case 5:
				FacebookFriendsCounter.main(args);
				break;
			case 6:
				System.exit(1);
				break;
			default:
				throw new RuntimeException("No valid options provided. Value options are 1-6");

		}
	}

	private static String readOption() throws IOException {

		String programIndex;
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			programIndex = reader.readLine();
			if (!programIndex.matches("[1-6xX]")) {
				System.err.println("Invalid option selected! Valid values are between 1 and 6. Please try again!");
				showOptions();
				continue;
			}
			break;
		}

		if(programIndex.equalsIgnoreCase("X")) {
			System.exit(1);
		}
		return programIndex;
	}

	private static void showOptions() {

		System.out.println("Select from the following options:\n----------------------------------");
		System.out.println("1. FacebookCommentsAndLikesHtmlReportPerPost");
		System.out.println("2. AggregatedFacebookCommentsAndLikesCounterFromExcelWorkbook");
		System.out.println("3. FacebookCommentsCounter");
		System.out.println("4. FacebookLikesCounter");
		System.out.println("5. FacebookFriendsCounter");
		System.out.println("6. Exit");
		System.out.print("Option: ");
	}

	private static void handleReportPerPost() throws IOException {

		String[] fileNameAndTabIndex = readFileNameAndTabIndex("Enter the Excel tabs index or name: ");
		String fileName = fileNameAndTabIndex[0];
		String tabIndexOrName = fileNameAndTabIndex[1];
		FacebookCommentsAndLikesHtmlReportPerPost.main(fileName, tabIndexOrName);
	}

	private static void handleReportForMultiplePosts() throws IOException {

		String[] fileNameAndTabIndex = readFileNameAndTabIndex("Enter the range of Excel tabs (e.g., 0-3 | 01-Jan-2019 - 05-May-2019 | All : ");
		String fileName = fileNameAndTabIndex[0];
		String tabRange = fileNameAndTabIndex[1];

		AggregatedFacebookCommentsAndLikesCounterFromExcelWorkbook.main(fileName, tabRange);
	}

	static String[] readFileNameAndTabIndex(String tabIndexOrRangeInputMessage) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Excel file name including path: ");
		String fileName = reader.readLine();
		System.out.print(tabIndexOrRangeInputMessage);
		String tabIndexOrName = reader.readLine();
		return new String[] {fileName, tabIndexOrName};
	}
}
