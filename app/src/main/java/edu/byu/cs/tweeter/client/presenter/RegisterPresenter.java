package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    public interface View {
        boolean checkImage();

        void setErrorView(String message);

        void createToast(String message);

        Bitmap getImage();

        void displayMessage(String message);

        void startMainActivity(User registeredUser, String message);
    }

    private View view;

    private UserService userService;

    public RegisterPresenter(View view) {
        this.view = view;
        this.userService = new UserService();
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

            userService.register(firstName, lastName, alias, password, imageBytesBase64, new RegisterObserver());
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

    private class RegisterObserver implements UserService.RegisterObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void startMainActivity(User registeredUser, String message) {
            view.startMainActivity(registeredUser, message);
        }
    }
}
