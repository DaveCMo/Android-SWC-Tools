package com.swctools.activity_modules.armoury_equipment;

import com.swctools.activity_modules.armoury_equipment.models.Armoury_Set_Item;

public interface ArmouryEquipment_Callback {

    void addEquipment(Armoury_Set_Item armoury_set_item, int position);

    void removeEquipment(Armoury_Set_Item armoury_set_item, int position);
}
