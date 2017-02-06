package app.racdeveloper.com.bencolconnect.aboutUs;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.racdeveloper.com.bencolconnect.R;
/**
 * Created by Rachit on 2/6/2017.
 */

public class DeveloperActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DeveloperViewAdapter developerViewAdapter;
    private List<DeveloperData> developerDataList;
    private static final String [] RachitDeveloper = {"Rachit Agarwal","Computer Science & Engineering, 2014\nS/O Amit & Radha Agarwal","https://www.facebook.com/RachitAgarwal2603",
            "https://in.linkedin.com/in/rachitagarwal2603","https://twitter.com/agarwal2603","https://plus.google.com/106068619461123247587"};
    private static final String [] ShashankDeveloper = {"Shashank Singh","Computer Science & Engineering, 2014","https://www.facebook.com/ssinghhdi",
            "https://www.linkedin.com/in/shashank-singh-477217b4","https://twitter.com","https://plus.google.com/114020383148970954239"};
    private static final String [] RishabhDeveloper = {"Rishabh Singh","Electronics & Communication Engineering, 2014","https://www.facebook.com/rishu11116432",
            "https://www.linkedin.com/in/rishusingh11116432","https://twitter.com/rishu11116432","https://plus.google.com/+RishabhSingh11116432"};
    private static final Integer [] DeveloperPhotos = {R.drawable.rachit,R.drawable.shashank,R.drawable.rishabh};

    public DeveloperActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        mRecyclerView = (RecyclerView) findViewById(R.id.aboutUs_recycler_view);
        developerDataList = new ArrayList<>();
        load_developer_information();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        developerViewAdapter = new DeveloperViewAdapter(this,developerDataList);
        mRecyclerView.setAdapter(developerViewAdapter);
    }

    private void load_developer_information() {

        DeveloperData rachitDeveloper = new DeveloperData(1,DeveloperPhotos[0],RachitDeveloper[0],RachitDeveloper[1],RachitDeveloper[2],
                RachitDeveloper[3],RachitDeveloper[4],RachitDeveloper[5]);
        developerDataList.add(rachitDeveloper);
        DeveloperData shashankDeveloper = new DeveloperData(2,DeveloperPhotos[1],ShashankDeveloper[0],ShashankDeveloper[1],ShashankDeveloper[2],
                ShashankDeveloper[3],ShashankDeveloper[4],ShashankDeveloper[5]);
        developerDataList.add(shashankDeveloper);
        DeveloperData rishabhDeveloper = new DeveloperData(3,DeveloperPhotos[2],RishabhDeveloper[0],RishabhDeveloper[1],RishabhDeveloper[2],
                RishabhDeveloper[3],RishabhDeveloper[4],RishabhDeveloper[5]);
        developerDataList.add(rishabhDeveloper);
    }
}
