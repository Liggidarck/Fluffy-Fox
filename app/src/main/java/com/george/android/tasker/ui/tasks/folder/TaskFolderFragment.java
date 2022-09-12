package com.george.android.tasker.ui.tasks.folder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;
import com.george.android.tasker.data.model.TaskFolder;
import com.george.android.tasker.data.viewmodel.TasksFolderViewModel;
import com.george.android.tasker.databinding.FragmentTaskFolderBinding;
import com.george.android.tasker.ui.adapters.TaskFolderAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskFolderFragment extends Fragment {

    FragmentTaskFolderBinding binding;
    TasksFolderViewModel tasksFolderViewModel;

    TaskFolderAdapter taskFolderAdapter = new TaskFolderAdapter();

    List<TaskFolder> folders = new ArrayList<>();
    public static final String TAG = "TaskFolderFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.toolbarTasksFolder.inflateMenu(R.menu.task_menu);

        tasksFolderViewModel = new ViewModelProvider(this).get(TasksFolderViewModel.class);

        initRecyclerView();

        tasksFolderViewModel.getAllFolders().observe(TaskFolderFragment.this.requireActivity(), taskFolders -> {
            folders = taskFolders;
            taskFolderAdapter.setTaskFolders(taskFolders);
        });

        taskFolderAdapter.setOnClickListener((taskFolder, position) -> {
            NavController navController = Navigation.findNavController(TaskFolderFragment.this.requireActivity(),
                    R.id.nav_host_fragment_activity_main);

            int folderId = taskFolder.getFolderId();
            String name = taskFolder.getNameFolder();

            Bundle bundle = new Bundle();
            bundle.putInt("folderId", folderId);
            bundle.putString("folderName", name);

            navController.navigate(R.id.action_navigation_task_to_tasksFragment, bundle);
        });

        taskFolderAdapter.setOnFolderClickListener((taskFolder, position) -> {
            Log.d(TAG, "id folder: " + taskFolder.getFolderId());

            Bundle bundle = new Bundle();
            bundle.putInt("folderId", taskFolder.getFolderId());
            bundle.putString("name", taskFolder.getNameFolder());
            bundle.putInt("position", taskFolder.getPosition());


            EditFolderTaskBottomSheet editFolderTaskBottomSheet = new EditFolderTaskBottomSheet();
            editFolderTaskBottomSheet.setArguments(bundle);
            editFolderTaskBottomSheet.show(getParentFragmentManager(), "EditFolderTaskBottomSheet");
        });

        binding.buttonAddTaskFolder.setOnClickListener(v -> {
            AddFolderTaskBottomSheet addFolderTaskBottomSheet = new AddFolderTaskBottomSheet();
            addFolderTaskBottomSheet.show(getParentFragmentManager(), "AddFolderTaskBottomSheet");
        });

        binding.toolbarTasksFolder.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search_task_item) {
                NavController navController = Navigation.findNavController(TaskFolderFragment.this.requireActivity(),
                        R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_task_to_navigation_task_search);
            }
            return false;
        });

        return root;
    }

    private void initRecyclerView() {
        binding.recyclerTasksFolder.setLayoutManager(new LinearLayoutManager(TaskFolderFragment.this.requireActivity()));
        binding.recyclerTasksFolder.setHasFixedSize(true);
        binding.recyclerTasksFolder.setAdapter(taskFolderAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerTasksFolder);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int position = fromPosition; position < toPosition; position++) {
                    Collections.swap(folders, position, position + 1);

                    int order1 = folders.get(position).getPosition();
                    int order2 = folders.get(position + 1).getPosition();
                    folders.get(position).setPosition(order2);
                    folders.get(position + 1).setPosition(order1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(folders, i, i - 1);

                    int order1 = folders.get(i).getPosition();
                    int order2 = folders.get(i - 1).getPosition();
                    folders.get(i).setPosition(order2);
                    folders.get(i - 1).setPosition(order1);
                }
            }
            taskFolderAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;

        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            Log.d(TAG, "clearView: start update");
            tasksFolderViewModel.updatePositions(folders);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
