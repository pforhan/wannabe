package wannabe.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView tv = (TextView) findViewById(R.id.text);
    tv.setText("Pat says hi, and: " + getIntent().getData());

    PackageManager mgr = getPackageManager();

    if ("com.htc.HtcLinkifyDispatcher".equals(mgr.resolveActivity(getIntent(),
        PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName)) {
      Intent chooser = Intent.createChooser(getIntent(), "Unlinkify that URL!");
      startActivity(chooser);
    } else {
      startActivity(getIntent());
    }
  }
}
