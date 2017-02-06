package app.racdeveloper.com.bencolconnect.fetchProfiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.racdeveloper.com.bencolconnect.Constants;
import app.racdeveloper.com.bencolconnect.QueryPreferences;
import app.racdeveloper.com.bencolconnect.R;

/**
 * Created by Rachit on 11/23/2016.
 */
public class UpdateProfileDetails extends AppCompatActivity{
    private static String URL = Constants.URL + "";
    private static String resultKey = null;
    EditText etName, etContact, etCouncil, etSkills, etHobbies, etBloodGroup, etAddress, etFacebookLink, etTwitterLink, etLinkedInLink;
    TextView tvSubmit;
    String name, contact, council, skills, hobbies, bloodGroup, address, facebookLink, twitterLink, linkedinLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_details_activity);

        etName = (EditText) findViewById(R.id.etName_Details);
        etContact = (EditText) findViewById(R.id.etContact_Details);
        etCouncil = (EditText) findViewById(R.id.etCouncils_Details);
        etSkills = (EditText) findViewById(R.id.etSkills_Details);
        etHobbies = (EditText) findViewById(R.id.etHobbies_Details);
        etBloodGroup = (EditText) findViewById(R.id.etBloodGroup_Details);
        etAddress = (EditText) findViewById(R.id.etAddress_Details);
        etFacebookLink = (EditText) findViewById(R.id.etFacebookLink_Details);
        etTwitterLink = (EditText) findViewById(R.id.etTwitterLink_Details);
        etLinkedInLink = (EditText) findViewById(R.id.etLinkedInLink_Details);

        tvSubmit = (TextView) findViewById(R.id.tvSubmit_Details);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = etName.getText().toString();
                contact = etContact.getText().toString();
                council = etCouncil.getText().toString();
                skills = etSkills.getText().toString();
                hobbies = etHobbies.getText().toString();
                bloodGroup = etBloodGroup.getText().toString();
                address = etAddress.getText().toString();
                facebookLink = etFacebookLink.getText().toString();
                twitterLink = etTwitterLink.getText().toString();
                linkedinLink = etLinkedInLink.getText().toString();

                checkSubmitDetails();
                Toast.makeText(UpdateProfileDetails.this, "Submit is Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkSubmitDetails() {
        View focusView = null;
        boolean setProfileCheck=true;
        if(contact.equals("")){
            etContact.setError("Field Required");
            focusView = etContact;
            setProfileCheck=false;
        }
        else if(skills.equals("")){
            etSkills.setError("Skills Required");
            focusView= etSkills;
            setProfileCheck=false;
        }
        else if (bloodGroup.equals("")){
            etBloodGroup.setError("Field Required");
            focusView= etBloodGroup;
            setProfileCheck=false;
        }
        else if(address.equals("")){
            etAddress.setError("Field Required");
            focusView= etAddress;
            setProfileCheck=false;
        }
        else if (facebookLink.equals("")){
            etFacebookLink.setError("Field Required");
            focusView= etFacebookLink;
            setProfileCheck=false;
        }
        if(setProfileCheck){
            setProfile();
        }else{
            focusView.requestFocus();
        }
    }

    private void setProfile(){

        final RequestQueue requestQueue= Volley.newRequestQueue(UpdateProfileDetails.this);
        String profilePicString="";
        Map<String, String> param= new HashMap<>();
        param.put("token", QueryPreferences.getToken(UpdateProfileDetails.this));
        param.put("name", profilePicString);
        param.put("contact", profilePicString);
        param.put("councils", profilePicString);
        param.put("skills", profilePicString);
        param.put("hobbies", profilePicString);
        param.put("bloodGroup", profilePicString);
        param.put("address", profilePicString);
        param.put("facebookLink", profilePicString);
        param.put("twitterLink", profilePicString);
        param.put("linkedinLink", profilePicString);


        JsonObjectRequest jor= new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(param), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject!=null) {
                    try {
                        resultKey = jsonObject.getString("result");

                        if(resultKey.equals("success")){
                            Toast.makeText(UpdateProfileDetails.this, "Profile Details are successfully updated!!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(UpdateProfileDetails.this, "Submit Again!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ppp", "Volley Error : "+volleyError);
            }
        });
        requestQueue.add(jor);
        finish();
    }
}
