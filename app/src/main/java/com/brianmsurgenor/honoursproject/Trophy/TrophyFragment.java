package com.brianmsurgenor.honoursproject.Trophy;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brianmsurgenor.honoursproject.R;


public class TrophyFragment extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private ContentResolver mContentResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_trophy, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.trophy_recycler_view);
        final TrophyGridAdapter adapter = new TrophyGridAdapter(mContentResolver, getActivity());

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }


}
