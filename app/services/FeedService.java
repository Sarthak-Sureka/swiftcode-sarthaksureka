package services;

import data.FeedResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class FeedService {
    public FeedResponse getfeedresponse(String query){
        FeedResponse feedResponse = new FeedResponse();
        try{
            WSRequest feedRequest = WS.url("https://news.google.com/news");
            CompletionStage<WSResponse> responsePromise = feedRequest
                    .setQueryParameter("q",query)
                    .setQueryParameter("output","rss")
                    .get();
            Document response = responsePromise.thenApply(WSResponse::asXml).toCompletableFuture().get();
            Node item = response.getFirstChild().getFirstChild().getChildNodes().item(10);
            feedResponse.title = item.getChildNodes().item(0).getFirstChild().getNodeValue();
            //item = 0 will return the whole item node in which the title node is the first child and
            // if it would have contained sub nodes then that would be starting from second node
            feedResponse.description = item.getChildNodes().item(4).getFirstChild().getNodeValue();
            feedResponse.pubDate = item.getChildNodes().item(3).getFirstChild().getNodeValue();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return feedResponse;
    }

}
