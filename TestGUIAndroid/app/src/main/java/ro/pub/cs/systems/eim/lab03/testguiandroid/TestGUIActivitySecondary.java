package ro.pub.cs.systems.eim.lab03.testguiandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TestGUIActivitySecondary extends AppCompatActivity {

    // get the objects for graphic controller
    private TextView numberOfClickTextView = null;
    private Button okButton = null;
    private Button cancelButton = null;

    // add an inner class - listener
    private ButtonClickListener buttonClickLister = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        // override the method for onClick()
        @Override
        public void onClick (View view) {
            switch (view.getId()) {
                case R.id.ok_button:
                    // setResult -> value + intent(for additional info)
                    // send back to the main method the result from the secondary one
                    setResult(RESULT_OK,null);
                    break;
                case R.id.cancel_button:
                    // RESULT_CANCEL is by default in AStudio
                    setResult(RESULT_CANCELED,null);
                    break;
            }

            // finalize the method forced
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load the gui from the xml file
        setContentView(R.layout.activity_test_guisecondary);

        // get the reference to the gui control element
        // for text view -> INTENT!!!!!
        numberOfClickTextView = (TextView) findViewById(R.id.nr_clicks_text_view);
        // get the intent and verify if it isn't null and has the key "numberOfClicks"
        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey("numberOfClicks")) {
            int nrClicks = intent.getIntExtra("numberOfClicks", -1);
            numberOfClickTextView.setText(String.valueOf(nrClicks));
        }

        // set the listener for the buttons
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(buttonClickLister);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(buttonClickLister);

    }
}
