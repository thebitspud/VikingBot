package bot.VikingBot.util;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;


import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;


public class Music {
	
	/* MUSIC
	 * 
	 * Hello, welcome to the Music class. If you're here to learn how to use the Music class, 
	 * here's some code you can shove into the main method for App.java. 
	 * 
		Music.joinChannel(493168112338862082L); //Joins the bot to a channel.
		Music.configurePlayerManager(); //Setups Music. Only need to do this once.
		Music.play("https://www.youtube.com/watch?v=aW-7CqxhnAQ"); //Plays music from wherever you want! Youtube, soundcloud...
		
		If you're here to change code, beware, here there be dragons...
	 */
	
	
	//Channel that the bot is currently in.
	private static Long currentChannelId;
	
	//All the Audio variables
	private static AudioPlayerManager playerManager;
	private static AudioPlayer player;
	private static AudioEventListener trackScheduler;
	private static AudioManager audioManager;
	private static ArrayList<AudioPlayerSendHandler> AudioHandlers;
	private static JDA jda;
	
	public static AudioEventListener getTrackScheduler() {
		return trackScheduler;
	}
	
	//Sets the JDA that will be used to find voice channels and guilds.
	public static void setJDA(JDA JDA) {
		jda = JDA;
	}
	
	//Sets up the class to play music.
	public static void configureMusic() {
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		player = playerManager.createPlayer();
		trackScheduler = new TrackScheduler(player);
		player.addListener(trackScheduler);
		AudioHandlers = new ArrayList<>();
	}
	
	//Join channel by channel id.
	public static void joinChannel(Long channelId) {
		audioManager = jda.getVoiceChannelById(channelId).getGuild().getAudioManager();
		audioManager.openAudioConnection(jda.getVoiceChannelById(channelId));
		currentChannelId = channelId;
	}
	
	//Join channel by VoiceChannel
	public static void joinChannel(VoiceChannel vc) {
		audioManager = vc.getGuild().getAudioManager();

		audioManager.openAudioConnection(vc);
		currentChannelId = vc.getIdLong();
	}
	
	//Exits a voice channel.
	public static void exitChannel() {
		audioManager.closeAudioConnection();
		currentChannelId = 0L;
	}
	
	//Plays music, given by link.
	public static void play(String link) {
		//Get the voice channel that we are in.
		VoiceChannel vc = jda.getVoiceChannelById(currentChannelId);
		
		//See if an AudioPlayerSendHandler has already been made for thie voice channel.
		AudioPlayerSendHandler apsh;
		if(getAudioHandler(vc.getGuild()) == null) {
			apsh = new AudioPlayerSendHandler(player,vc.getIdLong());
			AudioHandlers.add(apsh);
		}else {
			apsh = getAudioHandler(vc.getGuild());
		}
		
		//Set the handler for the Audio Manager
		audioManager.setSendingHandler(apsh);
		
		//Load the link into the player manager.
		playerManager.loadItem(link, new AudioLoadResultHandler() {
			  

			@Override
			  public void trackLoaded(AudioTrack track) {
				((TrackScheduler)Music.trackScheduler).queue(track);
			  }

			  @Override
			  public void playlistLoaded(AudioPlaylist playlist) {
			    for (AudioTrack track : playlist.getTracks()) {
			    	((TrackScheduler)Music.trackScheduler).queue(track);
			    }
			  }

			  @Override
			  public void noMatches() {
			    // Notify the user that we've got nothing
			  }

			  @Override
			  public void loadFailed(FriendlyException throwable) {
			    // Notify the user that everything exploded
			  }
		});
		
	}
	
	//Get audio handler by Guild.
	public static AudioPlayerSendHandler getAudioHandler(Guild g) {
		//Cycle through all the AudioPlayerSendHandlers
		for(AudioPlayerSendHandler apsh : AudioHandlers){
			//If the AudioPlayerSendHandler matches the guild in the parameter, then return the Handler.
			if(apsh.getGuildId() == g.getIdLong()) {
				return apsh;
			}
		}
		//No matching AudioPlayerSendHandlers found.
		return null;
	}
	
	//Get audio handler by guild id.
	public static AudioPlayerSendHandler getAudioHandler(Long guildId) {
		//Cycle through all the AudioPlayerSendHandlers
		for(AudioPlayerSendHandler apsh : AudioHandlers){
			//If the AudioPlayerSendHandler matches the guild in the parameter, then return the Handler.
			if(apsh.getGuildId() == guildId) {
				return apsh;
			}
		}
		//No matching AudioPlayerSendHandlers found.
		return null;
	}
	
	
}
