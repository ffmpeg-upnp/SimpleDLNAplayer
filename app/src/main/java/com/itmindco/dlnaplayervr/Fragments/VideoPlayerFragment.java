package com.itmindco.dlnaplayervr.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.libraries.mediaframework.exoplayerextensions.Video;
import com.itmindco.dlnaplayervr.R;
import com.itmindco.dlnaplayervr.adplayer.ImaPlayer;

import java.io.IOException;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoPlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPlayerFragment extends Fragment implements SurfaceHolder.Callback {
    private static final String ARG_VIDEOURL = "videoUrl";

    private String videoUrl;
    private ImaPlayer imaPlayer;
    AndroidMediaPlayer ijkPlayer;
    SurfaceHolder holder;

    private OnFragmentInteractionListener mListener;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    public static VideoPlayerFragment newInstance(String videoUrl) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEOURL, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUrl = getArguments().getString(ARG_VIDEOURL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        ijkPlayer = new AndroidMediaPlayer();
        SurfaceView videoPlayerContainer = (SurfaceView) view.findViewById(R.id.surfaceView);

        holder = videoPlayerContainer.getHolder();
        holder.addCallback(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void createImaPlayer(Video videoListItem) {
        if (imaPlayer != null) {
            imaPlayer.release();
        }

        // If there was previously a video player in the container, remove it.
        //videoPlayerContainer.removeAllViews();

        //String adTagUrl = videoListItem.adUrl;
        //String videoTitle = videoListItem.title;

//        imaPlayer = new ImaPlayer(this,
//                videoPlayerContainer,
//                videoListItem,
//                "my",
//                null);
        //imaPlayer.setFullscreenCallback(this);

        //Resources res = getResources();

        // Customize the UI of the video player.

        // Set a logo (an Android icon will be displayed in the top left)
        //Drawable logo = res.getDrawable(R.drawable.gmf_icon);
        //imaPlayer.setLogoImage(logo);



        // Now that the player is set up, let's start playing.
        imaPlayer.play();
    }

    public void createIjkPlayer(Video videoListItem) throws IOException {
        if (ijkPlayer != null) {
            //ijkPlayer.release();
        }

        //videoPlayerContainer.removeAllViews();


        //Surface surface = videoPlayerContainer.getHolder().getSurface();
        //ijkPlayer.setSurface(surface);

        //holder.setFixedSize(100,100);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        ijkPlayer.setDataSource(videoListItem.getUrl());
        //ijkPlayer.setDisplay(holder);
        ijkPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.start();
            }
        });

        ijkPlayer.prepareAsync();

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("","");
        ijkPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public interface OnFragmentInteractionListener {
         void onFragmentInteraction(Uri uri);
    }

}
