package com.example.projectfit.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectfit.Models.User;
import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Utils.GsonProvider;
import com.example.projectfit.Utils.WorkoutAdapterForMyplan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class PlanFragment extends Fragment {

    private Button[] dayButtons;
    private Button selectedDayButton;
    private ProgressBar[] progressBars;
    private LinearLayout[] trainingLayouts;
    private int[] progressStatuses = {0, 0, 0}; // Track progress for each workout
    private LinearLayout editPlanButton;
    private TextView editPlanText;
    private boolean isEditModeEnabled = false;
    private ImageView trashIcon;
    private boolean isFirstTime = true;
    private ImageButton addWorkoutButton;
    private ListView workoutListView;
    private Button cancelButton;
    private boolean isListVisible = false;
    private ScrollView mainScrollView;
    private LinearLayout dayButtonsContainer;
    private TextView[] setsCompletedTextViews;
    private WorkoutRoomRepository workoutRepository;
    private WorkoutAdapterForMyplan workoutAdapter;
    private Boolean isWorkoutDeleted = false;
    private BottomNavigationView bottomBar;
    private User user;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        user = getUserFromSharedPreferences();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(view);
        //setupNavigation(view);
        setupDayButtons();
        setupTrainingLayouts();
        setupEditPlanButton();
        setupAddWorkoutButton();
        workoutRepository = new WorkoutRoomRepository(requireContext());
        setupWorkoutListView();
        setupContainers(view);
        loadTodayWorkouts();
    }

    private void loadTodayWorkouts() {
        Date today = Calendar.getInstance().getTime();
        String dayOfWeek = getDayOfWeek(today);
        for (int i = 0; i < dayButtons.length; i++) {
            if (dayButtons[i].getText().toString().equals(dayOfWeek)) {
                dayButtons[i].performClick();
                break;
            }
        }
    }

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);

        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            return gson.fromJson(userJson, userType);
        }
        return null;
    }

    private void navigateToPlanQuestionsActivity() {
        Intent intent = new Intent(getContext(), PlanQuestionsActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private String getDayOfWeek(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = {"SUN", "MON", "TUS", "WED", "THU", "FRI", "SAT"};
        return days[dayOfWeek - 1];
    }

    @SuppressLint("WrongViewCast")
    private void initViews(View view) {
        dayButtons = new Button[]{
                view.findViewById(R.id.sunday),
                view.findViewById(R.id.monday),
                view.findViewById(R.id.tuesday),
                view.findViewById(R.id.wednesday),
                view.findViewById(R.id.thursday),
                view.findViewById(R.id.friday),
                view.findViewById(R.id.saturday)
        };
        selectedDayButton = dayButtons[0];

        progressBars = new ProgressBar[]{
                view.findViewById(R.id.training_progress_bar1),
                view.findViewById(R.id.training_progress_bar2),
                view.findViewById(R.id.training_progress_bar3)
        };
        trainingLayouts = new LinearLayout[]{
                view.findViewById(R.id.Layout1),
                view.findViewById(R.id.Layout2),
                view.findViewById(R.id.Layout3)
        };
        editPlanButton = view.findViewById(R.id.ryd3katlt2w);
        editPlanText = view.findViewById(R.id.r1q5rejf6pyw);
        trashIcon = view.findViewById(R.id.trash_icon);
        bottomBar = view.findViewById(R.id.bottom_navigation);
        addWorkoutButton = view.findViewById(R.id.add_workout_button);
        workoutListView = view.findViewById(R.id.workout_list_view);
        cancelButton = view.findViewById(R.id.cancel_button);
        mainScrollView = view.findViewById(R.id.main_content);
        dayButtonsContainer = view.findViewById(R.id.day_buttons_container);
        setsCompletedTextViews = new TextView[]{
                view.findViewById(R.id.setsCompleted1),
                view.findViewById(R.id.setsCompleted2),
                view.findViewById(R.id.setsCompleted3)
        };

        for (int i = 0; i < setsCompletedTextViews.length; i++) {
            int completedSets = progressStatuses[i] / 10;
            setsCompletedTextViews[i].setText(completedSets + "/10");
        }

        cancelButton.setOnClickListener(v -> toggleWorkoutListVisibility(false));
    }

    private void setupTrainingLayouts() {
        for (int i = 0; i < trainingLayouts.length; i++) {
            final int index = i;
            trainingLayouts[i].setOnClickListener(view -> handleClick(index));
            trainingLayouts[i].setOnLongClickListener(view -> handleLongClick(view, index));
        }
    }

    private void setupAddWorkoutButton() {
        addWorkoutButton.setOnClickListener(v -> toggleWorkoutListVisibility(true));
    }

    private void toggleWorkoutListVisibility(boolean show) {
        if (show) {
            workoutListView.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            adjustLayoutForSplitView();
        } else {
            workoutListView.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            resetLayout();
        }
        isListVisible = show;
    }

    private void adjustLayoutForSplitView() {
        LinearLayout workoutDetailsLayout = requireView().findViewById(R.id.workout_details_layout);
        workoutDetailsLayout.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams mainContentParams = (LinearLayout.LayoutParams) mainScrollView.getLayoutParams();
        mainContentParams.weight = 0.7F;
        mainScrollView.setLayoutParams(mainContentParams);

        LinearLayout.LayoutParams workoutDetailsParams = (LinearLayout.LayoutParams) workoutDetailsLayout.getLayoutParams();
        workoutDetailsParams.weight = 0.3F;
        workoutDetailsLayout.setLayoutParams(workoutDetailsParams);

        dayButtonsContainer.setGravity(Gravity.START);
    }

    private void resetLayout() {
        LinearLayout workoutDetailsLayout = requireView().findViewById(R.id.workout_details_layout);
        workoutDetailsLayout.setVisibility(View.GONE);

        LinearLayout.LayoutParams mainContentParams = (LinearLayout.LayoutParams) mainScrollView.getLayoutParams();
        mainContentParams.weight = 1;
        mainScrollView.setLayoutParams(mainContentParams);

        dayButtonsContainer.setGravity(Gravity.CENTER);
    }

    private void handleClick(int index) {
        if (!isEditModeEnabled) {
            increaseProgressBar(index);
        } else {
            showEditDialog(index);
        }
    }

    private boolean handleLongClick(View view, int index) {
        if (!isEditModeEnabled) {
            undoProgress(index);
        } else {
            startDrag(view);
        }
        return true;
    }

    private void startDrag(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(data, shadowBuilder, view, 0);
        } else {
            view.startDrag(data, shadowBuilder, view, 0);
        }
        view.setBackgroundColor(Color.GRAY);
    }

    private void increaseProgressBar(int index) {
        if (progressStatuses[index] < 100) {
            progressStatuses[index] += 10;
            progressBars[index].setProgress(progressStatuses[index]);
            int completedSets = progressStatuses[index] / 10;
            setsCompletedTextViews[index].setText(completedSets + "/10");
        }
    }

    private void undoProgress(int index) {
        if (progressStatuses[index] > 0) {
            progressStatuses[index] -= 10;
            progressBars[index].setProgress(progressStatuses[index]);
            int completedSets = progressStatuses[index] / 10;
            setsCompletedTextViews[index].setText(completedSets + "/10");
        }
    }

    private void showEditDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit Workout");

        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputSets = new EditText(requireActivity());
        inputSets.setHint("Sets");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(requireActivity());
        inputReps.setHint("Reps");
        layout.addView(inputReps);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String sets = inputSets.getText().toString();
            String reps = inputReps.getText().toString();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

//    private void setupNavigation(View view) {
//        bottomBar.setOnNavigationItemSelectedListener(item -> {
//            int id_item = item.getItemId();
//            if (id_item == R.id.home_BottomIcon) {
//                navigateTo(HomePageActivity.class);
//                return true;
//            } else if (id_item == R.id.plan_BottomIcon) {
//                return true;
//            } else if (id_item == R.id.workouts_BottomIcon) {
//                navigateTo(WorkoutsFilterActivity.class);
//                return true;
//            } else if (id_item == R.id.profile_BottomIcon) {
//                navigateTo(ProfileActivity.class);
//                return true;
//            } else
//                return false;
//        });
//    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(requireActivity(), targetActivity);
        startActivity(intent);
    }

    private void setupDayButtons() {
        View.OnClickListener dayButtonClickListener = view -> {
            if (selectedDayButton != null) {
                selectedDayButton.setBackgroundResource(R.drawable.s000000sw1cr18lr27017b7d9cc0c6073cc);
                selectedDayButton.setSelected(false);
            }

            Button clickedButton = (Button) view;
            clickedButton.setBackgroundResource(R.drawable.s000000sw1cr18bffffff);
            clickedButton.setSelected(true);
            selectedDayButton = clickedButton;
        };

        for (Button dayButton : dayButtons) {
            dayButton.setOnClickListener(dayButtonClickListener);
        }
    }

    private void setupEditPlanButton() {
        editPlanButton.setOnClickListener(v -> toggleEditMode());
        trashIcon.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Delete All Workouts");
        builder.setMessage("Are you sure you want to delete all workouts from your plan?");

        builder.setPositiveButton("Yes", (dialog, which) -> deleteAllWorkoutsFromPlan());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void deleteAllWorkoutsFromPlan() {
        LinearLayout todaysTrainingSection = requireView().findViewById(R.id.workoutsContainer);
        todaysTrainingSection.removeAllViews();
        Toast.makeText(requireContext(), "All workouts have been deleted from the plan.", Toast.LENGTH_SHORT).show();
    }

    private void toggleEditMode() {
        isEditModeEnabled = !isEditModeEnabled;
        editPlanText.setText(isEditModeEnabled ? "Done" : "Edit Plan");
        if (isFirstTime) {
            setupDragAndDrop();
            isFirstTime = false;
        }
        LinearLayout workoutDetailsLayout = requireView().findViewById(R.id.workout_details_layout);
        if (workoutDetailsLayout.getVisibility() == View.VISIBLE)
            resetLayout();
        trashIcon.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
        addWorkoutButton.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
        enableDrag(isEditModeEnabled);
        if (isEditModeEnabled) {
            Toast.makeText(requireContext(), "Dragging workouts(by long clicking) to rearrange them or delete them is enabled", Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(requireContext(), "Edit mode is disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableDrag(boolean enable) {
        if (enable) {
            for (int i = 0; i < trainingLayouts.length; i++) {
                final int index = i;
                trainingLayouts[i].setOnLongClickListener(enable ? view -> handleLongClick(view, index) : null);
            }
        }
    }

    private void setupWorkoutListView() {
        workoutListView = requireView().findViewById(R.id.workout_list_view);
        workoutRepository.getAllWorkoutsLocally().observe(getViewLifecycleOwner(), workouts -> {
            if (workoutAdapter == null) {
                workoutAdapter = new WorkoutAdapterForMyplan(requireContext(), workouts);
                workoutListView.setAdapter(workoutAdapter);
            } else {
                workoutAdapter.notifyDataSetChanged();
            }
        });

        workoutListView.setOnItemLongClickListener((parent, view, position, id) -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            }
            view.setBackgroundColor(Color.WHITE);
            isWorkoutDeleted = true;
            return true;
        });
    }

    private void setupContainers(View view) {
        LinearLayout todaysTrainingSection = view.findViewById(R.id.rzacy26dzg1h);
        setupDropContainer(todaysTrainingSection);
    }

    private void setupDropContainer(LinearLayout dropContainer) {
        dropContainer.setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    if (owner instanceof AdapterView) {
                        int position = workoutListView.getPositionForView(draggedView);
                        Object item = workoutAdapter.getItem(position);
                        if (item instanceof Workout) {
                            workoutAdapter.removeItem(position);
                            workoutAdapter.notifyDataSetChanged();
                            Workout workout = (Workout) item;
                            addWorkoutToPlan(workout);
                        }
                    } else if (owner != dropContainer) {
                        if (view instanceof LinearLayout && !(view == draggedView)) {
                            dropContainer.addView(draggedView);
                            owner.removeView(draggedView);
                        }
                    }
                    draggedView.setBackgroundResource(R.drawable.cr18bffffff);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void addWorkoutToPlan(Workout workout) {
        LinearLayout workoutTemplate = (LinearLayout) LayoutInflater.from(requireContext()).inflate(R.layout.workout_item_template, null);
        workoutTemplate.setBackgroundResource(R.drawable.cr18bffffff);
        populateWorkoutDetails(workoutTemplate, workout);
        LinearLayout todaysTrainingSection = requireView().findViewById(R.id.workoutsContainer);
        todaysTrainingSection.addView(workoutTemplate);
        setupClickListenersForWorkout(workoutTemplate, workout);
        setupDragAndDropForWorkout(workoutTemplate);
    }

    private void populateWorkoutDetails(LinearLayout workoutLayout, Workout workout) {
        TextView workoutNameTextView = workoutLayout.findViewById(R.id.r58ginjerv5oa);
        TextView caloriesTextView = workoutLayout.findViewById(R.id.rqd9c60hxzfa);
        TextView nextSetTextView = workoutLayout.findViewById(R.id.rg5slbkteo9wa);
        ProgressBar progressBar = workoutLayout.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutLayout.findViewById(R.id.setsCompleted2);
        TextView durationTextView = workoutLayout.findViewById(R.id.rc5h6rvwtrkra);
        ShapeableImageView imageView = workoutLayout.findViewById(R.id.rlaob0eufl3a);

        workoutNameTextView.setText(workout.getWorkoutName());
        caloriesTextView.setText(workout.getCalories() + " Kcal");
        nextSetTextView.setText("Next set: " + workout.getSets_reps().get(0) + " reps");
        setsCompletedTextView.setText("0/" + workout.getSets_reps().get(1));
        durationTextView.setText(workout.getDurationInMinutes() + " Mins");
        imageView.setImageResource(workout.getWorkoutLogoResId());

        progressBar.setMax(workout.getSets_reps().get(1) * 10);
        progressBar.setProgress(0);
        progressBar.setProgressDrawable(requireContext().getDrawable(R.drawable.progress_bar_custom));
    }

    private void setupClickListenersForWorkout(View workoutView, Workout workout) {
        workoutView.setOnClickListener(view -> {
            if (!isEditModeEnabled) {
                increaseProgressBarForDynamic(workoutView);
            } else {
                showEditDialogForDynamic(workoutView, workout);
            }
        });

        workoutView.setOnLongClickListener(view -> {
            if (isEditModeEnabled) {
                startDrag(view);
            } else {
                undoProgressForDynamic(workoutView);
            }
            return true;
        });
    }

    private void showEditDialogForDynamic(View workoutView, Workout workout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit Workout");

        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView setsTextView = workoutView.findViewById(R.id.setsCompleted2);

        final EditText inputSets = new EditText(requireActivity());
        inputSets.setHint("Sets:(" + workout.getSets_reps().get(1) + ")");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(requireActivity());
        inputReps.setHint("Reps(" + workout.getSets_reps().get(0) + ")");
        layout.addView(inputReps);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String sets = inputSets.getText().toString();
            String reps = inputReps.getText().toString();
            int setsNum;
            int repsNum;
            if (sets.isEmpty() || reps.isEmpty()) {
                Toast.makeText(requireContext(), "Empty input", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                setsNum = Integer.parseInt(sets);
                repsNum = Integer.parseInt(reps);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                return;
            }
            if (setsNum <= 0 || repsNum <= 0) {
                Toast.makeText(requireContext(), "Sets and reps cannot be negative or zero", Toast.LENGTH_SHORT).show();
                return;
            }
            if (setsNum > 20 || repsNum > 20) {
                Toast.makeText(requireContext(), "Reps and sets cannot be greater than 20", Toast.LENGTH_SHORT).show();
                return;
            }
            workout.setSets_reps(Arrays.asList(Integer.parseInt(sets), Integer.parseInt(reps)));
            workoutRepository.updateWorkout(workout);

            TextView nextSetTextView = workoutView.findViewById(R.id.rg5slbkteo9wa);
            nextSetTextView.setText("Next set: " + reps + " reps");
            ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
            progressBar.setMax(workout.getSets_reps().get(0) * 10);
            progressBar.setProgress(0);
            setsTextView.setText("0/" + sets);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void setupDragAndDropForWorkout(View workoutView) {
        // Removed redundant setOnLongClickListener setup to prevent conflict with undo functionality

        // Set drag listener to handle drop events
        workoutView.setOnDragListener((view, event) -> {
            if (!isEditModeEnabled) {
                // Do not allow drag operations if not in edit mode
                return false;
            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackgroundColor(Color.LTGRAY); // Highlight when entered
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackgroundColor(Color.WHITE); // Remove highlight when exited
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    ViewGroup targetParent = (ViewGroup) view.getParent();

                    // Handle dragging from ListView to LinearLayout
                    if (owner instanceof AdapterView) {
                        int position = workoutListView.getPositionForView(draggedView);
                        Object item = workoutAdapter.getItem(position); // Correctly retrieve the Workout object
                        if (item instanceof Workout) {
                            workoutAdapter.removeItem(position);
                            workoutAdapter.notifyDataSetChanged(); // Refresh the adapter

                            // Create a new workout view from the template and add it to the plan
                            Workout workout = (Workout) item; // Correct cast
                            addWorkoutToPlan(workout);
                        }
                    }
                    // Handle rearranging within the same LinearLayout container
                    else if (owner instanceof LinearLayout && targetParent instanceof LinearLayout) {
                        if (view != draggedView) { // Ensure we are not dragging onto itself
                            int sourceIndex = owner.indexOfChild(draggedView);
                            int targetIndex = targetParent.indexOfChild(view);

                            if (sourceIndex != -1 && targetIndex != -1) {
                                // Rearrange only if source and target indices are valid
                                owner.removeView(draggedView); // Remove the dragged view from its parent
                                targetParent.addView(draggedView, targetIndex); // Add it to the target index
                            }
                        }
                    }

                    // Reset the dragged view's background after dropping
                    draggedView.setBackgroundResource(R.drawable.cr18bffffff); // Reset to original background

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackgroundResource(R.drawable.cr18bffffff);  // Reset to original color
                    return true;
                default:
                    return false;
            }
        });
    }

    private void increaseProgressBarForDynamic(View workoutView) {
        ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutView.findViewById(R.id.setsCompleted2);
        String setAndRestOfTheString = setsCompletedTextView.getText().toString().split("/")[1];
        int sets = Integer.parseInt(setAndRestOfTheString);
        int progress = progressBar.getProgress();
        if (progress < sets * 10) {
            progress += 10;
            progressBar.setProgress(progress);
            int completedSets = progress / 10;
            setsCompletedTextView.setText(completedSets + "/" + setAndRestOfTheString);
        }
    }

    private void undoProgressForDynamic(View workoutView) {
        ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutView.findViewById(R.id.setsCompleted2);
        String setAndRestOfTheString = setsCompletedTextView.getText().toString().split("/")[1];
        int progress = progressBar.getProgress();
        if (progress > 0) {
            progress -= 10;
            progressBar.setProgress(progress);
            int completedSets = progress / 10;
            setsCompletedTextView.setText(completedSets + "/" + setAndRestOfTheString);
        }
    }

    private void setupDragAndDrop() {
        View.OnLongClickListener longClickListener = view -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);

            view.setBackgroundColor(Color.TRANSPARENT);  // Optional: visual cue for dragging

            return true;
        };

        View.OnDragListener dragListener = (view, event) -> {
            LinearLayout layout = (LinearLayout) view;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Optionally clear background here if needed
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackgroundColor(Color.LTGRAY);
                    // Optionally set a temporary background here if needed
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackgroundColor(Color.WHITE);
                    // Optionally reset to original background here if needed
                    return true;
                case DragEvent.ACTION_DROP:
                    View sourceView = (View) event.getLocalState();
                    ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
                    if (sourceParent instanceof ListView) {
                        // Handle dragging from ListView to plan
                        ListView listView = (ListView) sourceParent;
                        int position = listView.getPositionForView(sourceView);
                        Workout workout = (Workout) workoutAdapter.getItem(position);

                        // Add a new workout view to the plan based on dragged workout
                        addWorkoutToPlan(workout);
                        return true;
                    }
                    LinearLayout targetLayout = (LinearLayout) view;
                    ViewGroup targetParent = (ViewGroup) targetLayout.getParent();

                    int sourceIndex = sourceParent.indexOfChild(sourceView);
                    int targetIndex = targetParent.indexOfChild(targetLayout);

                    // Check if source and target are in the same ViewGroup
                    if (sourceParent == targetParent) {
                        if (sourceIndex != targetIndex) {
                            rearrangeWorkouts(targetParent, sourceIndex, targetIndex);
                        }
                    }

                    sourceView.setVisibility(View.VISIBLE); // Ensure visibility
                    view.setBackgroundColor(Color.TRANSPARENT);  // Reset background after drop
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Reset to the original background after dragging ends
                    layout.setBackgroundResource(R.drawable.cr18bffffff);
                    return true;
                default:
                    return false;
            }
        };
        View.OnDragListener trashDragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setAlpha(0.5f); // Visual cue
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setAlpha(1.0f);
                        return true;
                    case DragEvent.ACTION_DROP:
                        View sourceView = (View) event.getLocalState();
                        ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
                        sourceParent.removeView(sourceView); // Remove the workout
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setAlpha(1.0f);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        };
        trashIcon.setOnDragListener(trashDragListener);
        trashIcon.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // Optional: Highlight the trash icon
                        trashIcon.setColorFilter(Color.RED); // Highlight
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // Optional: Remove highlight
                        trashIcon.setColorFilter(null); // Remove highlight
                        break;
                    case DragEvent.ACTION_DROP:
                        // Handle the drop of a workout item
                        if(!isWorkoutDeleted) {
                            View view = (View) event.getLocalState();
                            ViewGroup owner = (ViewGroup) view.getParent();
                            owner.removeView(view); // Remove the workout view
                            // Optional: Update any underlying data or notify adapters
                            break;
                        }

                    case DragEvent.ACTION_DRAG_ENDED:
                        // Optional: Reset any visual cues
                        trashIcon.setColorFilter(null); // Reset color filter if used
                        trashIcon.setVisibility(View.VISIBLE);// Make sure the view is visible if not deleted
                        isWorkoutDeleted=false;
                        break;
                }
                return true;
            }
        });





        for (LinearLayout layout : new LinearLayout[]{trainingLayouts[0],trainingLayouts[1], trainingLayouts[2]}) {
            layout.setOnLongClickListener(longClickListener);
            layout.setOnDragListener(dragListener);
        }
    }


    private void rearrangeWorkouts(ViewGroup parent, int sourceIndex, int targetIndex) {
        View draggedView = parent.getChildAt(sourceIndex);
        parent.removeViewAt(sourceIndex);
        if (targetIndex <= parent.getChildCount()) {
            parent.addView(draggedView, targetIndex);
        } else {
            parent.addView(draggedView);
        }
    }
}
