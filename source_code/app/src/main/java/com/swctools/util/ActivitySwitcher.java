package com.swctools.util;

import android.content.Context;
import android.content.Intent;


import com.swctools.activity_modules.about.AboutActivity;
import com.swctools.activity_modules.add_player.AddPlayerActivity;
import com.swctools.activity_modules.defence_tracker.DefenceTrackerActivity;
import com.swctools.activity_modules.EditLayoutJson.EditLayoutJsonActivity;
import com.swctools.activity_modules.gallery.ImageGalleryActivity;
import com.swctools.activity_modules.layout_detail.LayoutDetail;
import com.swctools.activity_modules.layout_manager.LayoutManager;
import com.swctools.activity_modules.logs.LogsActivity;
import com.swctools.activity_modules.main.MainActivity;
import com.swctools.activity_modules.tags_types.ManageTypeTagActivity;
import com.swctools.activity_modules.multi_image_picker.MultiImagePicker;
import com.swctools.activity_modules.player.PlayerActivity;
import com.swctools.activity_modules.player_config.PlayerConfig;
import com.swctools.activity_modules.ReleaseNotesActivity;
import com.swctools.activity_modules.save_layout.SaveLayout_Activity;
import com.swctools.activity_modules.say_thanks.SayThanksActivity;
import com.swctools.activity_modules.config_screens.SettingsActivity;
import com.swctools.activity_modules.war_room.WarRoomActivity;
import com.swctools.activity_modules.war_sign_up.WarSignup;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.enums.DatabaseMethods;
import com.swctools.common.enums.ScreenCommands.SaveLayoutInterface;
import com.swctools.layouts.models.LayoutRecord;

public class ActivitySwitcher {
    private static final String TAG = "ActivitySwitcher";
    //Utility class to manage what things we need to load an activity to keep it consistent.
    //Just this one for now.. will probably have others ¯\_(ツ)_/¯




//    private static boolean checkBanned(Context mContext) {
//        boolean banned = false;
//        getCred();
//        String u = mContext.getResources().getString(R.string.spfbu);
//        String p = mContext.getResources().getString(R.string.spfbp);
//        byte[] decodeU = Base64.decode(u, Base64.DEFAULT);
//        byte[] decodeP = Base64.decode(p, Base64.DEFAULT);
//        String val2 = new String(decodeU);
//        String val3 = new String(decodeP);
//        mAuth = FirebaseAuth.getInstance();
//        final AppConfig appConfig = new AppConfig(mContext);
//        mAuth.signInWithEmailAndPassword(val2, val3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    String userId = user.getUid();
//                    myRef = mFirebaseDatabase.getReference();
//                    final String uid = appConfig.getUniqueDeviceID();
//
//                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                    rootRef.child(uid);
//                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot snapshot) {
//                            if (snapshot.getValue() == null) {
//                                // The child doesn't exist
//                            } else {
//                                banned = true;
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    String l = myRef.child(userId).child(uid).getKey();
//                }
//            }
//        });
//        return false;
//
//    }

    public static void launchSaveLayoutActivity_Save(String layoutJSON, String playerId, String playerFaction, SaveLayoutInterface screenMode, Context context) {
        Intent intent = new Intent(context, SaveLayout_Activity.class);
        intent.putExtra(BundleKeys.ACTIVITY_MODE.toString(), screenMode.toString());
        intent.putExtra(BundleKeys.LAYOUT_JSON_STRING.toString(), layoutJSON);
        intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);
        intent.putExtra(BundleKeys.PLAYER_FACTION.toString(), playerFaction);
        intent.putExtra(BundleKeys.PREV_SCREEN.toString(), playerFaction);

      
        context.startActivity(intent);
    }


    public static void launchLayoutDetails(LayoutRecord layoutRecord, Context context) {
        Intent intent = new Intent(context, LayoutDetail.class);
        intent.putExtra(BundleKeys.LAYOUT_ID.toString(), layoutRecord.getLayoutId());
        context.startActivity(intent);
    }

    public static void launchPlayerConfig(String playerId, Context context) {
        Intent intent = new Intent(context, PlayerConfig.class);
        intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);
        context.startActivity(intent);
    }

    public static void launchWarStatus(String playerId, String warId, Context context) {
        Intent intent = new Intent(context, WarRoomActivity.class);
        intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);
        intent.putExtra(BundleKeys.WAR_ID.toString(), warId);
        context.startActivity(intent);
    }

    public static void launchEditLayoutJson(int layoutId, int versionId, Context context) {
        Intent intent = new Intent(context, EditLayoutJsonActivity.class);
        intent.putExtra(BundleKeys.LAYOUT_ID.toString(), layoutId);
        intent.putExtra(BundleKeys.LAYOUT_VERSION_ID.toString(), versionId);
        context.startActivity(intent);
    }

    public static void launchPlayerDetails(String playerId, Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(BundleKeys.PLAYER_ID.toString(), playerId);
        context.startActivity(intent);
    }

    public static void launchPickImages(int limit, String refCol, int ref, String table, String tableImageColumn, String lblFld, DatabaseMethods databaseMethods, Context context) {
        Intent intent = new Intent(context, MultiImagePicker.class);
        intent.putExtra(MultiImagePicker.IMAGE_LIMIT, limit);
        intent.putExtra(MultiImagePicker.SOURCE_TABLE_ID, ref);
        intent.putExtra(MultiImagePicker.SOURCE_TABLE, table);
        intent.putExtra(MultiImagePicker.SOURCE_TABLE_FIELD, tableImageColumn);
        intent.putExtra(MultiImagePicker.DATABASE_METHOD, databaseMethods.name());
        intent.putExtra(MultiImagePicker.SOURCE_TABLE_ID_COL, refCol);
        intent.putExtra(MultiImagePicker.SOURCE_TABLE_LABEL, lblFld);
        context.startActivity(intent);
    }

    public static void launchMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void launchLayoutManager(Context context) {
        Intent intent = new Intent(context, LayoutManager.class);
        context.startActivity(intent);
    }

    public static void launchSettings(Context context) {

        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);

    }

    public static void launchLogs(Context context) {
        Intent aboutIntent = new Intent(context, LogsActivity.class);
        context.startActivity(aboutIntent);
    }

    public static void launchSayThanks(Context context) {
        Intent intent = new Intent(context, SayThanksActivity.class);
        context.startActivity(intent);
    }

    public static void launchDefenceTrackerSettings(Context context) {
        Intent intent = new Intent(context, DefenceTrackerActivity.class);
        context.startActivity(intent);
    }

    public static void launchManageTags(Context context) {
        Intent intent = new Intent(context, ManageTypeTagActivity.class);
        context.startActivity(intent);
    }

    public static void launchWarSignUp(Context context) {
        Intent intent = new Intent(context, WarSignup.class);
        context.startActivity(intent);
    }

    public static void launchWarDash(Context context) {
        Intent intent = new Intent(context, WarRoomActivity.class);
        context.startActivity(intent);
    }

    public static void launchAddPlayer(Context context) {
        Intent intent = new Intent(context, AddPlayerActivity.class);
        context.startActivity(intent);
    }

    public static void launchLayoutImageGallery(int selected, int layoutId, Context context) {
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra(ImageGalleryActivity.LAYOUT_ID, layoutId);
        intent.putExtra(ImageGalleryActivity.POSITION_ID, selected);
        context.startActivity(intent);
    }

    public static void launchAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);

    }

    public static void launchReleaseNotes(Context context) {
        Intent intent = new Intent(context, ReleaseNotesActivity.class);
        context.startActivity(intent);

    }
}
