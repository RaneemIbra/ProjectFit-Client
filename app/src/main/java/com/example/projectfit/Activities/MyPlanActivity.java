package com.example.projectfit.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.Workout;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.WorkoutRoomRepository;
import com.example.projectfit.Utils.WorkoutAdapter;
import com.example.projectfit.Utils.WorkoutAdapterForMyplan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

public class MyPlanActivity extends AppCompatActivity {

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
    private int nextWorkoutIndex = 3; // Start at 3 since three workouts are predefined

    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        setupNavigation();
        setupDayButtons();
        setupTrainingLayouts();
        setupEditPlanButton();
        setupAddWorkoutButton();
        workoutRepository = new WorkoutRoomRepository(this);
        setupWorkoutListView();
        setupContainers();
    }



    @SuppressLint("WrongViewCast")
    private void initViews() {
        dayButtons = new Button[]{
                findViewById(R.id.sunday),
                findViewById(R.id.monday),
                findViewById(R.id.tuesday),
                findViewById(R.id.wednesday),
                findViewById(R.id.thursday),
                findViewById(R.id.friday),
                findViewById(R.id.saturday)
        };
        selectedDayButton = dayButtons[0];

        progressBars = new ProgressBar[]{
                findViewById(R.id.training_progress_bar1),
                findViewById(R.id.training_progress_bar2),
                findViewById(R.id.training_progress_bar3)
        };
        trainingLayouts = new LinearLayout[]{
                findViewById(R.id.Layout1),
                findViewById(R.id.Layout2),
                findViewById(R.id.Layout3)
        };
        editPlanButton = findViewById(R.id.ryd3katlt2w);
        editPlanText = findViewById(R.id.r1q5rejf6pyw);
        trashIcon = findViewById(R.id.trash_icon);
        bottomBar = findViewById(R.id.bottom_navigation);
        addWorkoutButton = findViewById(R.id.add_workout_button);
        workoutListView = findViewById(R.id.workout_list_view);
        cancelButton = findViewById(R.id.cancel_button);
        mainScrollView =findViewById(R.id.main_content);
        dayButtonsContainer = findViewById(R.id.day_buttons_container);
        setsCompletedTextViews = new TextView[]{
                findViewById(R.id.setsCompleted1),
                findViewById(R.id.setsCompleted2),
                findViewById(R.id.setsCompleted3)
        };
        // Synchronize TextViews with progress statuses
        for (int i = 0; i < setsCompletedTextViews.length; i++) {
            int completedSets = progressStatuses[i] / 10; // Calculate initial completed sets
            setsCompletedTextViews[i].setText(completedSets + "/10");
        }

        // Set up Cancel button click listener
        cancelButton.setOnClickListener(v -> toggleWorkoutListVisibility(false));
    }

    private void setupTrainingLayouts() {
        for (int i = 0; i < trainingLayouts.length; i++) {
            final int index = i;

            // Set on click listener to handle both edit and non-edit modes
            trainingLayouts[i].setOnClickListener(view -> handleClick(index));

            // Set on long click listener to handle both edit and non-edit modes
            trainingLayouts[i].setOnLongClickListener(view -> handleLongClick(view, index));
        }
    }

    private void setupAddWorkoutButton() {
        addWorkoutButton.setOnClickListener(v -> toggleWorkoutListVisibility(true));
    }

    private void toggleWorkoutListVisibility(boolean show) {
        if (show) {
            // Show the ListView and adjust the layout
            workoutListView.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            adjustLayoutForSplitView();
        } else {
            // Hide the ListView and reset the layout
            workoutListView.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            resetLayout();
        }
        isListVisible = show;
    }

    private void adjustLayoutForSplitView() {
        LinearLayout workoutDetailsLayout = findViewById(R.id.workout_details_layout);
        workoutDetailsLayout.setVisibility(View.VISIBLE); // Show the workout details

        // Adjust weight to split the screen evenly
        LinearLayout.LayoutParams mainContentParams = (LinearLayout.LayoutParams) mainScrollView.getLayoutParams();
        mainContentParams.weight = 0.65F; // Set weight to 1 to fill half the screen
        mainScrollView.setLayoutParams(mainContentParams);

        LinearLayout.LayoutParams workoutDetailsParams = (LinearLayout.LayoutParams) workoutDetailsLayout.getLayoutParams();
        workoutDetailsParams.weight = 0.35F; // Set weight to 1 to fill half the screen
        workoutDetailsLayout.setLayoutParams(workoutDetailsParams);

        // Adjust the alignment of the day buttons to the left
        dayButtonsContainer.setGravity(Gravity.START); // Align to the left
    }

    private void resetLayout() {
        // Hide the workout details layout
        LinearLayout workoutDetailsLayout = findViewById(R.id.workout_details_layout);
        workoutDetailsLayout.setVisibility(View.GONE); // Hide it

        // Adjust weight to reset main content to full screen
        LinearLayout.LayoutParams mainContentParams = (LinearLayout.LayoutParams) mainScrollView.getLayoutParams();
        mainContentParams.weight = 1; // Full width
        mainScrollView.setLayoutParams(mainContentParams);

        // Reset the alignment of the day buttons to center
        dayButtonsContainer.setGravity(Gravity.CENTER); // Align to the center
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
        view.setBackgroundColor(Color.GRAY);  // Set background color to gray when dragging starts
    }

    private void increaseProgressBar(int index) {
        if (progressStatuses[index] < 100) {
            progressStatuses[index] += 10; // Increase by 10 each click
            progressBars[index].setProgress(progressStatuses[index]);
            // Update the TextView with the new set count
            int completedSets = progressStatuses[index] / 10; // Assuming each progress step represents one set
            setsCompletedTextViews[index].setText(completedSets + "/10");
        }
    }

    private void undoProgress(int index) {
        if (progressStatuses[index] > 0) {
            progressStatuses[index] -= 10; // Decrease by 10 on long press
            progressBars[index].setProgress(progressStatuses[index]);
            // Update the TextView with the new set count
            int completedSets = progressStatuses[index] / 10; // Assuming each progress step represents one set
            setsCompletedTextViews[index].setText(completedSets + "/10");
        }
    }

    private void showEditDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Workout");

        // Add an input field for sets and reps
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputSets = new EditText(this);
        inputSets.setHint("Sets");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(this);
        inputReps.setHint("Reps");
        layout.addView(inputReps);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialog, which) -> {
            // Retrieve and use input values to set workout details
            String sets = inputSets.getText().toString();
            String reps = inputReps.getText().toString();
            // Update workout details logic here
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void setupNavigation() {
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener (){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id_item=item.getItemId();
                if(id_item==R.id.home_BottomIcon)
                {
                    navigateTo(HomePageActivity.class);
                    return true;
                }
                else if (id_item == R.id.plan_BottomIcon)
                {
                    return true;
                }
                else if (id_item==R.id.workouts_BottomIcon)
                {
                    navigateTo(WorkoutsFilterActivity.class);
                    return true;
                }
                else if ( id_item==R.id.profile_BottomIcon)
                {
                    navigateTo(ProfileActivity.class);
                    return true;
                }
                else
                    return false;

            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(MyPlanActivity.this, targetActivity);
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
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view); // Remove the workout view
                        // Optional: Update any underlying data or notify adapters
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // Optional: Reset any visual cues
                        trashIcon.setColorFilter(null); // Reset color filter if used
                        trashIcon.setVisibility(View.VISIBLE); // Make sure the view is visible if not deleted
                        break;
                }
                return true;
            }
        });





        for (LinearLayout layout : new LinearLayout[]{findViewById(R.id.Layout1), findViewById(R.id.Layout2), findViewById(R.id.Layout3)}) {
            layout.setOnLongClickListener(longClickListener);
            layout.setOnDragListener(dragListener);
        }
    }

    private void rearrangeWorkouts(ViewGroup parent, int sourceIndex, int targetIndex) {
        View draggedView = parent.getChildAt(sourceIndex);
        parent.removeViewAt(sourceIndex);  // Remove the view from its current position

        if (targetIndex <= parent.getChildCount()) {
            parent.addView(draggedView, targetIndex);  // Add the view at the new position
        } else {
            parent.addView(draggedView);  // Add it back at the end if targetIndex exceeds count
        }

        // This single step handles reordering by removing and then re-adding the view in the new position
    }

    private void setupEditPlanButton() {
        editPlanButton.setOnClickListener(v -> toggleEditMode());
        // Add click listener to trash icon to delete all workouts
        trashIcon.setOnClickListener(v -> showDeleteConfirmationDialog());

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Workouts");
        builder.setMessage("Are you sure you want to delete all workouts from your plan?");

        builder.setPositiveButton("Yes", (dialog, which) -> deleteAllWorkoutsFromPlan());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void deleteAllWorkoutsFromPlan() {
        LinearLayout todaysTrainingSection = findViewById(R.id.workoutsContainer); // "Today's Training Section" layout
        todaysTrainingSection.removeAllViews(); // Remove all views from the section


        // Optionally, show a message to the user indicating the workouts have been deleted
        Toast.makeText(this, "All workouts have been deleted from the plan.", Toast.LENGTH_SHORT).show();
    }

    private void toggleEditMode() {
        isEditModeEnabled = !isEditModeEnabled;
        editPlanText.setText(isEditModeEnabled ? "Done Editing" : "Edit Plan");
        if(isFirstTime){
            // Set up drag listeners
            setupDragAndDrop();
            isFirstTime = false;
        }
        LinearLayout workoutDetailsLayout = findViewById(R.id.workout_details_layout);
        if(workoutDetailsLayout.getVisibility()==View.VISIBLE)
            resetLayout();
        trashIcon.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
        addWorkoutButton.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
        enableDrag(isEditModeEnabled);

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
        ListView workoutListView = findViewById(R.id.workout_list_view);

        // Observe workouts from repository
        workoutRepository.getAllWorkoutsLocally().observe(this, workouts -> {
            if (workoutAdapter == null) {
                workoutAdapter = new WorkoutAdapterForMyplan(this, workouts);
                workoutListView.setAdapter(workoutAdapter);
            } else {
                workoutAdapter.notifyDataSetChanged(); // Update the adapter when data changes
            }
        });

        // Set up drag listeners for the ListView
        workoutListView.setOnItemLongClickListener((parent, view, position, id) -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            }
            view.setBackgroundColor(Color.WHITE);  // Visual cue for dragging
            return true;
        });
    }
    private void addWorkoutToPlan(Workout workout) {
        // Inflate the workout template layout from XML
        LinearLayout workoutTemplate = (LinearLayout) getLayoutInflater().inflate(R.layout.workout_item_template, null);

        // Set the background for the inflated layout
        workoutTemplate.setBackgroundResource(R.drawable.cr18bffffff); // Ensure the correct background is set

        // Populate workout details into the inflated layout
        populateWorkoutDetails(workoutTemplate, workout);

        // Add the populated workout layout to the "Today's Training Section"
        LinearLayout todaysTrainingSection = findViewById(R.id.workoutsContainer);
        todaysTrainingSection.addView(workoutTemplate);

        // Set up click and drag-and-drop functionality for the new workout layout
        setupClickListenersForWorkout(workoutTemplate,workout);
        setupDragAndDropForWorkout(workoutTemplate);
    }

    // Method to populate workout details in the inflated layout
    private void populateWorkoutDetails(LinearLayout workoutLayout, Workout workout) {
        // Find the relevant views in the inflated layout
        TextView workoutNameTextView = workoutLayout.findViewById(R.id.r58ginjerv5oa);
        TextView caloriesTextView = workoutLayout.findViewById(R.id.rqd9c60hxzfa);
        TextView nextSetTextView = workoutLayout.findViewById(R.id.rg5slbkteo9wa);
        ProgressBar progressBar = workoutLayout.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutLayout.findViewById(R.id.setsCompleted2);
        TextView durationTextView = workoutLayout.findViewById(R.id.rc5h6rvwtrkra);

        // Set the workout details to the views
        workoutNameTextView.setText(workout.getWorkoutName());
        caloriesTextView.setText(workout.getCalories() + " Kcal");
        nextSetTextView.setText("Next set: " + workout.getSets_reps().get(0) + " reps");
        setsCompletedTextView.setText("0/"+workout.getSets_reps().get(1)); // Set initial progress
        durationTextView.setText(workout.getDurationInMinutes() + " Mins");

        // Set up the progress bar
        progressBar.setMax(workout.getSets_reps().get(1)*10);
        progressBar.setProgress(0);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_custom));
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

    private void setupClickListenersForWorkout(View workoutView,Workout workout) {

        // Set up click listener to increase progress or open edit dialog based on mode
        workoutView.setOnClickListener(view -> {
            if (!isEditModeEnabled) {
                increaseProgressBarForDynamic(workoutView); // Method to handle progress increase
            } else {
                showEditDialogForDynamic(workoutView,workout); // Show dialog to edit the dynamic workout
            }
        });

        // Set up long click listener to either start dragging or handle progress undo
        workoutView.setOnLongClickListener(view -> {
            if (isEditModeEnabled) {
                // Start drag operation in edit mode
                startDrag(view);
            } else {
                // Undo progress in non-edit mode on long press for dynamic workouts
                undoProgressForDynamic(workoutView); // Method to handle progress undo
            }
            return true; // Return true to indicate the long click is consumed
        });
    }
    private void showEditDialogForDynamic(View workoutView,Workout workout) {
        // Create an AlertDialog to edit workout details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Workout");

        // Add an input field for sets and reps
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Retrieve existing sets and reps if needed
        TextView setsTextView = workoutView.findViewById(R.id.setsCompleted2);

        final EditText inputSets = new EditText(this);
        inputSets.setHint("Sets:("+workout.getSets_reps().get(1)+")");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(this);
        inputReps.setHint("Reps("+workout.getSets_reps().get(0)+")");
        layout.addView(inputReps);

        builder.setView(layout);


        builder.setPositiveButton("Change", (dialog, which) -> {
            // Retrieve and use input values to set workout details
            String sets = inputSets.getText().toString();
            String reps = inputReps.getText().toString();
            int setsNum;
            int repsNum;
            if(sets.isEmpty() || reps.isEmpty())
            {
                Toast.makeText(this,"Empty input",Toast.LENGTH_SHORT).show();
                return;
            }
            try{
                setsNum=Integer.parseInt(sets);
                repsNum=Integer.parseInt(reps);
            }
            catch (Exception e){
                Toast.makeText(this,"Invalid input",Toast.LENGTH_SHORT).show();
                return;
            }
            if(setsNum<=0 || repsNum<=0)
            {
                Toast.makeText(this,"Sets and reps cannot be negative or zero",Toast.LENGTH_SHORT).show();
                return;
            }
            if(setsNum>20||repsNum>20)
            {
                Toast.makeText(this,"Reps and sets cannot be greater than 20",Toast.LENGTH_SHORT).show();
                return;
            }
            workout.setSets_reps(Arrays.asList(Integer.parseInt(sets), Integer.parseInt(reps)));
            workoutRepository.updateWorkout(workout);
            // Update the workout view's TextView to display the new sets and reps
            TextView nextSetTextView = workoutView.findViewById(R.id.rg5slbkteo9wa);
            nextSetTextView.setText("Next set: " + reps + " reps");
            ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
            progressBar.setMax(workout.getSets_reps().get(0)*10);
            progressBar.setProgress(0);
            // Update the sets completed text
            setsTextView.setText("0/" +sets);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    private void increaseProgressBarForDynamic(View workoutView) {
        ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutView.findViewById(R.id.setsCompleted2);
        String setAndRestOfTheString=setsCompletedTextView.getText().toString().split("/")[1];
        int sets=Integer.parseInt(setAndRestOfTheString);
        int progress = progressBar.getProgress();
        if (progress <sets*10) {
            progress += 10; // Increase by 10 each click
            progressBar.setProgress(progress);

            // Update the TextView with the new set count
            int completedSets = progress / 10;

            setsCompletedTextView.setText(completedSets + "/"+setAndRestOfTheString);
        }
    }

    private void undoProgressForDynamic(View workoutView) {
        ProgressBar progressBar = workoutView.findViewById(R.id.training_progress_bar2);
        TextView setsCompletedTextView = workoutView.findViewById(R.id.setsCompleted2);
        String setAndRestOfTheString=setsCompletedTextView.getText().toString().split("/")[1];
        int progress = progressBar.getProgress();
        if (progress > 0) {
            progress -= 10; // Decrease by 10 on long press
            progressBar.setProgress(progress);

            // Update the TextView with the new set count
            int completedSets = progress / 10;
            setsCompletedTextView.setText(completedSets + "/"+setAndRestOfTheString);
        }
    }
    private void setupDropContainer(LinearLayout dropContainer) {
        dropContainer.setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //view.setBackgroundColor(Color.LTGRAY); // Highlight when entered
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    //view.setBackgroundColor(Color.WHITE); // Remove highlight when exited
                    return true;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();

                    // Ensure that the owner is not an AdapterView (like ListView)
                    if (owner instanceof AdapterView) {
                        // If dragged from a ListView, handle differently
                        int position = workoutListView.getPositionForView(draggedView);
                        Object item = workoutAdapter.getItem(position); // Correctly retrieve the Workout object
                        if (item instanceof Workout) {
                            workoutAdapter.removeItem(position);
                            workoutAdapter.notifyDataSetChanged(); // Refresh the adapter

                            // Create a new workout view from the template and add it to the plan
                            Workout workout = (Workout) item; // Correct cast
                            addWorkoutToPlan(workout);
                        }
                    } else if (owner != dropContainer) {
                        // Handle dropping between different containers
                        if (view instanceof LinearLayout && !(view == draggedView)) {
                            dropContainer.addView(draggedView);
                            owner.removeView(draggedView); // Remove view from the current container
                        }
                    }

                    // Reset the dragged view's background after dropping
                    draggedView.setBackgroundResource(R.drawable.cr18bffffff); // Reset to original background

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    //view.setBackgroundColor(Color.WHITE);  // Reset to original color
                    return true;
                default:
                    return false;
            }
        });
    }
    private void setupContainers() {
        LinearLayout todaysTrainingSection = findViewById(R.id.rzacy26dzg1h); // Example container
        setupDropContainer(todaysTrainingSection); // Enable drop functionality only for this container
    }


}