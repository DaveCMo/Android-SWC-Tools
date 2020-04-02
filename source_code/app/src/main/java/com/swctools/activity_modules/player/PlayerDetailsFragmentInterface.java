package com.swctools.activity_modules.player;

public interface PlayerDetailsFragmentInterface {
    interface Messages {
        String MESSAGE = "MESSAGE";
    }

    interface KEYS {
        String VISITOR_MESSAGE_DATA = "VISITOR_MESSAGE_DATA";
    }

    interface COMMANDS {
        String REFRESH_ME = "REFRESH_DATA";
    }


    void sendRefreshCommand();
    void finishedRendering();

//    void getPlayerModel(boolean getPlayer, boolean getBattles, boolean getFav);


}
