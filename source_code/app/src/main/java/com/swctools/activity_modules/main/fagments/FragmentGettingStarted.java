package com.swctools.activity_modules.main.fagments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.swctools.R;
import com.swctools.common.helpers.YouTubeHelper;
import com.swctools.common.view_adaptors.recycler_adaptors.RecyclerAdaptor_BulletPoint;

import java.util.ArrayList;
import java.util.List;

public class FragmentGettingStarted extends Fragment {
    private static final String YoutubeDeveloperKey = "xyz";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private RecyclerView quickStartRecycler;
    private Context mContext;
    private YouTubePlayerView youtubeVideoView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private YouTubePlayer YPlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private FragmentTransaction transaction;
    private ScrollView gettingStartedScroll;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        YPlayer = null;
        youTubePlayerFragment = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_getting_started, container, false);
        gettingStartedScroll = (ScrollView) view.findViewById(R.id.gettingStartedScroll);
        quickStartRecycler = (RecyclerView) view.findViewById(R.id.quickStartRecycler);
        if (savedInstanceState == null) {
            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        } else {
            youTubePlayerFragment = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
        }


        youTubePlayerFragment.initialize(YouTubeHelper.API_KEY, new OnInitializedListener() {

            @Override
            public void onInitializationSuccess(Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);
                    YPlayer.cueVideo("8rPACDDEWhg");
                    YPlayer.setShowFullscreenButton(true);
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                }
            }

            @Override
            public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub

            }
        });

        List<String> items = new ArrayList<>();
        items.add(getResources().getString(R.string.getting_started_quickstart_1));
        items.add(getResources().getString(R.string.getting_started_quickstart_2));
        items.add(getResources().getString(R.string.getting_started_quickstart_3));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        quickStartRecycler.setLayoutManager(mLayoutManager);
        quickStartRecycler.setItemAnimator(new DefaultItemAnimator());
        RecyclerAdaptor_BulletPoint recyclerAdaptorBulletPoint = new RecyclerAdaptor_BulletPoint(items, mContext);
        quickStartRecycler.setAdapter(recyclerAdaptorBulletPoint);
        recyclerAdaptorBulletPoint.notifyDataSetChanged();


        return view;
    }


    class YouTubeInitListener implements YouTubePlayer.OnInitializedListener {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo("3LiubyYpEUk");
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }
    }

    class VideoClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            youtubeVideoView.initialize(YouTubeHelper.API_KEY, onInitializedListener);
        }
    }
}
