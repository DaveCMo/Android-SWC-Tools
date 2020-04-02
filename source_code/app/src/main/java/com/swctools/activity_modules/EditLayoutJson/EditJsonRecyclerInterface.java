package com.swctools.activity_modules.EditLayoutJson;

public interface EditJsonRecyclerInterface {
    void scrollToBottom();

    void saveRow(int id, String key, String UID, int x, int y);

    void removeRow(int id);

    void showMessageFromRecycler(String msg);

    void saveLayout();
}