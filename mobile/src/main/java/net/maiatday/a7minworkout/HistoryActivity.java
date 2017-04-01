package net.maiatday.a7minworkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import net.maiatday.a7minworkout.model.Record;
import net.maiatday.a7minworkout.model.RecordFields;
import net.maiatday.a7minworkout.model.Streak;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public class HistoryActivity extends AppCompatActivity implements RealmRecordRecyclerAdapter.OnRowClicked {

    private Realm realm;
    private RecyclerView recyclerView;
    private Streak streak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        streak = realm.where(Streak.class).findFirst();
        if (streak != null) {
            getSupportActionBar().setTitle(getString(R.string.history_title, streak.getStreak()));
        }
        setUpRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareStreak();
            }
        });
    }

    private void shareStreak() {
        if (streak != null) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_title));
            String sAux = getString(R.string.streak_text, streak.getStreak());
            sAux = sAux + getString(R.string.app_url);
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, getString(R.string.choose_one)));
        } else {
            Toast.makeText(this, R.string.no_streak_share, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, HistoryActivity.class);
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        OrderedRealmCollection<Record> results = realm.where(Record.class).findAllSortedAsync(RecordFields.TIME_STAMP, Sort.ASCENDING);
        OrderedRealmCollection<Record> results = realm.where(Record.class).findAllSortedAsync(RecordFields.TIME_STAMP, Sort.ASCENDING);
        recyclerView.setAdapter(new RealmRecordRecyclerAdapter(this, results));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onLongClick(Record data) {
        Toast.makeText(this, "TODO future feature add note", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Record data) {
        Toast.makeText(this, "TODO future feature add note", Toast.LENGTH_SHORT).show();
    }
}
