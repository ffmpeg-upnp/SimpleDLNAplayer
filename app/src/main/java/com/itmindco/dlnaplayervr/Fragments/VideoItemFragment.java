package com.itmindco.dlnaplayervr.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itmindco.dlnaplayervr.Models.VideoListContent;
import com.itmindco.dlnaplayervr.Models.VideoListItem;
import com.itmindco.dlnaplayervr.R;
import com.itmindco.dlnaplayervr.UpnpService.ContentDirectoryBrowse;



public class VideoItemFragment extends Fragment implements ContentDirectoryBrowse.Callbacks {

    private OnListFragmentInteractionListener mListener;
    VideoItemAdapter adapter;
    ContentDirectoryBrowse contentDirectoryBrowse;


    public VideoItemFragment() {
    }

    @SuppressWarnings("unused")
    public static VideoItemFragment newInstance(int columnCount) {
        return new VideoItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videoitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new VideoItemAdapter(VideoListContent.ITEMS, mListener);
            recyclerView.setAdapter(adapter);

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        if(contentDirectoryBrowse == null) {
            contentDirectoryBrowse = new ContentDirectoryBrowse(this);
        }
        contentDirectoryBrowse.bindServiceConnection(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        contentDirectoryBrowse.unbindServiceConnection(getContext());
    }

    @UiThread
    public void UpdateList(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }});

    }

    public void findDevices(){
        contentDirectoryBrowse.refreshDevices();
    }

    public void showUpnpContent(VideoListItem item) {
        switch (item.type) {
            case DEVICE:
                if (item.device.isFullyHydrated()) {
                    contentDirectoryBrowse.showContent(item.getContentDirectory(), "0");
                }
                break;
            case DIRECTORY:
                contentDirectoryBrowse.showContent(item.service, item.getId());
                break;
        }

    }

    @Override
    public void onRefresh() {
        UpdateList();
    }

    @Override
    public void onError(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(VideoListItem item);
    }
}
