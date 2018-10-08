package bot.VikingBot.core;

import java.awt.Color;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

public class App extends ListenerAdapter {
	
	static JDA jda;
	static ArrayList<Event> events;
	
    public static void main( String[] args ) throws LoginException, IOException, ServiceException {
        jda = new JDABuilder(AccountType.BOT).setToken(Key.TOKEN).build();
        jda.addEventListener(new App());
        jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, Ref.prefix + "help"));
        Spreadsheet.updateLocalSheet();
        events = Spreadsheet.scanSheet();
        SpreadsheetTimer spreadsheetTimer = new SpreadsheetTimer(300000L);
        System.err.println("[App.java] Spreadsheet loaded.");
        Music.setJDA(jda);
        Music.configureMusic();
    }
    
    public static void updateEvents() {
    	Spreadsheet.updateLocalSheet();
    	events = Spreadsheet.scanSheet();
    	//System.out.println("Update");
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
    	if(!raw.startsWith(Ref.prefix) && (!raw.contains("r/"))) {
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
    	
    	switch(command.equalsIgnoreCase) {
    	case "help":
    		
    		objMsgCh.sendMessage(Ref.helpMessage).queue();
    		
    		break;
    		
    	case "nextMeeting":
    	case "meeting":
    		
    		LocalDate nextWed = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
    		String date = nextWed.getDayOfWeek().name() + ", " + nextWed.getMonth().name() + " " + nextWed.getDayOfMonth() + ", " + nextWed.getYear();
    		
    		EmbedBuilder eb = new EmbedBuilder();
    		eb.setTitle("Next meeting",Ref.WEBSITE_URL);
    		eb.setColor(Ref.VIKINGS_MAROON);
    		eb.addField(date,"3:10pm - 4:30pm",false);
    		eb.addField("Location","BNSS - Room 315",false);
    		eb.setThumbnail(Ref.LOGO_URL);
    		
    		objMsgCh.sendMessage(eb.build()).queue();
    		
    		break;
    	
    	case "invite":
    		
    		objMsgCh.sendMessage("https://discord.gg/u5Fwsgy").queue();
    		
    		break;
    		
    	case "events":
    	case "event":
    	case "evt":
    		
    		EmbedBuilder title = new EmbedBuilder();
    		title.setTitle("Upcoming Events, Homework, and Tests");
    		
    		title.setColor(Ref.VIKINGS_MAROON);
    		String description = "`" + events.size() + " events in total.`\n\n";
    		Map<String, Integer> eventSummary = getEventSummary();
    		
    		for(String type : getEventSummary().keySet()) {
    			if(eventSummary.get(type) > 1)
    				description += "`" + eventSummary.get(type) + " " + type + "s coming up.`\n";
    			else description += "`" + eventSummary.get(type) + " " + type + " coming up.`\n";
    		}
    		
    		title.setDescription(description);
    		objMsgCh.sendMessage(title.build()).queue();
    		
    		for(Event e: events) objMsgCh.sendMessage(e.getEmbed()).queue();
    		
    		break;
    		
    	case "update":
    		
    		Spreadsheet.updateLocalSheet();
    		events = Spreadsheet.scanSheet();
    		objMsgCh.sendMessage("Events updated.").queue();
    		
    		break;
    		
    	case "join":
    		
    		if(objMember.getVoiceState().inVoiceChannel()) //Music.joinChannel(493513615505358857L);
    			Music.joinChannel(objMember.getVoiceState().getChannel());
    		else objMsgCh.sendMessage(objUser.getAsMention() + " You must be in a voice channel to use this command!").queue();
    		
    		break;
    		
    	case "leave":
    		
    		Music.exitChannel();
    		
    		break;
    		
    	case "play":
    		
    		Music.play(input);
    		
    		break;
    		
    	case "terminate":
    		
    		jda.shutdown();
    		
    		break;
    		
    	case "source":
    		
    		objMsgCh.sendMessage("https://github.com/VikingsDev/VikingBot").queue();
    		
    		break;
    		
    	default:
    		
    		objMsgCh.sendMessage("Unknown command. Try " + Ref.prefix + "help").queue();
    		
    		break:
    	}
    }
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    	RestAction<PrivateChannel> restAction = event.getUser().openPrivateChannel();
    	restAction.queue(channel -> {
    		channel.sendMessage("Hello and welcome to VikingsDev! We're excited to have you here. To start, here are the rules and some suggestions:").queue();
    		MessageChannel welcomeChannel = jda.getTextChannelById(453259026130534411L);
    		welcomeChannel.getHistory().retrievePast(1).queue(message ->{
    			channel.sendMessage(message.get(0)).queue();
    		});
    	});
    }
    
    public Map<String, Integer> getEventSummary() {
    	ArrayList<String> eventTypes = new ArrayList<>();
    	for(Event e : events) {
    		if(!eventTypes.contains(e.getType())) {
    			eventTypes.add(e.getType());
    		}
    	}
    	
    	Map<String, Integer> typeSummary = new HashMap<String,Integer>();
    	
    	for(String et : eventTypes) {
    		int count = 0;
    		for(Event e : events) {
    			if(e.getType().equals(et)) {
    				count++;
    			}
    		}
    		typeSummary.put(et, count);
    	}
    	
    	return typeSummary;
    }
}
