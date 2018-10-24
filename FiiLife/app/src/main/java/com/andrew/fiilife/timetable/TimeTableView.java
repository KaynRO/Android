package com.andrew.fiilife.timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andrew.fiilife.R;
import com.andrew.fiilife.data.Data;

/**
 * Created by Razvan Gabriel / Dizz on 08-Aug-16.
 */
public class TimeTableView extends RecyclerView.ViewHolder {

    private Context context;
    private TextView name;
    private TextView day;
    private TextView description;




    public TimeTableView(View view, Context context) {
        super(view);
        this.context = context;
        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);
        day = (TextView) view.findViewById(R.id.day);
    }



    public void bindModel(Data.TableItem item){

        name.setText(item.name);
        String dayS;
        switch (item.day){
            case 1 : dayS = "Monday"; break;
            case 2 : dayS = "Tuesday"; break;
            case 3 : dayS = "Wednesday"; break;
            case 4 : dayS = "Thursday"; break;
            case 5 : dayS = "Friday"; break;
            default: dayS = "Monday"; break;
        }

        day.setText(dayS);


        description.setText(item.description);

    }

}