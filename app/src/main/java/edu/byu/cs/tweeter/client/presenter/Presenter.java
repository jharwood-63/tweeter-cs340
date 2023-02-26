package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ServiceObserver;

public abstract class Presenter {
    public interface PresenterView {
        void displayMessage(String message);
    }

    protected PresenterView view;

    public Presenter(PresenterView view) {
        this.view = view;
    }

    public PresenterView getView() {
        return view;
    }

    protected abstract class PresenterObserver implements ServiceObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(getMessagePrefix() + message);
        }

        protected abstract String getMessagePrefix();

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(getExceptionPrefix() + ex.getMessage());
        }

        protected abstract String getExceptionPrefix();
    }
}
