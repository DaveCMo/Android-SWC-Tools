package com.swctools.activity_modules.war_sign_up;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swctools.R;
import com.swctools.base.BaseActivity;
import com.swctools.common.enums.BundleKeys;
import com.swctools.common.helpers.ImageHelpers;
import com.swctools.activity_modules.war_sign_up.models.GuildGetPublicResultItem;
import com.swctools.activity_modules.war_sign_up.models.GuildMember;
import com.swctools.activity_modules.war_sign_up.models.GuildSearchResultItem;
import com.swctools.swc_server_interactions.fragments.SWC_WarSignup_Tasks_Fragment;
import com.swctools.swc_server_interactions.results.SWCGetPublicGuildResponseData;
import com.swctools.util.MethodResult;
import com.swctools.util.StringUtil;
import com.swctools.util.Utils;
import com.swctools.activity_modules.war_sign_up.fragments.Fragment_WarDetails;
import com.swctools.activity_modules.war_sign_up.fragments.Fragment_WarHitList;
import com.swctools.activity_modules.war_sign_up.fragments.Fragment_WarMembers;
import com.swctools.activity_modules.war_sign_up.fragments.Fragment_WarSearch;
import com.swctools.activity_modules.war_sign_up.interfaces.FragmentWarDetailsInterface;
import com.swctools.activity_modules.war_sign_up.interfaces.FragmentWarSearchInterface;
import com.swctools.activity_modules.war_sign_up.interfaces.GuildSearchResultRowInterface;
import com.swctools.activity_modules.war_sign_up.interfaces.HitListCallBack;
import com.swctools.activity_modules.war_sign_up.interfaces.WarMembersInterface;
import com.swctools.activity_modules.war_sign_up.interfaces.WarMembersListCallBack;
import com.swctools.activity_modules.war_sign_up.interfaces.WarSignupInterface;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WarSignup extends BaseActivity
        implements FragmentWarSearchInterface,
        GuildSearchResultRowInterface,
        FragmentWarDetailsInterface,
        WarMembersListCallBack,
        WarMembersInterface,
        HitListCallBack,
        WarSignupInterface {

    private static final String TAG = "WarSignup";

    private static final String WAR_SEARCH = "WAR_SEARCH";
    private static final String WAR_DETAILS = "WAR_DETAILS";
    private static final String HITLIST = "HITLIST";
    private static final String MEMBERSLIST = "MEMBERSLIST";

    private static final String PRINTMEMBER = "%1$s (%2$s: %3$s) -\n";
    private static final String WARVS = "WAR! vs. %1s\n\n";


    private String startT;
    private List<String> selectedPlanets;

    private ImageView guildFaction_Img;
    private TextView guildName, guildMembers;
    private FrameLayout frameLayout;
    private Fragment_WarSearch fragment_warSearch;
    private boolean warBound;

    private String searchTerm;
    private boolean mBound;
    private int downloadCommand;
    private String progressMsg;

    private FragmentManager fm;
    private GuildSearchResultItem selectedGuild;
    private String guildId;
    private String whichFrag;
    private HashMap<String, GuildMember> manualMemberList;
    private StringBuilder hitList;
    private ArrayList<GuildMember> warParty;


     private SWC_WarSignup_Tasks_Fragment swc_warSignup_tasks_fragment;

    @Override
    public void shareText(String s) {
        Utils.shareText(s, this);
    }

    @Override
    public void copyText(String s) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("SWC TOOLS HITLIST", s);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text copied!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.warSignupToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frameLayout = (FrameLayout) findViewById(R.id.war_signupFrame);
        guildFaction_Img = (ImageView) findViewById(R.id.guildFaction_Img);
        guildMembers = (TextView) findViewById(R.id.guildMembers);
        guildName = (TextView) findViewById(R.id.guildName);
        setTitle("WAR SIGN UP");
        progress_overlay_bar = findViewById(R.id.progress_overlay_bar);
        progress_overlay_container = findViewById(R.id.progress_overlay_container_include);
        progress_overlay_message = findViewById(R.id.progress_overlay_message);
        //Create objects:

        swc_warSignup_tasks_fragment = SWC_WarSignup_Tasks_Fragment.getInstance(getSupportFragmentManager());

        if (savedInstanceState != null) {
            whichFrag = savedInstanceState.getString(InstanceKeys.WHICH_FRAG.toString());
        } else {
            fragment_warSearch = new Fragment_WarSearch();
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.war_signupFrame, fragment_warSearch, WAR_SEARCH);
            whichFrag = WAR_SEARCH;
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {

                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }

    private void setFragment(String tag) {
        whichFrag = tag;
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment == null) {
            if (whichFrag.equalsIgnoreCase(WAR_DETAILS)) {
                fragment = new Fragment_WarDetails();
            } else if (whichFrag.equalsIgnoreCase(WAR_SEARCH)) {
                fragment = new Fragment_WarSearch();
            } else if (whichFrag.equalsIgnoreCase(HITLIST)) {
                Bundle args = new Bundle();
                args.putString(BundleKeys.HIT_LIST_RESULT.toString(), hitList.toString());
                fragment = new Fragment_WarHitList();
                fragment.setArguments(args);
            } else if (whichFrag.equalsIgnoreCase(MEMBERSLIST)) {
                fragment = new Fragment_WarMembers();
            }
        }
        ft.replace(R.id.war_signupFrame, fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(InstanceKeys.WHICH_FRAG.toString(), whichFrag);
    }

    @Override
    public void getList(String startT, List<String> p) {
        this.startT = startT;
        this.selectedPlanets = new ArrayList<>();
        this.selectedPlanets.addAll(p);
        publishProgress("Getting players in war...");
        swc_warSignup_tasks_fragment.getGuild(this.guildId, getApplicationContext());
    }

    @Override
    public void searchSquad(String searchTerm) {
        if (StringUtil.isStringNotNull(searchTerm)) {
            if (searchTerm.length() >= 3) {
                resetSelectedGuild();
                this.searchTerm = searchTerm;
                if (StringUtil.stringIsUUID(searchTerm)) {
                    this.guildId = searchTerm;
                    publishProgress("Searching for Squad ID " + searchTerm + "...");
                    swc_warSignup_tasks_fragment.getGuild(searchTerm, getApplicationContext());
                } else {
                    publishProgress("Searching for Squad starting " + searchTerm + "...");
                    swc_warSignup_tasks_fragment.searchForSquad(searchTerm, getApplicationContext());
                }
            } else {
                Toast.makeText(this, "Must enter a search string equal to or greather than 3 characters.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Enter in a value to search!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void publishProgress(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publishProgress(msg, 0, 0);
            }
        });
    }


    public void resetSelectedGuild() {
        guildName.setText("");
        guildFaction_Img.setImageDrawable(null);
        guildMembers.setText(null);
        selectedGuild = null;
    }

    @Override
    public void guildSelected(GuildSearchResultItem guildSearchResultItem) {
        this.selectedGuild = guildSearchResultItem;
        this.guildId = guildSearchResultItem.guildId;
        guildName.setText(guildSearchResultItem.guildName);
        guildFaction_Img.setImageDrawable(ImageHelpers.factionIcon(guildSearchResultItem.faction, this));
        guildMembers.setText(String.valueOf(guildSearchResultItem.members));
        setFragment(WAR_DETAILS);


    }


    private void buildHitList(MethodResult methodResult) {
        try {

            SWCGetPublicGuildResponseData guildResponseData = new SWCGetPublicGuildResponseData(methodResult.getMessage());


            hitList = new StringBuilder();
            hitList.delete(0, hitList.length());
            String squadName = StringUtil.htmlRemovedGameName(URLDecoder.decode(guildResponseData.getResult().getString("name")));
            try {
                String warId = guildResponseData.getResult().getString("currentWarId");
                try {
                    //Build Headers...
                    hitList.append(String.format(WARVS, squadName));
                    hitList.append(startT);
                    hitList.append("OUTPOSTS:\n");
                    hitList.append("----------\n");
                    for (String s : selectedPlanets) {
                        hitList.append(s + "\n");
                    }
                    hitList.append("\n");
                    hitList.append("BASES (HQ - Strength) \n");
                    hitList.append("-------------------\n");
                    warParty = new ArrayList<>();
                    warParty.clear();
                    for (int i = 0; i < guildResponseData.getMembers().size(); i++) {

                        GuildMember guildMember = new GuildMember(guildResponseData.getMembers().getJsonObject(i));
                        if (guildMember.warParty == 1) {
                            warParty.add(guildMember);
                        }
                    }
                    if (warParty.size() > 0) {
                        sortAndFinaliseLIst();
                    } else {
                        Fragment_WarMembers fragment_warMembers = new Fragment_WarMembers();
                        Bundle args = new Bundle();
                        args.putString(BundleKeys.HIT_LIST_GUILD.toString(), guildResponseData.resultData.toString());
                        fragment_warMembers.setArguments(args);
                        setFragment(MEMBERSLIST);
                    }
                } catch (Exception e) {
                }
            } catch (Exception e) {
                Toast.makeText(this, "Squad is not in a war!!", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortAndFinaliseLIst() {
        Collections.sort(warParty, new Comparator<GuildMember>() {
            @Override
            public int compare(GuildMember m1, GuildMember m2) {
                return m2.xp - m1.xp;
            }
        });
        for (GuildMember m : warParty) {
            hitList.append(String.format(PRINTMEMBER, m.memberName, m.hqLevel, m.xp));
        }
        Fragment_WarHitList fragment_warHitList = new Fragment_WarHitList();
        Bundle args = new Bundle();
        fragment_warHitList.setArguments(args);
        args.putString(BundleKeys.HIT_LIST_RESULT.toString(), hitList.toString());
        setFragment(HITLIST);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }


    @Override
    public void receiveGuildData(MethodResult result) {
        if (result.success) {
            SWCGetPublicGuildResponseData guildResponseData = new SWCGetPublicGuildResponseData(result.getMessage());
            guildSelected(new GuildGetPublicResultItem(guildResponseData.getResult()));
            buildHitList(result);
        } else {
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
        }
        finishDownloading();

    }

    @Override
    public void finishDownloading() {
        super.finishDownloading();

    }

    @Override
    public void receiveSearchResults(MethodResult result) {
        if (result.success) {
            try {
                Fragment_WarSearch fragment_warSearch = (Fragment_WarSearch) fm.findFragmentByTag(WAR_SEARCH);
                fragment_warSearch.setGuild_results_recycler(result.getMessage());
            } catch (Exception e) {
                Toast.makeText(this, "Error parsing search results into a list: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
        }
        finishDownloading();

    }

    @Override
    public void memberChkChanged(GuildMember guildMember, boolean b) {
        if (manualMemberList == null) {
            manualMemberList = new HashMap<>();
        }
        if (b) {
            manualMemberList.put(guildMember.playerId, guildMember);
        } else {
            manualMemberList.remove(guildMember.playerId);
        }
        Fragment_WarMembers warMembers = (Fragment_WarMembers) fm.findFragmentByTag(MEMBERSLIST);
        warMembers.setNumSelected(manualMemberList.size());
    }

    @Override
    public void completeList() {
        if (manualMemberList != null) {
            if (manualMemberList.size() == 15) {
                for (Map.Entry<String, GuildMember> entry : manualMemberList.entrySet()) {
                    warParty.add(entry.getValue());
                }
                sortAndFinaliseLIst();

            } else {
                Toast.makeText(this, "Select 15 members!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Select 15 members!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void playerServiceResult(String command, MethodResult methodResult) {

    }

    private enum InstanceKeys {
        WHICH_FRAG;
    }

    public void handleListUpdate(String activity_command, boolean success) {

    }
}
