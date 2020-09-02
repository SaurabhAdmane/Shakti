package com.shakticoin.app.api;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class BackendRepository implements LifecycleObserver {
    private LifecycleOwner lifecycleOwner;

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        if (this.lifecycleOwner != null) {
            this.lifecycleOwner.getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected  void onPause() {
    }

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

    /**
     * Different API returns error messages differently but often this is a field of
     * an object. This utility method provides a convenient method to extract error message
     * from response body carrying a JSON object.
     * @param name Name of the field
     */
    protected @NonNull String getResponseErrorMessage(@NonNull String name, @Nullable ResponseBody errorBody) {
        if (errorBody != null) {
            try {
                String content = errorBody.string();
                if (!TextUtils.isEmpty(content)) {
                    JSONObject json = new JSONObject(content);
                    if (json.has(name)) {
                        return json.getString(name);
                    }
                }
            } catch (IOException | JSONException e) {
                Debug.logException(e);
            }
        }
        return ShaktiApplication.getContext().getString(R.string.err_unexpected);
    }
}
