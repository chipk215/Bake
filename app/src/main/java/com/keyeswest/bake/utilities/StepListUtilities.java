package com.keyeswest.bake.utilities;


import com.keyeswest.bake.models.Step;

import java.util.List;

public class StepListUtilities {

    public static int getIndexForCorrespondingId(String id, List<Step> steps){
        for (int i=0; i< steps.size(); i++){
            if (steps.get(i).getUniqueId().equals(id)){
                return i;
            }
        }
        return -1;
    }
}
