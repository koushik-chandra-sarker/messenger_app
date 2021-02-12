package bd.edu.seu.messengerapp.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAUUf-2UA:APA91bEV8Cx97QNkgP8V35VzhM14w9tA23IXPy4o4HahuSg9pbmGU878RgA5ELG_SBo6vHopUaN-bjn5zInVxTQ6Q0bE8aUE6j9P4qF65MTeX_4VeQ6hwV51rvm9x_QuHMskxqF_zWvz"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender sender);
}
