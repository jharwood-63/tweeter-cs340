package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryPresenter extends PagedStatusPresenter {
    public GetStoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    public String getExceptionPrefix() {
        return "Failed to get story because of exception: ";
    }

    @Override
    public String getFailurePrefix() {
        return "Failed to get story: ";
    }
}
