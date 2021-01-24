package bd.edu.seu.messengerapp.presenters;

public interface SplashPresenter extends BasePresenter {
    interface Model{
        void preProcessing();
        void redirectToNextPage();
    }
}
