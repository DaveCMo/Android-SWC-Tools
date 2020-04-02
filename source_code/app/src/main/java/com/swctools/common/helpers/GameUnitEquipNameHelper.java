package com.swctools.common.helpers;

public class GameUnitEquipNameHelper {
    private static final String TAG = "GameUnitEquipNameHelper";
    private static final String lkpCol = "lkup";

//    private static GameUnitResult gameUnitResult(String lookupStr, String faction, String tableName, Context context) {
//        Cursor cursor;
//        GameUnitLookup gameUnitLookup = new GameUnitLookup(tableName, context);
//        GameUnitResult gameUnitResult = new GameUnitResult(gameUnitLookup.getValue(lookupStr), lookupStr);
//        return  gameUnitResult;
////        String[] columns = {DatabaseContracts.GameUnitTable.CAPACITY,
////                DatabaseContracts.GameUnitTable.GAME_NAME,
////                DatabaseContracts.GameUnitTable.LEVEL,
////                DatabaseContracts.GameUnitTable.UI_NAME,
////                DatabaseContracts.GameUnitTable.FACTION,
////                DatabaseContracts.GameUnitTable.GAME_NAME + "||" + DatabaseContracts.GameUnitTable.LEVEL + " AS " + lkpCol};
////
////        String selectionStr = lkpCol + " LIKE ? ";// AND " + DatabaseContracts.GameUnitTable.FACTION + " = ?";
////        String[] selectionArgs = {lookupStr};//, faction};
////        cursor = DBSQLiteHelper.queryDB(tableName, columns, selectionStr, selectionArgs, context);
////        String ui = lookupStr;
////        int level = 0;
////        int cap = 0;
////        try {
////            while (cursor.moveToNext()) {
////                ui = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.UI_NAME));
////                level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.LEVEL));
////                cap = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContracts.GameUnitTable.CAPACITY));
////                return new GameUnitResult(ui, lookupStr, level, cap);
////            }
////            return new GameUnitResult(lookupStr, lookupStr, 0, cap);
////        } catch (Exception e) {
////            e.printStackTrace();
////            return new GameUnitResult(lookupStr, lookupStr, 0, cap);
////        } finally {
////            cursor.close();
////        }
//    }


    public static class GameUnitResult {
        public String uiName;
        public int level;
        public String gameName;
        public int capacity;

        public GameUnitResult(String uiName, String gameName, int level, int capacity) {
            this.uiName = uiName;
            this.gameName = gameName;
            this.level = level;
            this.capacity = capacity;
        }
        public GameUnitResult(String parseString, String lookUp){
            String[] items = parseString.split("\\|");
            try {
                this.uiName = items[0];
                this.gameName = lookUp;
                this.level = Integer.parseInt(items[1]);
                this.capacity = Integer.parseInt(items[2]);
            } catch (NumberFormatException e) {
                this.uiName = lookUp;
                this.gameName = lookUp;
                this.level = 0;
                this.capacity = 0;
                e.printStackTrace();
            }
        }
    }




}
