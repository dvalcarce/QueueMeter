package dvalcarce.queuemeter;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		show_settings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			show_settings();
			return true;
		case R.id.menu_reset:
			reset();
			return true;
		case R.id.menu_exit:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void reset() {
		int number = QueueData.getInstance().getNumberInstances();
		QueueData.createInstances(number);
		Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT)
				.show();
	}

	private void show_settings() {

		View view = getLayoutInflater().inflate(R.layout.settings_dialog,
				(ViewGroup) this.findViewById(R.layout.main_activity));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		builder.setTitle(R.string.settings_title);

		final AlertDialog dialog = builder.create();
		// dialog.setTitle(R.string.settings_title);
		// dialog.setContentView(R.layout.settings_dialog);

		Button dialogButton = (Button) view.findViewById(R.id.acceptButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText text = (EditText) dialog
						.findViewById(R.id.queuePicker);
				Integer n = Integer.parseInt(text.getText().toString());
				if (QueueData.createInstances(n)) {
					createButtons(n);
					dialog.dismiss();
				}
			}
		});

		dialog.show();

		Toast.makeText(getApplicationContext(), "Menú Settings",
				Toast.LENGTH_SHORT).show();

	}

	protected void createButtons(int n) {
		LinearLayout vertical = (LinearLayout) this
				.findViewById(R.id.vertical_layout);

		for (int i = 0; i < n; i++) {
			final int j = i;
			LinearLayout horizontal = new LinearLayout(this);
			Button inButton = new Button(this);
			inButton.setText("IN " + i);
			inButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					QueueData.getInstance().arrival(j, new Date());
				}
			});
			Button outButton = new Button(this);
			outButton.setText("OUT " + i);
			outButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					QueueData.getInstance().departure(j, new Date());
				}
			});

			horizontal.addView(inButton);
			horizontal.addView(outButton);
			vertical.addView(horizontal);
		}

	}
}
