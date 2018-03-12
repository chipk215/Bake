package com.keyeswest.bake.utilities;


import android.content.Context;
import android.content.SharedPreferences;

import com.keyeswest.bake.interfaces.IsCheckable;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WriteSharedPreferences<T extends IsCheckable> implements Runnable {

    private Context mContext;
    private String mFileName;
    private List<T> mItems;

    public WriteSharedPreferences(Context context, String fileName, List<T> items){

        mContext = context;
        mFileName = fileName;
        mItems = items;

    }

    @Override
    public void run() {
        SharedPreferences.Editor editor =
                mContext.getSharedPreferences(mFileName, MODE_PRIVATE).edit();

        for (T i : mItems){
            Boolean isChecked = i.getCheckedState();
            editor.putBoolean(i.getUniqueId(), isChecked);
        }

        editor.apply();

    }
}
