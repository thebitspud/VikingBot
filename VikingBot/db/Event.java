package bot.VikingBot.db;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import bot.VikingBot.constants.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class Event {
	
	private String name;
	private String description;
	private Date initialDate;
	private Date endDate;
	private String imageURL;
	private String type;
	private Color colour;

	public Event(String name, String description, Date initialDate, Date endDate, String imageURL, String type,
			Color colour) {
		super();
		this.name = name;
		this.description = description;
		this.initialDate = initialDate;
		this.endDate = endDate;
		this.imageURL = imageURL;
		this.type = type;
		this.colour = colour;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	public MessageEmbed getEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle(name);
		eb.setDescription(description);
		eb.addField("Event stars",Ref.dateFormat.format(initialDate),true);
		eb.addField("Event ends",Ref.dateFormat.format(endDate),true);
		eb.addField("Type",type,true);
		eb.setThumbnail(imageURL);
		eb.setColor(this.colour);
		
		return eb.build();
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLogoURL() {
		return imageURL;
	}

	public void setLogoURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public static Event fromJson(String json) {
		Gson gson = new Gson();
		Type typeEvent = new TypeToken<Event>() {}.getType();
		Event evt = gson.fromJson(json,typeEvent);
		
		return evt;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		Type typeEvent = new TypeToken<Event>() {}.getType();
		String json = gson.toJson(this, typeEvent);
		
		return json;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
