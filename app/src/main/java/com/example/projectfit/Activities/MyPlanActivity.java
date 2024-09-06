package com.example.projectfit.Activities;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    }

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

        bottomBar=findViewById(R.id.bottom_navigation);

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
        }
        view.setBackgroundColor(Color.LTGRAY);  // Visual cue for dragging
    }

    private void increaseProgressBar(int index) {
        if (progressStatuses[index] < 100) {
            progressStatuses[index] += 10; // Increase by 10 each click
            progressBars[index].setProgress(progressStatuses[index]);
        }
    }

    private void undoProgress(int index) {
        if (progressStatuses[index] > 0) {
            progressStatuses[index] -= 10; // Decrease by 10 on long press
            progressBars[index].setProgress(progressStatuses[index]);
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
            view.setBackgroundColor(Color.LTGRAY);  // Optional: visual cue for dragging
            return true;
        };

        View.OnDragListener dragListener = (view, event) -> {
            LinearLayout layout = (LinearLayout) view;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Optionally clear background here if needed
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // Optionally set a temporary background here if needed
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    // Optionally reset to original background here if needed
                    return true;
                case DragEvent.ACTION_DROP:
                    View sourceView = (View) event.getLocalState();
                    ViewGroup sourceParent = (ViewGroup) sourceView.getParent();
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


    }

    private void toggleEditMode() {
        isEditModeEnabled = !isEditModeEnabled;
        editPlanText.setText(isEditModeEnabled ? "Done Editing" : "Edit Plan");
        if(isFirstTime){
        // Set up drag listeners
        setupDragAndDrop();
        isFirstTime = false;
        }
        trashIcon.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);
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
}
