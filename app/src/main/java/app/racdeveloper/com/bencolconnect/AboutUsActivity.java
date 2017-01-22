package app.racdeveloper.com.bencolconnect;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Window;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

/**
 * Created by Rachit on 11/13/2016.
 */
public class AboutUsActivity extends Activity {
    /**
     * This variable is the container that will host our cards
     */
    public static int countOfCards=0;
    private CardContainer mCardContainer;
    CardModel rachit, shashank, rishabh;
    SimpleCardStackAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us);

        mCardContainer = (CardContainer) findViewById(R.id.layoutview);

        rachit= new CardModel("Rachit Agarwal", "Computer Science & Engineering", ResourcesCompat.getDrawable(getResources(), R.drawable.picture2, null));

        shashank= new CardModel("Shashank Singh", "Computer Science & Engineering", ResourcesCompat.getDrawable(getResources(), R.drawable.picture3, null));
        shashank.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.i("Swipe", "Shashank is pressed");
            }
        });
        rishabh= new CardModel("Rishabh Singh", "Electronics & Communication Engineering", ResourcesCompat.getDrawable(getResources(), R.drawable.picture1, null));
        rishabh.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.i("Swipe", "Rishabh is pressed");
            }
        });

        rachit.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.i("Swipeable Cards","Rachit is pressed");
            }
        });
        rachit.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
            @Override
            public void onLike() {
                Log.i("Swipeable Cards","I like the card");
                if(countOfCards<1){
                    countOfCards++;
                }
                else{
                    countOfCards=0;
                    mCardContainer.setAdapter(adapter);
                }
            }

            @Override
            public void onDislike() {
                Log.i("Swipeable Cards","I dislike the card");
                if(countOfCards<1){
                    countOfCards++;
                }
                else{
                    countOfCards=0;
                    mCardContainer.setAdapter(adapter);
                }
            }
        });

        adapter = new SimpleCardStackAdapter(this);
        adapter.add(rishabh);
        adapter.add(shashank);
        adapter.add(rachit);
        adapter.add(rishabh);
        adapter.add(shashank);
        adapter.add(rachit);
        mCardContainer.setAdapter(adapter);
    }
}
