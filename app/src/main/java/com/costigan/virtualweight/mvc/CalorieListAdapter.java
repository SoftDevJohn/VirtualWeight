package com.costigan.virtualweight.mvc;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.costigan.virtualweight.db.Calorie;
import com.costigan.virtualweight.ui.EditCalorieContextMenuListener;
//import com.example.android.roomwordssample.R;
import com.costigan.virtualweight.R;

import java.util.Collections;
import java.util.List;


public class CalorieListAdapter extends RecyclerView.Adapter<CalorieListAdapter.WordViewHolder> {
    EditCalorieContextMenuListener listener;

    public void setContextMenuListener(EditCalorieContextMenuListener listener) {
        this.listener = listener;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);

            //Added by JC
            wordItemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            String row = ((AppCompatTextView) v).getText().toString();
            String fields[] = row.split("\t\t");
            String date = fields[0];
            int calsIn = Integer.parseInt(fields[1]);
            int calsOut = Integer.parseInt(fields[2]);

            if (listener != null) {
                listener.editListRow(date, calsIn, calsOut);
            }

        }


    }

    private final LayoutInflater mInflater;
    private List<Calorie> mCalories = Collections.emptyList(); // Cached copy of words

    public CalorieListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.listener = (EditCalorieContextMenuListener) context;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Calorie current = mCalories.get(position);
        holder.wordItemView.setText(current.getWord() + "\t\t" + current.getCaloriesIn() + "\t\t" + current.getCaloriesOut());
    }

    public void setWords(List<Calorie> calories) {
        mCalories = calories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCalories.size();
    }
}


