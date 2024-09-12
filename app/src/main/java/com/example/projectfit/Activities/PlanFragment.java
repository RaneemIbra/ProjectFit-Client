package com.example.projectfit.Activities;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
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
import androidx.fragment.app.Fragment;

import com.example.projectfit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlanFragment extends Fragment {
    LinearLayout layout1,layout2,layout3;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PlanFragment() {
        // Required empty public constructor
    }

    public static PlanFragment newInstance(String param1, String param2) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        layout1=view.findViewById(R.id.Layout1);
        layout2=view.findViewById(R.id.Layout2);
        layout3=view.findViewById(R.id.Layout3);

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
        editPlanButton =  view.findViewById(R.id.ryd3katlt2w);
        editPlanText =  view.findViewById(R.id.r1q5rejf6pyw);
        trashIcon =  view.findViewById(R.id.trash_icon);

        setupDayButtons();
        setupTrainingLayouts();
        setupEditPlanButton();
        return view;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Workout");

        // Add an input field for sets and reps
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputSets = new EditText(getContext());
        inputSets.setHint("Sets");
        layout.addView(inputSets);

        final EditText inputReps = new EditText(getContext());
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

        for (LinearLayout layout : new LinearLayout[]{layout1, layout2, layout3}) {
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