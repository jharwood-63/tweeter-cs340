package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticateNotificationObserver;

public class RegisterPresenter extends AuthenticatePresenter {
    public interface RegisterView extends AuthenticateView {
        boolean checkImage();

        Bitmap getImage();
    }

    public RegisterPresenter(RegisterView view) {
        super(view);
    }

    private RegisterView getRegisterView() {
        return ((RegisterView) getView());
    }

    public void register(String firstName, String lastName, String alias, String password) {
        try {
            validateRegistration(firstName, lastName, alias, password);
            setupAuthentication("Registering...");

            // Convert image to byte array.
            Bitmap image = getRegisterView().getImage();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            getUserService().register(firstName, lastName, alias, password, imageBytesBase64, new AuthenticateObserver());
        } catch (Exception e) {
            setErrorView(e.getMessage());
        }
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }

        validateAlias(alias);
        validatePassword(password);

        if (!getRegisterView().checkImage()) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }
}
