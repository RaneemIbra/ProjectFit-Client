package com.example.projectfit.Activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
    private int[] progressStatuses = {0, 0, 0};
    private LinearLayout editPlanButton;
    private TextView editPlanText;
    private boolean isEditModeEnabled = false;
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
        setupProgressBars();
        setupDragAndDrop();
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
        bottomBar=findViewById(R.id.bottom_navigation);
    }

    private void setupProgressBars() {
        for (int i = 0; i < trainingLayouts.length; i++) {
            final int index = i;
            trainingLayouts[i].setOnClickListener(view -> increaseProgressBar(index));
        }
    }

    private void increaseProgressBar(int index) {
        if (progressStatuses[index] < 100) {
            progressStatuses[index] += 10;
            progressBars[index].setProgress(progressStatuses[index]);
        }
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
                    navigateTo(WorkoutsListActivity.class);
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
        enableDrag(isEditModeEnabled);
    }
    private void enableDrag(boolean enable) {
        for (LinearLayout layout : trainingLayouts) {
            layout.setOnLongClickListener(enable ? onLongClickListener : null); // Enable or disable long click
        }
    }

    private View.OnLongClickListener onLongClickListener = view -> {
        if (isEditModeEnabled) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setBackgroundColor(Color.LTGRAY); // Visual cue for dragging
            return true;
        }
        return false;
    };


}