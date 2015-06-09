package edu.washington.beerswains.beerwench;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JSONParser extends AsyncTask<String, Void, Boolean> {
    private JSONObject json;
    private BeerFinder finder;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public JSONParser(BeerFinder finder) {
        this.finder = finder;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {

            //------------------>>
            HttpGet httppost = new HttpGet(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);


                this.json = new JSONObject(data);

                return true;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {
        Log.e("json", this.json.toString());
        this.finder.setFound(this.json);
    }

    public JSONObject getJson() {
        return this.json;
    }
}