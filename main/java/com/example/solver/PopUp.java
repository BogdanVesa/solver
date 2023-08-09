package com.example.solver;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopUp extends DialogFragment {


    private String selectedItem= null;

    public String getSelectedItem(){
        return selectedItem;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        int pos=0;
        String[] types = getResources().getStringArray(R.array.types_equation);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);

        if(selectedItem != null){
            for(int i=0;i< types.length ; i++){
                if(types[i]==selectedItem)
                    pos=i;
            }
        }
        builder.setTitle(R.string.chose_equation)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.types_equation, pos,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItem = types[which];
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
