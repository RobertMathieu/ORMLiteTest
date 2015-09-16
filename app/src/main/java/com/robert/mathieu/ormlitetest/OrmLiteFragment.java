package com.robert.mathieu.ormlitetest;

import android.support.v4.app.Fragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Mathieu on 15/09/2015.
 */
public class OrmLiteFragment extends Fragment {

    private DatabaseHelper databaseHelper = null;

    protected DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}