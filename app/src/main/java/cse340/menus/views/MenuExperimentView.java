package cse340.menus.views;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public abstract class
MenuExperimentView extends AbstractMenuExperimentView {

    /**
     * Constructor
     *
     * @param context
     * @param trial Experiment trial (contains a list of items)
     */
    public MenuExperimentView(Context context, ExperimentTrial trial) { super(context, trial); }

    /**
     * Constructor
     *
     * @param context
     * @param items Items to display in menu
     */
    public MenuExperimentView(Context context, List<String> items) { super(context, items); }

    /**
     * Calculates the index of the menu item using the current finger position
     * This is specific to your menu's geometry, so override it in your Pie and Normal and Custom menu classes.
     *
     * Note that you should not be altering your menu's state within essentialGeometry.
     * This function should return a value to your touch event handler, and nothing more.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    protected abstract int essentialGeometry(PointF p);

    /***
     * Handles user's touch input on the screen. It should follow the state machine specified
     * in the spec.
     *
     * @param event Event for touch.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mState == null) {
            Log.i(TAG, "mState is null for this menu");
            return false;
        }

        int menuItem = essentialGeometry(event);

        /*
         * TODO: Implement the state machine for all of your views.
         * All of the state logic should be handled here, you won't need to change
         * this for Pie, Normal, and Custom menu to work. Use the state machine defined in the spec for reference.
         *
         * Below is the template for the state machine. You should use the state field to
         * fetch the menu's current state, and process it accordingly.
         */

        /*
         * switch (state) {
         * . . .
         * }
         */

        android.graphics.PointF mouse = new android.graphics.PointF();
        int x = (int) event.getX();
        int y = (int) event.getY();
        mouse.set(x, y);
        switch(mState) {
            case START:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mState = mState.SELECTING;
                    startSelection(mouse);
                    return true;
                }
                return false;
            case SELECTING:
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    updateModel(menuItem);
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    endSelection(menuItem, mouse);
                    return true;
                }
                return false;

                default: break;
        }
        return false;
    }

    //////////////////////////////////////////////////
    // These methods are taken directly from the spec's description of the PPS
    //////////////////////////////////////////////////

    /**
     * Start the menu selection by recording the starting point and start the trial
     * @param point The current position of the mouse
     */
    protected void startSelection(PointF point) {
        startTrialRecording(point);
        // TODO: Make this visible
        setVisibility(VISIBLE);
    }

    /**
     * Complete the menu selection and end the trial
     * @param menuItem the menu item that was selected by the user
     * @param point The current position of the mouse
     */
    protected void endSelection(int menuItem, PointF point) {
        // TODO: 1) Announce the selection using a Toast (or "Nothing Selected" if it is -1)
        announce(getItem());
        endTrialRecording(menuItem, point);

        // TODO: 2) reset state machine
        // think about what might need to be reset here. What fields are used in your state machine?
        mState = mState.START;
        setCurrentIndex(-1);
        setVisibility(INVISIBLE);
    }

    /**
     * Change the model of the menu and force a redraw, if the current selection has changed.
     * @param menuItem the menu item that is currently selected by the user
     */
    protected void updateModel(int menuItem) {
        // TODO: check if the item selected has changed. If so
        // TODO: 1) update your menu's model
        
        if(menuItem != getCurrentIndex()) {
            setCurrentIndex(menuItem);
            invalidate();
        }
    }

    /**
     * Start the trial for this menu if you're in experiment mode
     * @param point The current position of the mouse
     */
    private void startTrialRecording(PointF point) {
        // TODO: call trial.startTrial() (only if in experiment mode), passing it the position of the mouse
        if(experimentMode()) {
            getTrial().startTrial(point);
        }
    }

    /**
     * End this Menu trial by recording the trial data (if necessary)
     * @param menuItem the menu item that was selected by the user
     * @param point The current position of the mouse
     */
    protected void endTrialRecording(int menuItem, PointF point) {
        if(getTrialListener() != null) {
            getTrial().endTrial(point, menuItem);
            getTrialListener().onTrialCompleted(getTrial());
        }

        // TODO: if the menu trial listener is not null, notify it
        // TODO:  a) call trial.endTrial(), passing it the pointer position and the currently selected item
        // TODO:  b) call onTrialCompleted(trial)
    }
}