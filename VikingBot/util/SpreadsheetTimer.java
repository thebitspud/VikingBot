package bot.VikingBot.util;

import java.util.Timer;
import java.util.TimerTask;

import bot.VikingBot.core.App;

public class SpreadsheetTimer extends TimerTask{
	
	public SpreadsheetTimer(Long interval) {
		Timer spreadsheetTimer = new Timer();
		spreadsheetTimer.scheduleAtFixedRate(this,0 , interval);
	}
	
	@Override
	public void run() {
		App.updateEvents();
	}

}
