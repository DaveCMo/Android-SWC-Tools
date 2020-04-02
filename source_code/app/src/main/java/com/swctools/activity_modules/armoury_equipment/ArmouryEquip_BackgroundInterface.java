package com.swctools.activity_modules.armoury_equipment;

import com.swctools.util.MethodResult;

public interface ArmouryEquip_BackgroundInterface {

    void progressUpdate(String msg);

    void receiveResult(String command, MethodResult methodResult);
}
