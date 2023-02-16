package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthenticatePresenter implements AuthenticateNotificationObserver {
    public interface View extends AuthenticateView {
        boolean checkImage();

        Bitmap getImage();
    }
    
    private View view;

    public RegisterPresenter(View view) {
        this.view = view;
    }

    public void register(String firstName, String lastName, String alias, String password) {
        try {
            validateRegistration(firstName, lastName, alias, password);
            view.setErrorView(null);
            view.createToast("Registering...");

            // Convert image to byte array.
            Bitmap image = view.getImage();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            getUserService().register(firstName, lastName, alias, password, imageBytesBase64, this);
        } catch (Exception e) {
            view.setErrorView(e.getMessage());
        }
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (!view.checkImage()) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage("Failed to register: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayMessage("Failed to register because of exception: " + ex.getMessage());
    }

    @Override
    public void handleSuccess(User registeredUser, String message) {
        view.startMainActivity(registeredUser, message);
    }
}
