package com.itmindco.dlnaplayervr.Fragments;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.google.android.libraries.mediaframework.exoplayerextensions.Video;
import com.itmindco.dlnaplayervr.R;

import java.io.IOException;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class VideoPlayerFragment extends Fragment implements SurfaceHolder.Callback,MediaController.MediaPlayerControl {
    private static final String ARG_VIDEOURL = "videoUrl";

    private String videoUrl;
    IMediaPlayer player;
    SurfaceHolder holder;
    MediaController mediaController;
    SurfaceView videoPlayerContainer;


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

        //IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        player = new AndroidMediaPlayer();
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//
//        //    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
//
////                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
//
//
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
//
//
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//
//
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
//
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        player = ijkMediaPlayer;//new AndroidMediaPlayer();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        //ijkPlayer = new AndroidMediaPlayer();
        videoPlayerContainer = (SurfaceView) view.findViewById(R.id.surfaceView);
        videoPlayerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaController.show();
            }
        });
        holder = videoPlayerContainer.getHolder();
        holder.addCallback(this);

        mediaController = new MediaController(getContext());
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(view);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void setupPlayer(PlayerType type) {
        switch (type) {
            case IJKPLAYER:
                player = new IjkMediaPlayer();

                break;
            case ANDROIDMEDIAPLAYER:
                player = new AndroidMediaPlayer();

                break;
        }
        //recreate surface holder
        videoPlayerContainer.setVisibility(View.INVISIBLE);
        videoPlayerContainer.setVisibility(View.VISIBLE);
    }

    public void PlayVideo(String videoUrl) throws IOException {
        player.reset();

        player.setDataSource(videoUrl);

        //mediaController = new MediaController(getContext());
        //mediaController.setMediaPlayer(this);
        //mediaController.setAnchorView(getView());
        //ijkPlayer.setDisplay(holder);

        player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mediaController.show();
                mp.start();
            }
        });

        player.prepareAsync();
    }
    public void createImaPlayer(Video videoListItem) {
        //if (imaPlayer != null) {
        //    imaPlayer.release();
        //}

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
        //imaPlayer.play();
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private void clearSurface(Surface surface) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(display, null);

        int[] attribList = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL10.EGL_NONE, 0,      // placeholder for recordable [@-3]
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        egl.eglChooseConfig(display, attribList, configs, configs.length, numConfigs);
        EGLConfig config = configs[0];
        EGLContext context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        });
        EGLSurface eglSurface = egl.eglCreateWindowSurface(display, config, surface,
                new int[]{
                        EGL14.EGL_NONE
                });

        egl.eglMakeCurrent(display, eglSurface, eglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        egl.eglSwapBuffers(display, eglSurface);
        egl.eglDestroySurface(display, eglSurface);
        egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroyContext(display, context);
        egl.eglTerminate(display);
    }

    public void reset() {
        if (player != null) {
            player.stop();
            player.reset();

            clearSurface(holder.getSurface());
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }
    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return (int)player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return (int)player.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    public enum PlayerType {
        MEDIAPLAYER,
        ANDROIDMEDIAPLAYER,
        IMAPLAYER,
        IJKPLAYER
    }
}
