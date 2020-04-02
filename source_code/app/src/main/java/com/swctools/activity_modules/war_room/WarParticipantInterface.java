package com.swctools.activity_modules.war_room;

import com.swctools.activity_modules.war_room.processing_models.WarRoomData_WarParticipant;
import com.swctools.common.models.player_models.TacticalCapItem;

import java.util.List;

public interface WarParticipantInterface {
    void showDonatedTroops();

    void showLastDefence(String playerId, String guildId);

    void requestSC(String playerId);
}
