package dvalcarce.queuemeter;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class QueueMeterActivity extends Activity {

	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QueueMeterActivity.context = getApplicationContext();

		setContentView(R.layout.main_activity);
		showSettings();
	}

	public static Context getAppContext() {
		return QueueMeterActivity.context;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
	}

	@Override
	protected void onStop() {
		removeButtons();
		QueueData.deleteInstances();
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			removeButtons();
			showSettings();
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

	/**
	 * Removes all data and create new instances
	 */
	private void reset() {
		removeButtons();
		int number = QueueData.getInstance().getNumberInstances();
		QueueData.deleteInstances();
		QueueData.createInstances(number);
		createButtons(number);
		Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * Settings dialog
	 * 
	 * You can choose the number of queues to measure here
	 */
	private void showSettings() {

		View view = getLayoutInflater().inflate(R.layout.settings_dialog,
				(ViewGroup) this.findViewById(R.layout.main_activity));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		builder.setTitle(R.string.settings_title);

		final AlertDialog dialog = builder.create();

		Button dialogButton = (Button) view.findViewById(R.id.acceptButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText text = (EditText) dialog
						.findViewById(R.id.queuePicker);
				try {
					Integer n = Integer.parseInt(text.getText().toString());
					QueueData.deleteInstances();
					if (QueueData.createInstances(n)) {
						createButtons(n);
						dialog.dismiss();
					} else {
						Toast.makeText(getApplicationContext(),
								"Maximo " + QueueData.maxQueues + " colas",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.d("QueueMeterActivity", "Exception: " + e.getMessage() + e);
					Toast.makeText(getApplicationContext(), "Valor inválido",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		dialog.show();
	}

	/**
	 * Removes all horizontal layouts and buttons inside
	 */
	protected void removeButtons() {
		TableLayout table = (TableLayout) this.findViewById(R.id.table);

		for (int i = 0; i < table.getChildCount(); i++) {
			TableRow row = (TableRow) table.getChildAt(i);
			row.removeAllViews();
		}
		table.removeAllViews();
	}

	/**
	 * Creates a pair of buttons (IN and OUT) for each queue
	 * 
	 * @param n
	 *            number of button pairs
	 */
	protected void createButtons(int n) {
		TableLayout table = (TableLayout) this.findViewById(R.id.table);

		for (int i = 0; i < n; i++) {
			final int j = i;
			// Row
			TableRow row = new TableRow(this);

			// arrivalButton
			Button arrivalButton = new Button(this);
			arrivalButton.setText("IN " + (i + 1));
			arrivalButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					QueueData.getInstance().arrival(j, new Date());
				}
			});

			// departureButton
			Button departureButton = new Button(this);
			departureButton.setText("OUT " + (i + 1));
			departureButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					QueueData.getInstance().departure(j, new Date());
				}
			});

			TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
			buttonParams.setMargins(10, 20, 20, 10);
			row.addView(arrivalButton, buttonParams);
			row.addView(departureButton, buttonParams);

			TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			table.addView(row, rowParams);
		}
	}
}
