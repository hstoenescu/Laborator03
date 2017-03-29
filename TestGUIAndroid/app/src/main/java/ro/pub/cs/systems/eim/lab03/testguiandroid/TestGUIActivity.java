package ro.pub.cs.systems.eim.lab03.testguiandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TestGUIActivity extends AppCompatActivity {

    /* define objects for text and buttons */
    private EditText leftEditText = null;
    private EditText rightEditText = null;
    private EditText sumEditText = null;
    private EditText diffEditText = null;
    private Button leftButton = null;
    private Button rightButton = null;
    private  Button showSumButton = null;

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

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gui);

        /* EDIT_TEXT */
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
    }
}
