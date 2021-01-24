package bd.edu.seu.messengerapp.presenterImpls;

import android.os.Handler;

import bd.edu.seu.messengerapp.presenters.BasePresenter;
import bd.edu.seu.messengerapp.presenters.SplashPresenter;


public class SplashPresenterImpl implements SplashPresenter.Model {

    public SplashPresenterImpl(BasePresenter.View view) {
        this.view = view;
    }

    BasePresenter.View view;
    @Override
    public void preProcessing() {

    }

    @Override
    public void redirectToNextPage() {
        new Handler().postDelayed(() -> {
            view.onSuccess();
        }, 3000);
    }
}
