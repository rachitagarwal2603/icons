package app.racdeveloper.com.bencolconnect.resumesList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.racdeveloper.com.bencolconnect.Constants;
import app.racdeveloper.com.bencolconnect.R;

public class ResumeActivity extends AppCompatActivity {

    DownloadManager manager;
    private View mProgressView;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fabResume;
    private ResumeViewAdapter adapter;
    private List<ResumeData> data_list;
    RequestQueue requestQueue;
    String URL = Constants.URL + "resumes/get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coLayout);
        fabResume = (FloatingActionButton) findViewById(R.id.fabResume);
        fabResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "     Upload your Resume", Snackbar.LENGTH_LONG)
                        .setAction("UPLOAD", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                i.setType("application/pdf");
                                i.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(Intent.createChooser(i, "Select your Resume PDF"), 1);
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(Color.WHITE);

                // Changing action button text color
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.rgb(25,25,112));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(18);
                snackbar.show();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressView = findViewById(R.id.resume_progress);
        data_list = new ArrayList<>();
        showProgress(true);
        load_resume(0);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ResumeViewAdapter(this, data_list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK && data!=null){
            Uri mUri= data.getData();

//            File dir= Environment.getExternalStorageDirectory();
//            File filePDF= new File(dir, mUri.toString());
//            String encodeFileToBase64Binary = encodeFileToBase64Binary(filePDF);

//            Toast.makeText(this, "Encoded : "+ encodeFileToBase64Binary, Toast.LENGTH_SHORT).show();
            String encoded = null;
            byte[] byteArray = new byte[0];
            try {
                InputStream inputStream = new FileInputStream(mUri.toString());

                byteArray = IOUtils.toByteArray(inputStream);
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Encoded : "+ encoded, Toast.LENGTH_SHORT).show();


            Snackbar snack= Snackbar.make(coordinatorLayout, ""+ mUri.toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("SEND", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            snack.setActionTextColor(Color.WHITE);

            View sbView = snack.getView();
            sbView.setBackgroundColor(Color.rgb(25,25,112));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            snack.show();
        }
    }

    private String encodeFileToBase64Binary(File filePDF) {
        String encodedString = "0";
        try {
            InputStream inputStream = new FileInputStream(filePDF);//You can get an inputStream using any IO API
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                Toast.makeText(ResumeActivity.this, "Encoded2 : "+ encodedString, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            Toast.makeText(ResumeActivity.this, "Encoded3 : "+ encodedString, Toast.LENGTH_SHORT).show();

        } catch (IOException es) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return encodedString;
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
            protected Void doInBackground(Integer... integers) {

                requestQueue = Volley.newRequestQueue(ResumeActivity.this);
                Map<String, String> param = new HashMap<>();
                //param.put("token", PreferenceManager.getDefaultSharedPreferences(ResumeActivity.this).getString("token", null));
                JsonObjectRequest object = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(param), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("pppp","inOn response");
                            showProgress(false);
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
                });
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
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
