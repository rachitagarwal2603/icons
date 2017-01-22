package app.racdeveloper.com.bencolconnect.fetchProfiles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.racdeveloper.com.bencolconnect.Constants;
import app.racdeveloper.com.bencolconnect.QueryPreferences;
import app.racdeveloper.com.bencolconnect.R;
import app.racdeveloper.com.bencolconnect.pushNotification.NotificationWebview;

/**
 * Created by Rachit on 11/21/2016.
 */
public class MyProfile extends AppCompatActivity {
    View mProgressView;
    private MyProfileData myProfileData;
    RequestQueue requestQueue;
    String URL = Constants.URL + "basics/user_details";
    String searchString, searchRollno, token;

    ImageView userImage, fbContactButton, TwitterContactButton, LinkedinContactButton, update;
    TextView userName, userBio, userBranch, userBatch, userId, userCouncils, userSkills, userHobbies, userBlood, userAddress,
            userEmail, userWebsite, userContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_profile_activity);

        mProgressView = findViewById(R.id.progressbar);
        showProgress(true);

        userImage = (ImageView) findViewById(R.id.user_profile_photo);
        update = (ImageView) findViewById(R.id.profile_update);

        userName = (TextView) findViewById(R.id.user_profile_name);
        userBio = (TextView) findViewById(R.id.user_profile_short_bio);
        userBranch = (TextView) findViewById(R.id.user_profile_branch);
        userBatch = (TextView) findViewById(R.id.user_profile_batch);
        userId = (TextView) findViewById(R.id.user_profile_id);
        userCouncils = (TextView) findViewById(R.id.user_profile_councils);
        userSkills = (TextView) findViewById(R.id.user_profile_skills);
        userHobbies = (TextView) findViewById(R.id.user_profile_hobbies);
        userBlood = (TextView) findViewById(R.id.user_profile_blood);
        userAddress = (TextView) findViewById(R.id.user_profile_address);
        userEmail = (TextView) findViewById(R.id.user_profile_email);
        userWebsite = (TextView) findViewById(R.id.user_profile_website);
        userContact = (TextView) findViewById(R.id.user_profile_contact);

        fbContactButton = (ImageView) findViewById(R.id.fb_link_image);
        TwitterContactButton = (ImageView) findViewById(R.id.twitter_link_image);
        LinkedinContactButton = (ImageView) findViewById(R.id.linkedin_link_image);

        Intent i = getIntent();
        boolean isUpdateRequired = (i.getStringExtra("userProfileRequest"))==null;
        if(!isUpdateRequired){
            update.setVisibility(View.GONE);
            URL = Constants.URL + "basics/get_profile";
            searchString= i.getStringExtra("userProfileRequest");
            searchRollno= i.getStringExtra("userRollno");
            Toast.makeText(MyProfile.this, searchString + searchRollno, Toast.LENGTH_SHORT).show();
        }

        token= QueryPreferences.getToken(MyProfile.this);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog();
            }
        });
        load_Profile();
    }

    private void createAlertDialog() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(MyProfile.this);
        builder.setTitle("Select your Choice");

        LinearLayout layout = new LinearLayout(MyProfile.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView profilePic = new TextView(MyProfile.this);
        profilePic.setTextSize(20);
        profilePic.setText("\n\n\t\t\t\tUpdate your Profile Pic");
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i= new Intent(MyProfile.this, UpdateProfilePic.class);
                startActivity(i);
            }
        });
        layout.addView(profilePic);

        final TextView profileDetails = new TextView(MyProfile.this);
        profileDetails.setTextSize(20);
        profileDetails.setText("\n\t\t\t\tUpdate your Profile Details");
        profileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i= new Intent(MyProfile.this, UpdateProfileDetails.class);
                startActivity(i);
            }
        });
        layout.addView(profileDetails);

        builder.setView(layout);
        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void contactToWeb(String contactURL) {
        Intent intent = new Intent(this, NotificationWebview.class);
        intent.putExtra("uri", contactURL);
        startActivity(intent);
    }

    private void load_Profile() {

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... integers) {
                requestQueue = Volley.newRequestQueue(MyProfile.this);
                Map<String, String> param = new HashMap<>();

                if(searchString!=null) {             //   post rollno and token
                    param.put("string", searchString);
                    param.put("rollno", searchRollno);
                }

                param.put("token", token);

                JsonObjectRequest object = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(param),
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        showProgress(false);
                        try {
                            if(jsonObject!=null) {
                                Log.i("ppppp", jsonObject.toString());

                                JSONObject profileObject = jsonObject.getJSONObject("details");

                                Log.i("ppppp", profileObject.toString());
                                    myProfileData = new MyProfileData();
                                    myProfileData.setProfileName(profileObject.getString("name"));
                                    myProfileData.setProfileImageUrl(profileObject.getString("imageUrl"));
//                                myProfileData.setProfileBio(profileObject.getString("bio"));
                                    myProfileData.setProfileBranch(profileObject.getString("branch"));
                                    myProfileData.setProfileBatch(profileObject.getString("batch"));
                                    myProfileData.setProfileId(profileObject.getString("rollno"));
//                                myProfileData.setProfileCouncils(profileObject.getString(""));
//                                myProfileData.setProfileSkills(profileObject.getString(""));
//                                myProfileData.setProfileHobbies(profileObject.getString(""));
//                                myProfileData.setProfileBlood(profileObject.getString(""));
                                    myProfileData.setProfileEmail(profileObject.getString("email"));
//                                myProfileData.setProfileAddress(profileObject.getString(""));
//                                myProfileData.setProfileWebsite(profileObject.getString(""));
//                                myProfileData.setProfileFb(profileObject.getString(""));
//                                myProfileData.setProfileTwitter(profileObject.getString(""));
//                                myProfileData.setProfileLinkedin(profileObject.getString(""));
//                                myProfileData.setProfileContact(profileObject.getString(""));
                                    populateMyProfile();

                            }
                            else{
                                Toast.makeText(MyProfile.this, "FAIL!!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        task.execute();
    }

    private void populateMyProfile(){
        if(!myProfileData.getProfileImageUrl().equals(""))
            Glide.with(MyProfile.this).load(myProfileData.getProfileImageUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(userImage);

//            Picasso.with(this).load(myProfileData.getProfileImageUrl()).into(userImage);

        userName.setText(myProfileData.getProfileName());
        userBio.setText(myProfileData.getProfileBio());
        userBranch.append("\t\t\t"+myProfileData.getProfileBranch());
        userBatch.append("\t\t\t"+myProfileData.getProfileBatch());
        userId.append("\t\t\t"+myProfileData.getProfileId());
        userCouncils.append("\t\t\t"+myProfileData.getProfileCouncils());
        userSkills.append("\t\t\t"+myProfileData.getProfileSkills());
        userHobbies.append("\t\t\t"+myProfileData.getProfileHobbies());
        userBlood.append("\t\t\t"+myProfileData.getProfileBlood());
        userAddress.append("\t\t\t"+myProfileData.getProfileAddress());
        userEmail.append("\t\t\t"+myProfileData.getProfileEmail());
        userWebsite.append("\t\t\t"+myProfileData.getProfileWebsite());
        userContact.append("\t\t\t"+myProfileData.getProfileContact());

        //if (myProfileData.getProfileFb() != null) {
        if (true) {
            fbContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactToWeb("https://www.facebook.com/rishu11116432");
                }
            });
        }
        if (myProfileData.getProfileTwitter() != null) {
            TwitterContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactToWeb(myProfileData.getProfileTwitter());
                }
            });
        }
        if (myProfileData.getProfileLinkedin() != null) {
            LinkedinContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactToWeb(myProfileData.getProfileLinkedin());
                }
            });
        }
    }

    private void showProgress(final boolean show) {
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

