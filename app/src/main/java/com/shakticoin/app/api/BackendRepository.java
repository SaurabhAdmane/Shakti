package com.shakticoin.app.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class BackendRepository {

    /**
     * Call the callback for a repository method in order to report an error.
     */
    protected void returnError(@NonNull OnCompleteListener<?> listener, @NonNull Throwable error) {
        Debug.logException(error);
        listener.onComplete(null, error);
    }

    /**
     * Call the callback for a repository method in order to report an error.
     * It can return exception of different types depending on error code.
     */
    protected void returnError(@NonNull OnCompleteListener<?> listener, @NonNull Response<?> response) {
        ResponseBody body = response.errorBody();
        if (body != null) {
            try {
                Debug.logDebug(body.string());
            } catch (IOException e) {
            }
        }
        if (response.code() == 401) {
            listener.onComplete(null, new UnauthorizedException());
        } else {
            listener.onComplete(null, new RemoteException(response.message(), response.code()));
        }
    }

    /**
     * The method parse errorBody and return JSONObject. If response is not an object but array it
     * still returns JSONObject where the array is available under the key "rootArray".
     */
    protected JSONObject errorBodyJSON(@Nullable ResponseBody body) {
        if (body == null) return null;
        try {
            String context = body.string();
            try {
                return new JSONObject(context);

            } catch (JSONException e1) {
                // this might be not an object but array
                try {
                    JSONArray array = new JSONArray(context);
                    JSONObject obj = new JSONObject();
                    obj.put("rootArray", array);
                    return obj;

                } catch (JSONException e2) {
                    return null;
                }
            }

        } catch (IOException e) {
            return null;
        }
    }
}
