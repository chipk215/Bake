package com.keyeswest.bake.tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.keyeswest.bake.interfaces.HasUniqueId;

import java.util.Hashtable;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ReadCheckboxStates<T extends HasUniqueId> extends
        AsyncTask<List<T>, Void, Hashtable<String, Boolean>> {

    private ResultsCallback mCallback;
    private String mFileName;
    private Context mContext;

    private Hashtable<String, Boolean> mCheckboxState;

    public interface ResultsCallback{
        void CheckboxStates(Hashtable<String, Boolean> checkboxStates);
    }

    public ReadCheckboxStates(Context context, String fileName, ResultsCallback callback){
        mCallback = callback;
        mFileName = fileName;
        mContext = context;
    }

    @Override
    protected Hashtable<String, Boolean> doInBackground(List<T>... lists) {

        if ((lists == null) || (lists[0] == null)) {
            return null;
        }

        mCheckboxState = new Hashtable<>();
        for (T i : lists[0]){
            String hashValue = i.getUniqueId();
            Boolean isChecked = mContext.getSharedPreferences(mFileName, MODE_PRIVATE)
                    .getBoolean(hashValue, false);
            mCheckboxState.put(hashValue, isChecked);
        }

        return mCheckboxState;
    }

    @Override
    protected void onPostExecute(Hashtable<String, Boolean> checkboxStates) {
        mCallback.CheckboxStates(checkboxStates);
    }
}
