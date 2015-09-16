package com.robert.mathieu.ormlitetest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;
import java.util.Random;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends OrmLiteFragment {

    private final static int MAX_NUM_TO_CREATE = 8;
    private final String LOG_TAG = getClass().getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        Log.i(LOG_TAG, "creating " + getClass() + " at " + System.currentTimeMillis());
        tv.setMovementMethod(new ScrollingMovementMethod());
        doSampleDatabaseStuff("onCreate", tv);

        return view;
    }

    private void doSampleDatabaseStuff(String action, TextView tv) {
        // get our dao
        RuntimeExceptionDao<SimpleData, Integer> simpleDao = getHelper().getSimpleDataDao();
        // query for all of the data objects in the database
        List<SimpleData> list = simpleDao.queryForAll();
        // our string builder for building the content-view
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(list.size()).append(" entries in DB in ").append(action).append("()\n");

        // if we already have items in the database
        int simpleC = 1;
        for (SimpleData simple : list) {
            sb.append('#').append(simpleC).append(": ").append(simple).append('\n');
            simpleC++;
        }
        sb.append("------------------------------------------\n");
        sb.append("Deleted ids:");
        for (SimpleData simple : list) {
            simpleDao.delete(simple);
            sb.append(' ').append(simple.id);
            Log.i(LOG_TAG, "deleting simple(" + simple.id + ")");
            simpleC++;
        }
        sb.append('\n');
        sb.append("------------------------------------------\n");

        int createNum;
        do {
            createNum = new Random().nextInt(MAX_NUM_TO_CREATE) + 1;
        } while (createNum == list.size());
        sb.append("Creating ").append(createNum).append(" new entries:\n");
        for (int i = 0; i < createNum; i++) {
            // create a new simple object
            long millis = System.currentTimeMillis();
            SimpleData simple = new SimpleData(millis);
            // store it in the database
            simpleDao.create(simple);
            Log.i(LOG_TAG, "created simple(" + millis + ")");
            // output it
            sb.append('#').append(i + 1).append(": ");
            sb.append(simple).append('\n');
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        tv.setText(sb.toString());
        Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
    }
}