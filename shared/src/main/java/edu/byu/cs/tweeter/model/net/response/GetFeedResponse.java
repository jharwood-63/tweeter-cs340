package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedResponse extends PagedResponse {
    private List<Status> feedPage;

    public GetFeedResponse(boolean hasMorePages, List<Status> feedPage) {
        super(true, hasMorePages);
        this.feedPage = feedPage;
    }

    public GetFeedResponse(String message, boolean hasMorePages) {
        super(false, message, hasMorePages);
    }

    public List<Status> getFeedPage() {
        return feedPage;
    }

    public void setFeedPage(List<Status> feedPage) {
        this.feedPage = feedPage;
    }
}
