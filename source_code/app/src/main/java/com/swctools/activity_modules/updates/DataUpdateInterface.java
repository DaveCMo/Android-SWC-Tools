package com.swctools.activity_modules.updates;

import com.swctools.util.MethodResult;

public interface DataUpdateInterface {

    void postUpdateToActivity(String msg);
    void postErrorBacktoActivity(Exception e);
    void updateCompleted(MethodResult methodResult);

}
