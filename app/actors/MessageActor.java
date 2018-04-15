package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

public class MessageActor extends UntypedActor {

    //Self - Reference the Actor
    //PROPS
    //Object of Feed Service
    //Object of NewsAgentService
    //Define another actor Reference

    private final ActorRef out;

    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    public MessageActor(ActorRef out) {
        this.out = out;
    }

    private FeedService feedService = new FeedService();
    private NewsAgentService newsAgentService = new NewsAgentService();
    private NewsAgentResponse newsAgentResponse = new NewsAgentResponse();
    private FeedResponse feedResponse = new FeedResponse();

    @Override
    public void onReceive(Object message) throws Throwable {
        //Send back the response
        ObjectMapper mapper = new ObjectMapper();
        Message messageObject = new Message();
        if (message instanceof String) {
            messageObject.text = (String) message;
            messageObject.sender = Message.Sender.USER;
            out.tell(mapper.writeValueAsString(messageObject), self());
            //The function inside the tell converts the object into aa JSON string and self() will refer to the sender itself
            //newsAgentResponse = newsAgentService.getnewsagentresponse(messageObject.text,UUID.randomUUID()); not required
            String query = newsAgentService.getnewsagentresponse("Find " + message, UUID.randomUUID()).query;
            feedResponse = feedService.getfeedresponse(query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + query;
            messageObject.feedResponse = feedResponse;
            messageObject.sender = Message.Sender.BOT;
            out.tell(mapper.writeValueAsString(messageObject), self());
        }
        else{
            messageObject.text = "Input is invalid";
            messageObject.sender = Message.Sender.BOT;
            out.tell(mapper.writeValueAsString(messageObject), self());
        }
    }
}