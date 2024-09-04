package com.example.projectfit.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectfit.R;

public class DialogUtils {

    public static void showAddCupSizeDialog(Context context, OnCupSizeAddedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_cup_size, null);
        builder.setView(dialogView);

        EditText cupSizeInput = dialogView.findViewById(R.id.cup_size_input);

        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String cupSizeStr = cupSizeInput.getText().toString().trim();

            if (!cupSizeStr.isEmpty() && cupSizeStr.matches("\\d+")) {
                int cupSize = Integer.parseInt(cupSizeStr);
                listener.onCupSizeAdded(cupSize);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please enter a valid integer for cup size", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnCupSizeAddedListener {
        void onCupSizeAdded(int cupSize);
    }
}
