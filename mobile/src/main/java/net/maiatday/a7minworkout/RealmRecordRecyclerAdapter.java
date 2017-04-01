package net.maiatday.a7minworkout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.maiatday.a7minworkout.model.Record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by maia on 2017/04/01.
 */

class RealmRecordRecyclerAdapter extends RealmRecyclerViewAdapter<Record, RealmRecordRecyclerAdapter.MyViewHolder> {
    public RealmRecordRecyclerAdapter(OnRowClicked listener, OrderedRealmCollection<Record> data) {
        super(data, true);
        this.listener = listener;
    }

    private final OnRowClicked listener;

    static DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

    public static String shortDateString(Date date) {
        return dateFormat.format(date);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_record, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Record obj = getData().get(position);
        holder.data = obj;
        holder.note.setText(obj.getNote());
        holder.timestamp.setText(shortDateString(obj.getTimeStamp()));
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public TextView note;
        public TextView timestamp;
        public Record data;

        public MyViewHolder(View view) {
            super(view);
            note = (TextView) view.findViewById(R.id.textNote);
            timestamp = (TextView) view.findViewById(R.id.textTimestamp);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(data);
            return true;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(data);
        }
    }

    public interface OnRowClicked {

        void onLongClick(Record data);

        void onClick(Record data);
    }
}
