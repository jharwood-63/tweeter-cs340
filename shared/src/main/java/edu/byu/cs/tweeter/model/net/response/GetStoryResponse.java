package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryResponse extends PagedResponse {
    private List<Status> storyPage;

    public GetStoryResponse(List<Status> storyPage, boolean hasMorePages) {
        super(true, hasMorePages);
        this.storyPage = storyPage;
    }

    public GetStoryResponse(String message, boolean hasMorePages) {
        super(false, message, hasMorePages);
    }

    public List<Status> getStoryPage() {
        return storyPage;
    }

    public void setStoryPage(List<Status> storyPage) {
        this.storyPage = storyPage;
    }
}
