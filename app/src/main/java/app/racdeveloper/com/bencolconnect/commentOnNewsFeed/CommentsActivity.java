package app.racdeveloper.com.bencolconnect.commentOnNewsFeed;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import app.racdeveloper.com.bencolconnect.R;

public class CommentsActivity extends AppCompatActivity implements AddCommentFragment.ListUpdateListener, CommentList.ListUpdateListenerOnEdit{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Animation a= AnimationUtils.loadAnimation(this, R.anim.translate);
        a.reset();
        RelativeLayout rl= (RelativeLayout) findViewById(R.id.activity_comments);
        rl.clearAnimation();
        rl.startAnimation(a);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddCommentFragment addCommentFragment = new AddCommentFragment(getIntent().getExtras().getString("feedID"));
        CommentList listFragment  = new CommentList(this, getIntent().getExtras().getString("feedID"));
        fragmentTransaction.add(R.id.fragment_add,addCommentFragment);
        fragmentTransaction.add(R.id.fragment_list,listFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void listRefreshed() {
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_list, new CommentList(this, getIntent().getStringExtra("feedID")), null);
        transaction.commit();
    }

    @Override
    public void listRefreshedOnEdit() {
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_list, new CommentList(this, getIntent().getStringExtra("feedID")), null);
        transaction.commit();
    }
}
