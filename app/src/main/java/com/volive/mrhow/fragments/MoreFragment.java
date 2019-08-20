package com.volive.mrhow.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.volive.mrhow.R;
import com.volive.mrhow.activities.ViewDetailsActivity;
import com.volive.mrhow.adapters.MoreCommentsAdapter;
import com.volive.mrhow.adapters.StudentProjectsAdapter;
import com.volive.mrhow.models.MoreCommentsModels;
import com.volive.mrhow.models.StudentProjectModels;

import java.util.ArrayList;


public class MoreFragment extends Fragment {
    RecyclerView projects_recyclerview, comments_recyclerview;
    TextView text_seeall, text_seemore;

    ArrayList<StudentProjectModels> studentProject = new ArrayList<>();
    ArrayList<MoreCommentsModels> moreComments = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        initializeUI(view);
        initializeValues();

        studentProject = ViewDetailsActivity.studentProjectModels;
        moreComments = ViewDetailsActivity.moreCommentsModels;

        if(moreComments.size()>1){
            text_seemore.setVisibility(View.VISIBLE);
        }else {
            text_seemore.setVisibility(View.GONE);
        }


        if (studentProject.size() > 0) {
            StudentProjectsAdapter studentProjectsAdapter = new StudentProjectsAdapter(getActivity(), studentProject);
            projects_recyclerview.setAdapter(studentProjectsAdapter);
            studentProjectsAdapter.notifyDataSetChanged();

        }

        if (moreComments.size() > 0) {
            MoreCommentsAdapter moreCommentsAdapter = new MoreCommentsAdapter(getActivity(), moreComments,"withoutsee");
            comments_recyclerview.setAdapter(moreCommentsAdapter);
            moreCommentsAdapter.notifyDataSetChanged();
        }


        return view;
    }

    private void initializeValues() {
        text_seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreComments.size() > 0) {
                    text_seemore.setVisibility(View.GONE);
                    MoreCommentsAdapter moreCommentsAdapter = new MoreCommentsAdapter(getActivity(), moreComments, "withsee");
                    comments_recyclerview.setAdapter(moreCommentsAdapter);
                    moreCommentsAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void initializeUI(View view) {
        text_seeall = view.findViewById(R.id.text_seeall);
        text_seemore = view.findViewById(R.id.text_seemore);
        projects_recyclerview = view.findViewById(R.id.projects_recyclerview);
        comments_recyclerview = view.findViewById(R.id.comments_recyclerview);


        projects_recyclerview.setHasFixedSize(true);
        projects_recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));

        comments_recyclerview.setHasFixedSize(true);
        comments_recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));


    }

}
