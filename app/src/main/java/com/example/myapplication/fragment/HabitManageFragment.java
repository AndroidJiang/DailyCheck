package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.HabitDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.HabitListAdapter;
import com.example.myapplication.model.Habit;
import com.example.myapplication.utils.SPUtils;
import java.util.List;

public class HabitManageFragment extends Fragment {
    private RecyclerView recyclerView;
    private HabitListAdapter adapter;
    private List<Habit> habits;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_manage, container, false);
        initViews(view);
        loadData();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadData() {
        habits = SPUtils.getHabits(getContext());
        
        if (habits.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            adapter = new HabitListAdapter(habits, habit -> {
                Intent intent = new Intent(getActivity(), HabitDetailActivity.class);
                intent.putExtra("habit_id", habit.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}

