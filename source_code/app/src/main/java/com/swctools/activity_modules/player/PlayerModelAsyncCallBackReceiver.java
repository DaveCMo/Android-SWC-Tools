package com.swctools.activity_modules.player;

import com.swctools.common.models.player_models.PlayerModel;

public interface PlayerModelAsyncCallBackReceiver {
    void receivePlayerModel(PlayerModel playerModel);
    void receiveBattleLog();
    void receiveFavourites();
}
