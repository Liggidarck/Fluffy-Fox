package com.george.android.voltage_online.ui.tasks.folder;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.FragmentTaskFolderBinding;
import com.george.android.voltage_online.ui.adapters.TaskFolderAdapter;
import com.george.android.voltage_online.viewmodel.FolderViewModel;

public class FolderFragment extends Fragment {

    private FragmentTaskFolderBinding binding;
    private FolderViewModel folderViewModel;
    private final TaskFolderAdapter taskFolderAdapter = new TaskFolderAdapter();

    public static final String TAG = "TaskFolderFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskFolderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.toolbarTasksFolder.inflateMenu(R.menu.task_menu);

        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        initRecyclerView();

//        folderViewModel
//                .getAllFolder()
//                .observe(FolderFragment.this.requireActivity(),
//                        taskFolderAdapter::setTaskFolders);

        taskFolderAdapter.setOnClickListener((folder, position) -> {
            NavController navController = Navigation.findNavController(FolderFragment.this.requireActivity(),
                    R.id.nav_host_fragment_activity_main);

            int folderId = folder.getId();
            String name = folder.getNameFolder();

            Bundle bundle = new Bundle();
            bundle.putInt("folderId", folderId);
            bundle.putString("folderName", name);

            navController.navigate(R.id.action_navigation_task_to_tasksFragment, bundle);
        });

        taskFolderAdapter.setOnFolderClickListener((folder, position) -> {
            Log.d(TAG, "id folder: " + folder.getId());

            Bundle bundle = new Bundle();
            bundle.putInt("folderId", folder.getId());
            bundle.putString("name", folder.getNameFolder());

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
                NavController navController = Navigation.findNavController(FolderFragment.this.requireActivity(),
                        R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_task_to_navigation_task_search);
            }
            return false;
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        folderViewModel
                .getAllFolder()
                .observe(FolderFragment.this.requireActivity(),
                        taskFolderAdapter::setTaskFolders);

    }

    private void initRecyclerView() {
        binding.recyclerTasksFolder.setLayoutManager(new LinearLayoutManager(FolderFragment.this.requireActivity()));
        binding.recyclerTasksFolder.setHasFixedSize(true);
        binding.recyclerTasksFolder.setAdapter(taskFolderAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
