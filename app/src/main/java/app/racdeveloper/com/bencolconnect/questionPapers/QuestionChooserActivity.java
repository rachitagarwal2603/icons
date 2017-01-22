package app.racdeveloper.com.bencolconnect.questionPapers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import app.racdeveloper.com.bencolconnect.R;

/**
 * Created by Rachit on 10/19/2016.
 */
public class QuestionChooserActivity extends AppCompatActivity{

    private boolean isSpinnerInitial = true;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    public static String branch = null;
    public static String semester = null;
    int branchPosition,semesterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_paper_chooser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(QuestionChooserActivity.this, "Select your choice", Toast.LENGTH_SHORT).show();

        spinner = (Spinner) findViewById(R.id.spinnerbranch);
        adapter = adapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                branchPosition = position;

                if (isSpinnerInitial) {
                    branch = String.valueOf(position);
                    isSpinnerInitial = false;
                } else {
                    if
                            (position == 0) ;
                    else {
                        branch = String.valueOf(position);
                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        spinner = (Spinner) findViewById(R.id.spinnersemester);
        adapter = adapter.createFromResource(this, R.array.semester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                semesterPosition = position;

                if (isSpinnerInitial) {
                    semester = String.valueOf(position);
                    isSpinnerInitial = false;
                } else {
                    if
                            (position == 0) ;
                    else {
                        semester = String.valueOf(position);
                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        Button button = (Button) findViewById(R.id.search_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    if(branchPosition != 0 && semesterPosition != 0)
                    {
                        Intent questionIntent = new Intent(QuestionChooserActivity.this, QuestionPaperActivity.class);
                        startActivity(questionIntent);
                    }
                    else
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(QuestionChooserActivity.this).create();
                        alertDialog.setTitle("Inappropriate Choices Selected");
                        alertDialog.setMessage("Please,select one of the given choices to fetch the question papers.");
                        alertDialog.setIcon(R.drawable.emergency);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            });
        }

    }

}
