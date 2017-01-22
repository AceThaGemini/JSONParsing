package creationsofali.json;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ActorsListAdapter listAdapter;
    ListView listView;
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemDecoration decoration;
    ActorsRecyclerAdapter recyclerAdapter;

    ArrayList<Actors> actorsList;
    String viewCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        viewCode = getIntent().getStringExtra("viewCode");
        if (viewCode.equals("l")) {
            // inflate listView
            getSupportActionBar().setTitle("ListView");
            listView = (ListView) findViewById(R.id.list);

        } else if (viewCode.equals("r")) {
            // inflate recyclerView
            getSupportActionBar().setTitle("RecyclerView");
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            decoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
            recyclerView.addItemDecoration(decoration);
        }
        actorsList = new ArrayList<>();

        new ActorsAsyncTask().execute("http://microblogging.wingnity.com/JSONParsingTutorial/jsonActors");

    }

    // todo in background thread
    public class ActorsAsyncTask extends AsyncTask<String, Void, Boolean> {

        // background thread
        @Override
        protected Boolean doInBackground(String... params) {

            /** depricated shit */
//            HttpClient client;
//            HttpPost post;
//            HttpResponse response;

            Log.d("ASYNC", "background thread started");

            HttpURLConnection urlConnection = null;
            InputStream inStream;
            BufferedReader bufferedReader;
            StringBuffer stringBuffer;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection != null) {
                    Log.d("URL", "conn okay.");
                    Log.d("URL", "status code " + urlConnection.getResponseCode());
                } else {
                    Log.d("URL", "conn not okay");
                    Log.d("URL", "status code " + urlConnection.getResponseCode());
                }

                // if status code == 200, i.e: OK
                if (urlConnection.getResponseCode() == 200 /*HttpURLConnection.HTTP_OK*/) {

                    // on connection success
                    inStream = new BufferedInputStream(urlConnection.getInputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(inStream));
                    stringBuffer = new StringBuffer();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\n");

                    }
                    String data = stringBuffer.toString();

                    JSONObject dataObject = new JSONObject(data);
                    JSONArray jArray = dataObject.getJSONArray("actors");
                    Log.d("JSONArray", jArray.length() + " objects");

                    for (int i = 0; i < jArray.length(); i++) {
                        // for each particular index
                        JSONObject exactObject = jArray.getJSONObject(i);

                        // creating object
                        Actors actor = new Actors();
                        actor.setName(exactObject.getString("name"));
                        actor.setHeight(exactObject.getString("height"));
                        actor.setDob(exactObject.getString("dob"));
                        actor.setDescription(exactObject.getString("description"));
                        actor.setCountry(exactObject.getString("country"));
                        actor.setSpouse(exactObject.getString("spouse"));
                        actor.setChildren(exactObject.getString("children"));
                        actor.setImage(exactObject.getString("image"));

                        //adding to list
                        actorsList.add(actor);
                    }
                    // if parsing successful
                    return true;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                // preventing memory leak
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            // if parsing unsuccessful
            return false;
        }

        // to publish results in UI thread
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);

            if (!result) {
                // parsing failed
                Snackbar.make(findViewById(R.id.activity_main),
                        "Parsing failed! Check internet connection",
                        Snackbar.LENGTH_INDEFINITE)
                        .setDuration(10000).show();
            } else {
                //parsing successful, initialize listAdapter
                if (viewCode.equals("l")) {
                    // set listView
                    listAdapter = new ActorsListAdapter(getApplicationContext(), R.layout.list_item, actorsList);
                    listView.setAdapter(listAdapter);
                    listView.setVisibility(View.VISIBLE);

                } else if (viewCode.equals("r")) {
                    // set recyclerView
                    recyclerAdapter = new ActorsRecyclerAdapter(actorsList);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
