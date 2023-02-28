package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryPresenter extends PagedStatusPresenter {
    public GetStoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    protected String getExceptionPrefix() {
        return "Failed to get story because of exception: ";
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to get story: ";
    }
}
