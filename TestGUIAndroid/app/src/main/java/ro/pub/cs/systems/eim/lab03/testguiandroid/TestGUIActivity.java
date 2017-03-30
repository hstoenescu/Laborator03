package ro.pub.cs.systems.eim.lab03.testguiandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestGUIActivity extends AppCompatActivity {

    /* define objects for text and buttons */
    private EditText leftEditText = null;
    private EditText rightEditText = null;
    private EditText sumEditText = null;
    private EditText diffEditText = null;
    private Button leftButton = null;
    private Button rightButton = null;
    private  Button showSumButton = null;

    private final static int SEC_ACT_REQ_CODE = 1;
    private Button navigateToSecondaryActivityButton = null;

    /* service status used for Service */
    private int serviceStatus = Constants.SERVICE_STOPPED;

    /* define a listener for BUTTON */
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        /* for each button, override the method onClick() */
        @Override
        public void onClick(View view) {
            /* get the number from the text field */
            int leftNumberOfClicks = Integer.parseInt(leftEditText.getText().toString());
            int rightNumberOfClicks = Integer.parseInt(rightEditText.getText().toString());

            switch (view.getId()) {
                case R.id.press_me_button:
                    leftNumberOfClicks++;
                    leftEditText.setText(String.valueOf(leftNumberOfClicks));
                    break;
                case R.id.press_me_button2:
                    rightNumberOfClicks++;
                    rightEditText.setText(String.valueOf(rightNumberOfClicks));
                    break;
                case R.id.show_sum_button:
                    int sum = rightNumberOfClicks+leftNumberOfClicks;
                    sumEditText.setText(String.valueOf(sum));
                    break;
                // implement INTENT for sec button
                case R.id.navigate_to_secondary_activity:
                    Intent intent = new Intent(getApplicationContext(), TestGUIActivitySecondary.class);
                    int nrClicks = Integer.parseInt(leftEditText.getText().toString()) + Integer.parseInt(rightEditText.getText().toString());
                    intent.putExtra("numberOfClicks", nrClicks);
                    startActivityForResult(intent, SEC_ACT_REQ_CODE);
                    break;
            }

            // if the sum > minimum_nr and the service is stopped, then the method startService()
            // is invoked
            if (leftNumberOfClicks + rightNumberOfClicks > Constants.NUMBER_OF_CLICKS_THRESHOLD &&
                    serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), TestGUIService.class);
                intent.putExtra("firstNumber", leftNumberOfClicks);
                intent.putExtra("secondNumber", rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }

    }

    /* a bcast listener must have also an intent-filter associated */
    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gui);

        /* EDIT_TEXT */
        /* references of the graphic controller */
        /* get references to the elements defined in /res/layout/activity_test_gui */
        leftEditText = (EditText)findViewById(R.id.left_edit_text);
        rightEditText = (EditText)findViewById(R.id.right_edit_text);

        /* set the default value to 0 */
        leftEditText.setText(String.valueOf(0));
        rightEditText.setText(String.valueOf(0));

        /* analogue to sum and diff */
        sumEditText = (EditText)findViewById(R.id.show_sum);
        diffEditText = (EditText)findViewById(R.id.show_diff);
        sumEditText.setText(String.valueOf(0));
        diffEditText.setText(String.valueOf(0));

        /* BUTTONS */
        /* get the reference to the buttons */
        leftButton = (Button)findViewById(R.id.press_me_button);
        rightButton = (Button)findViewById(R.id.press_me_button2);
        showSumButton = (Button)findViewById(R.id.show_sum_button);

        /* set the listeners to buttons */
        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);
        showSumButton.setOnClickListener(buttonClickListener);

        /* set the listeners for secondary activity button */
        navigateToSecondaryActivityButton = (Button)findViewById(R.id.navigate_to_secondary_activity);
        navigateToSecondaryActivityButton.setOnClickListener(buttonClickListener);

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    // ex B2 b) -> we want to save the instance of the state
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // in order to save the instance, we need to add a KEY and a VALUE to the Bundle variable
        savedInstanceState.putString("leftCount", leftEditText.getText().toString());
        savedInstanceState.putString("rightCount", rightEditText.getText().toString());
    }

    // after the call of onCreate(), we need to access firstly onRestoreInstanceState, because we
    // may have some values saved
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        // if the key exists, then add the saved value. else initialise with 0
        if (savedInstanceState.containsKey("leftCount")) {
            leftEditText.setText(savedInstanceState.getString("leftCount"));
        } else {
            leftEditText.setText(String.valueOf(0));
        }

        // same for the right edit text
        if (savedInstanceState.containsKey("rightCount")){
            rightEditText.setText(savedInstanceState.getString("rightCount"));
        } else {
            rightEditText.setText(String.valueOf(0));
        }

    }

    /* Broadcast */
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    /* activate/deactivate the listener for intents */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, TestGUIService.class);
        stopService(intent);
        super.onDestroy();
    }

    // TEST ex B2 -> load the app (onCreate(), onStart() and onResume() methods are called)
    // -> press menu on phone
    // -> AndroidMonitor > terminate app
    // -> load the app again (it should work :) )

    // onActivityResult - called automatically when returning to this activity
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
        if (requestCode == SEC_ACT_REQ_CODE) {
            Toast.makeText(this, "The secondary activity returned with " +resultCode, Toast.LENGTH_LONG).show();
        }
    }
}
