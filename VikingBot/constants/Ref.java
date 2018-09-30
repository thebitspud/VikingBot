package bot.VikingBot.constants;

import java.awt.Color;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import bot.VikingBot.core.App;

public class Ref {
	
	public static final String prefix = "!";
	
	public static final Color VIKINGS_MAROON = new Color(0x713535); 
	
	public static final String LOGO_URL = "https://raw.githubusercontent.com/VikingsDev/VikingsDev.github.io/master/assets/vikingsdev.jpg";
	public static final String WEBSITE_URL = "https://vikingsdev.github.io";
	
	public static final String spreadsheetId = "1_aCZ2FQh075uedUViV5Zhd-a3Y0prD1xulyZNa2rO3o";
	
	public static String jarPath = ""; 
	public static File VikingBotDB = new File("VikingBot/events.json");
	public static File CREDENTIALS = new File("VikingBot/credentials.json");
	public static File VikingBotDBParent  = new File(".");
	
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static final ArrayList<Long> adminIds = new ArrayList<>(Arrays.asList(194857448673247235L));

	//Used when running project as a jar file.
	static{
		try {
			VikingBotDB = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "VikingBot/events.json");
			VikingBotDBParent = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "VikingBot");
			CREDENTIALS = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "VikingBot/credentials.json");
		}catch(Exception e) {
			System.out.println("Error getting JAR file path.");
		}
	}
	
	public static final String helpMessage = 
				   "```VIKINGS BOT MANUAL"  
			+"\n"
			+ "\n" + prefix + "nextMeeting - Returns the time of the next meeting."
			+ "\n" + prefix + "events - Returns a list of events coming up."
			+ "\n" + prefix + "update - Updates the list of events."
			+ "\n" + prefix + "role @role - Assigns @role to you. [CURRENTLY UNAVAILABLE]"
			+ "\n"
			+ "\n" + "FUN:"
			+ "\n" + prefix + "join - Bot joins the voice channel that you are currently in."
			+ "\n" + prefix + "leave - Bot leaves voice channel."
			+ "\n" + prefix + "play URL - Bot plays the youtube/soundcloud URL provided."
			+ ""
			
			+"```";
}
