package app.racdeveloper.com.bencolconnect.resumesList;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.racdeveloper.com.bencolconnect.Constants;
import app.racdeveloper.com.bencolconnect.R;

public class ResumeActivity extends AppCompatActivity {

    DownloadManager manager;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ResumeViewAdapter adapter;
    private List<ResumeData> data_list;
    RequestQueue requestQueue;
    String URL = Constants.URL + "resumes/get";
    //String URL = "http://192.168.3.103/laravel-projects/webapi/public_html/api/resumes/get";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        load_resume(0);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ResumeViewAdapter(this, data_list);
        recyclerView.setAdapter(adapter);


    }

    public void download(final View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to download this resume ?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                int itemPosition= recyclerView.getChildAdapterPosition(view);
                String url = data_list.get(itemPosition).getResumeUrl();
                String title = data_list.get(itemPosition).getResumeName();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("Downloading Resume of " + title);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Resume of " + title + url.substring(url.lastIndexOf(".")));
                manager.enqueue(request);
                Toast.makeText(ResumeActivity.this,"Downloading Resume of " + title, Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void load_resume(int id) {
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected Void doInBackground(Integer... integers) {

                requestQueue = Volley.newRequestQueue(ResumeActivity.this);
                Map<String, String> param = new HashMap<>();
                //param.put("token", PreferenceManager.getDefaultSharedPreferences(ResumeActivity.this).getString("token", null));
                JsonObjectRequest object = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(param), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("pppp","inOn response");
                            JSONArray resumeArray = response.getJSONArray("resume");
                            for (int i = 0; i < resumeArray.length(); i++) {
                                JSONObject resumeObject = resumeArray.getJSONObject(i);
                                ResumeData resume = new ResumeData(resumeObject.getInt("id"), resumeObject.getString("name"),
                                        resumeObject.getString("branch"), resumeObject.getString("batch"),
                                        resumeObject.getString("url"));
                                data_list.add(resume);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", "Error");
                            }
                        }
                );
                requestQueue.add(object);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                }
        };
        task.execute(id);
    }
}
