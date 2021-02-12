package bd.edu.seu.messengerapp.presenters;


public interface BasePresenter {
    interface View{
        void onSuccess();
        void onError(String... message);
        void showProgressBar( String message);
        void hideProgressBar();
    }
    interface CommonView {
        void onSuccess();
        void onError(String... message);
    }

}
