package com.example.tabsapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    Button strength, transfer;
    AlertDialog.Builder builder;
    SharedPreferences preferences;

    private static final int STR_SET = 1;
    private static final int GPS_SET = 2;
    private static final String PREF = "PREFERENCE";
    private static final String SHAKING = "SHAKING";
    private static final String TRANSFER = "TRANSFER";
    private static final String gps_list[] = {"GPS", "INTERNET"};
    private static final String str_list[] = {"5.0", "7.0", "10.0", "13.0", "15.0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        transfer = (Button) findViewById(R.id.coordinates_button);
        strength = (Button) findViewById(R.id.strength_button);
        preferences = getSharedPreferences(PREF, getApplicationContext().MODE_PRIVATE);

        View.OnClickListener cl1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.strength_button) {
                    showDialog(STR_SET);
                }
                if (view.getId() == R.id.coordinates_button) {
                    showDialog(GPS_SET);
                }
            }
        };
        transfer.setOnClickListener(cl1);
        strength.setOnClickListener(cl1);
    }

    public Dialog onCreateDialog(int id) {
        switch (id) {
            case STR_SET:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.strength_change)
                        .setCancelable(false)
                        .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setSingleChoiceItems(str_list, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(SHAKING, 5);
                                    editor.apply();
                                    ((AlertDialog) dialog).getListView().setItemChecked(0, true);
                                }
                                if (item == 1) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(SHAKING, 7);
                                    editor.apply();
                                    ((AlertDialog) dialog).getListView().setItemChecked(1, true);
                                }
                                if (item == 2) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(SHAKING, 10);
                                    editor.apply();
                                    ((AlertDialog) dialog).getListView().setItemChecked(2, true);
                                }
                                if (item == 3) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(SHAKING, 13);
                                    editor.apply();
                                    ((AlertDialog) dialog).getListView().setItemChecked(3, true);
                                }
                                if (item == 4) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(SHAKING, 15);
                                    editor.apply();
                                    ((AlertDialog) dialog).getListView().setItemChecked(4, true);
                                }
                            }
                        });
                break;
            case GPS_SET:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.coordinate_transfer_method)
                        .setCancelable(false)
                        .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setSingleChoiceItems(gps_list, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(TRANSFER, 1);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Выбрано получение координат по GPS!", Toast.LENGTH_SHORT).show();
                                    ((AlertDialog) dialog).getListView().setItemChecked(0, true);
                                }

                                if (item == 1) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(TRANSFER, 2);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Выбрано получение координат INTERNET!", Toast.LENGTH_SHORT).show();
                                    ((AlertDialog) dialog).getListView().setItemChecked(1, true);
                                }
                            }
                        });
                break;
        }
        return builder.create();
    }
}
