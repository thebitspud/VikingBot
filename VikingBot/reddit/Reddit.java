//package bot.VikingBot.reddit;
//
//import org.apache.http.impl.client.HttpClientBuilder;
//
//import com.github.jreddit.entity.Submission;
//import com.github.jreddit.retrieval.params.SubmissionSort;
//import com.google.api.services.youtube.YouTube.Activities.List;
//
//public class Reddit {
//	
//	public static void setUp() {
//		// Information about the app
//		String userAgent = "jReddit: Reddit API Wrapper for Java";
//		String clientID = "JKJF3592jUIisfjNbZQ";
//		String redirectURI = "https://www.example.com/auth";
//
//		// Reddit application
//		RedditApp redditApp = new RedditInstalledApp(clientID, redirectURI);
//		RedditOAuthAgent agent = new RedditOAuthAgent(userAgent, redditApp);    
//		RedditClient client = new RedditHttpClient(userAgent, HttpClientBuilder.create().build());
//
//		// Create a application-only token (will be valid for 1 hour)
//		RedditToken token = agent.tokenAppOnly(false);
//
//		// Create parser for request
//		SubmissionsListingParser parser = new SubmissionsListingParser();
//
//		// Create the request
//		SubmissionsOfSubredditRequest request = (SubmissionsOfSubredditRequest) new SubmissionsOfSubredditRequest("programming", SubmissionSort.HOT).setLimit(100);
//
//		// Perform and parse request, and store parsed result
//		List<Submission> submissions = parser.parse(client.get(token, request));
//
//		// Now print out the result (don't care about formatting)
//		System.out.println(submissions);
//	}
//}
