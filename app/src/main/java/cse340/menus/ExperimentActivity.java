package cse340.menus;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import cse340.menus.views.AbstractMenuExperimentView;

public class ExperimentActivity extends AbstractMainActivity {

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParticipantNum = 0;

        // programmatically asks for permissions to write to file storage
        // this is for saving the CSV file to disk
        ensurePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ensurePermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        startExperimentSession();
    }

    /**
     * Shows the menu given a trial.
     *
     * @param trial Current trial containing menu information.
     */
    @Override
    protected void showMenuForTrial(ExperimentTrial trial) {
        // Creates Menu based on trial (need to check what menu the current trial requires).
        // Sets the layout parameters for the menu and make it visible on screen
        super.showMenuForTrial(trial);

        // Indicate menu type in the task.
        final TextView instructionTextView = findViewById(R.id.instructionTextView);
        instructionTextView.setText(getString(R.string.trial_message, trial.getMenu(), trial.getItem()));


        // TODO: register a TrialListener with the menu:
        // TODO: When the user completes a trial, the menu listener should take care of the following:
        // TODO: 1. Write the result to your CSV file. This should be accomplished with
        //          ExperimentSession.recordResult().
        // TODO: 2. show the menu for the next trial, if there is another trial available.
        // TODO: 3. If another trial is not available, announce with a Toast and change the text in the
        //          instruction view to say the session is completed by using R.string.session_completed
        //          from strings.xml. For completeness, don’t forget to reset the session (to null)
        //          if you are done with all of the sessions.


        TrialListener newListener = new TrialListener() {
            @Override
            public void onTrialCompleted(ExperimentTrial trial) {
                mSession.recordResult();
                boolean nextOrNo = mSession.hasNext();
                if(nextOrNo) {
                    showMenuForTrial(mSession.next());
                } else {
                    TextView instruction = findViewById(R.id.instructionTextView);
                    instruction.setText(mSession.toString());
                    Toast.makeText(getApplicationContext(), R.string.session_completed, Toast.LENGTH_SHORT).show();
                    mMainLayout.removeView(mMenuView);
                }
            }
        };
        mMenuView.setTrialListener(newListener);

    }
}