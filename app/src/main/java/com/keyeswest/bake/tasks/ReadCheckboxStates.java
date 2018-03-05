package com.keyeswest.bake.tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.keyeswest.bake.interfaces.IsCheckable;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ReadCheckboxStates<T extends IsCheckable> extends
        AsyncTask<List<T>, Void, List<T>> {

    private ResultsCallback mCallback;
    private String mFileName;
    private Context mContext;

    public interface ResultsCallback<T>{
        void CheckboxStates(List<T> updatedItems);
    }

    public ReadCheckboxStates(Context context, String fileName, ResultsCallback callback){
        mCallback = callback;
        mFileName = fileName;
        mContext = context;
    }

    @Override
    protected List<T> doInBackground(List<T>... lists) {

        if ((lists == null) || (lists[0] == null)) {
            return null;
        }


        for (T i : lists[0]){
            String hashValue = i.getUniqueId();
            Boolean isChecked = mContext.getSharedPreferences(mFileName, MODE_PRIVATE)
                    .getBoolean(hashValue, false);
            i.setCheckedState(isChecked);
        }

        return lists[0];
    }

    @Override
    protected void onPostExecute(List<T> updatedList) {
        mCallback.CheckboxStates(updatedList);
    }
}
