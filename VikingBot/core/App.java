package bot.VikingBot.core;

import java.awt.Color;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;

import javax.security.auth.login.LoginException;

import com.google.gdata.util.ServiceException;

import bot.VikingBot.constants.Key;
import bot.VikingBot.constants.Ref;
import bot.VikingBot.db.Event;
import bot.VikingBot.util.Music;
import bot.VikingBot.util.Spreadsheet;
import bot.VikingBot.util.SpreadsheetTimer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

public class App extends ListenerAdapter
{
	
	static JDA jda;
	static ArrayList<Event> events;
    public static void main( String[] args ) throws LoginException, IOException, ServiceException
    {
        jda = new JDABuilder(AccountType.BOT).setToken(Key.TOKEN).build();
        jda.addEventListener(new App());
        jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, Ref.prefix + "help"));
        Spreadsheet.updateLocalSheet();
        events = Spreadsheet.scanSheet();
        SpreadsheetTimer spreadsheetTimer = new SpreadsheetTimer(150000L);
        
        Music.setJDA(jda);
        Music.configureMusic();
    }
    
    public static void updateEvents() {
    	Spreadsheet.updateLocalSheet();
    	events = Spreadsheet.scanSheet();
    	System.out.println("Update");
    }


    
    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
    	//Objects
    	Message objMsg = evt.getMessage();
    	MessageChannel objMsgCh = evt.getTextChannel();
    	User objUser = evt.getAuthor();
    	Member objMember = evt.getMember();
    	Guild objGuild = evt.getGuild();
    	
    	String raw = objMsg.getContentRaw();
    	
    	if(!raw.startsWith(Ref.prefix) || (!raw.contains("r/"))) {
    		return;
    	}
    	
    	String command = "";
    	String input = "";
    	try{
    		command = raw.substring(Ref.prefix.length(), raw.indexOf(" ")).trim();
    		input = raw.substring(command.length() + Ref.prefix.length() +1).trim();
    	}catch (Exception e) {
    		try {
    			command = raw.substring(Ref.prefix.length()).trim();
    		}catch(Exception exc){
    			return;
    		}	  		
    	}
    	
    	if(command.equalsIgnoreCase("help")) {
    		objMsgCh.sendMessage(Ref.helpMessage).queue();
    	}else if(command.equalsIgnoreCase("nextMeeting")) {
    		LocalDate nextWed = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
    		String date = nextWed.getDayOfWeek().name() + ", " + nextWed.getMonth().name() + " " + nextWed.getDayOfMonth() + ", " + nextWed.getYear();
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("Next meeting",Ref.WEBSITE_URL);
    		eb.setColor(Ref.VIKINGS_MAROON);
    		eb.addField(date,"3:10pm - 4:30pm",false);
    		eb.addField("Location","BNSS - Room 315",false);
    		eb.setThumbnail(Ref.LOGO_URL);
    		objMsgCh.sendMessage(eb.build()).queue();
    	}else if(command.equalsIgnoreCase("invite")) {
    		objMsgCh.sendMessage("https://discord.gg/u5Fwsgy").queue();
    	}else if(command.equalsIgnoreCase("events") || command.equalsIgnoreCase("event") || command.equalsIgnoreCase("evt")) {
    		EmbedBuilder title = new EmbedBuilder();
    		title.setTitle("Upcoming Events");
    		title.setDescription(events.size() + " events coming up.");
    		title.setColor(Ref.VIKINGS_MAROON);
    		
    		objMsgCh.sendMessage(title.build()).queue();
    		
    		for(Event e: events) {
    			EmbedBuilder eb = new EmbedBuilder();
    			eb.setTitle(e.getName());
    			eb.setThumbnail(e.getImageURL());
    			eb.setDescription(e.getDescription());
    			eb.addField("Event Starts at",Ref.dateFormat.format(e.getInitialDate()),true);
    			eb.addField("Event Ends at",Ref.dateFormat.format(e.getEndDate()),true);
    			eb.setColor(e.getColour());
    			
    			objMsgCh.sendMessage(eb.build()).queue();
    		}
    	}else if(command.equalsIgnoreCase("join")) {
    		if(objMember.getVoiceState().inVoiceChannel()) {
    			Music.joinChannel(objMember.getVoiceState().getChannel());
    			
    			//Music.joinChannel(493513615505358857L);
    		}else {
    			objMsgCh.sendMessage(objUser.getAsMention() + " You must be in a voice channel to use this command!").queue();
    		}
    		
    	}else if(command.equalsIgnoreCase("leave")) {
    		Music.exitChannel();
    	}else if(command.equalsIgnoreCase("play")) {
    		Music.play(input);
    	}
    	
    }
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    	RestAction<PrivateChannel> restAction = event.getUser().openPrivateChannel();
    	restAction.queue(channel -> {
    		channel.sendMessage("Hello and welcome to VikingsDev! We're excited to have you here. To start, here are the rules and some suggestions:").queue();
    		channel.sendMessage("This info can also be found in #welcome" + ""
    				+ "\nRules:\n" + 
    				"1. Be respectful\n" + 
    				"2. No channel pollution, keep your conversations in the right channels\n" + 
    				"3. No NSFW memes in any channel, please refrain from posting memes in channels other than #memes \n" + 
    				"4. Nickname must contain first name\n" + 
    				"\n" + 
    				"Suggestions:\n" + 
    				"1. It might be a good idea to mute #bot-testing , #bot-dev , and #programmerhumor since there will be a lot of spam in there.\n" + 
    				"2. #space probably won't have too many notifications, but there will probably be two per day. Muting it might be a good idea, however it's up to you!\n" + 
    				"\n" + 
    				"Chill:\n" + 
    				"1. Post all of your family-friendly memes to #memes ! We aren't too strict on what gets posted there, just nothing graphical.\n" + 
    				"2. #programmerhumor will feature hot posts from r/programmerhumor! Check their for some spicy programming memes :fire:\n" + 
    				"3. #space is a channel for all the people who like space! This channel will feature notifications when the ISS passes over,"
    				+ " both astronomy images of the day and images of the day by NASA, as well as breaking news from NASA.").queue();
    	});
    	
    }
}
