package com.dot.calculator.material.floating;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.dot.calculator.material.R;

/**
 * Creates the shortcut icon
 */
public class FloatingCalculatorCreateShortCutActivity extends Activity {
    private static final int REQUEST_CODE_WINDOW_OVERLAY_PERMISSION = 10001;

    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(
                        new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())),
                        REQUEST_CODE_WINDOW_OVERLAY_PERMISSION);
            } else {
                onSuccess();
            }
        }
    }

    private void onSuccess() {
        Intent.ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher_floating);

        Intent intent = new Intent();
        Intent launchIntent = new Intent(this, FloatingCalculatorOpenShortCutActivity.class);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        setResult(RESULT_OK, intent);
        finish();
    }

    private void onFailure() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.blank, R.anim.blank);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_WINDOW_OVERLAY_PERMISSION) {
            if (android.os.Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
                onSuccess();
            } else {
                onFailure();
            }
        }
    }
}
