package edu.byu.cs.tweeter.client.presenter;

public abstract class Presenter {
    public interface PresenterView {
        void displayMessage(String message);
    }
}
