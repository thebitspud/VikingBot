package bot.VikingBot.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
	  private final AudioPlayer audioPlayer;
	  private AudioFrame lastFrame;
	  private Long guildId;

	  public AudioPlayerSendHandler(AudioPlayer audioPlayer, Long guildId) {
	    this.audioPlayer = audioPlayer;
	    this.guildId = guildId;
	  }
	  
	  public Long getGuildId() {
		  return guildId;
	  }
	  
	  @Override
	  public boolean canProvide() {
	    lastFrame = audioPlayer.provide();
	    return lastFrame != null;
	  }

	  @Override
	  public byte[] provide20MsAudio() {
	    return lastFrame.getData();
	  }

	  @Override
	  public boolean isOpus() {
	    return true;
	  }
}