package vgroup.demoweek4.alerts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import vgroup.demoweek4.R;

/**
 * Created by DELL on 9/25/2017.
 */

public class AlertBuilder {
    private static AlertBuilder alertBuilder;

    private AlertBuilder() {
    }

    public static AlertBuilder getInstance() {
        if (alertBuilder == null) {
            alertBuilder = new AlertBuilder();
        }
        return alertBuilder;
    }


    /**
     * displays an alert dialog
     *
     * @param context - context of the calling activity
     * @param message - message to be displayed
     * @param title   - 0 for success, 1 for failure
     */
    public void getDialog(final Activity context, String message, int title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch (title) {
            case 0:
                builder.setTitle(context.getString(R.string.alert_success));
                break;
            case 1:
                builder.setTitle(context.getString(R.string.alert_stop));
                break;
            default:
                builder.setTitle(context.getString(R.string.alert));
                break;
        }

        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(context, context.getString(R.string.alert_toast), Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    /**
     * @param context- of the calling activity
     * @param message- message to be shown
     * @param title-   0 for success and 1 for failure
     * @param action   - various actions to be performed
     */
    public void getDialog(final Activity context, String message, int title, int action) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch (title) {
            case 0:
                builder.setTitle(context.getString(R.string.alert_success));
                break;
            case 1:
                builder.setTitle(context.getString(R.string.alert_stop));
                break;
            default:
                builder.setTitle(context.getString(R.string.alert));
                break;
        }

        builder.setMessage(message);


        switch (action) {
            case 1:

                builder.setPositiveButton(context.getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.finish();
                    }
                });
                break;
            case 3:
                builder.setNeutralButton(context.getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            case 2:
                builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNeutralButton(context.getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.finish();
                    }
                });
            default:
                break;
        }
        builder.show();


    }

    public void getPermissionDialog(final Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Stop!!");
        builder.setMessage("Cannot access without permission. Click yes to grant it");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
}