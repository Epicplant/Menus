package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public class NormalMenuView extends MenuExperimentView {

    /** Class constant used to determine the size of the normal menu */
    private static final float CELL_HEIGHT_RATIO = 0.104f;
    private static final float CELL_WIDTH_RATIO = 0.277f;
    private static final float TEXT_OFFSET_RATIO = 0.055f;

    /**
     * The height of each menu cell, in pixels. This is set to (CELL_HEIGHT_RATIO) * the device's
     * smaller dimension.
     */
    private float CELL_HEIGHT;

    /**
     * The width of each menu cell, in pixels. This is set to (CELL_WIDTH_RATIO) * the device's
     * smaller dimension.
     */
    private float CELL_WIDTH;

    /** A list of selectable items that will be placed within the menu*/
    private List mItems;

    /**
     * When adding text to your menu cells, TEXT_OFFSET should be used to setup the
     * coordinates of the text in the menu cell. This will ensure that text is "contained" by the menu.
     * For experimentation, try leaving this property off when drawing your menus.
     */
    private float TEXT_OFFSET;

    public NormalMenuView(Context context, List<String> items) {
        super(context, items);
    }

    public NormalMenuView(Context context, ExperimentTrial trial) {
        super(context, trial);
    }

    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    @Override
    protected void setup() {
        // TODO: set initial state to START
        mState = mState.START;
        mItems = getItems();

        // Determine the dimensions of the normal menu
        CELL_HEIGHT = CELL_HEIGHT_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        CELL_WIDTH = CELL_WIDTH_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        TEXT_OFFSET = TEXT_OFFSET_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);

        // TODO: set layout parameters with proper width and height
        //       remembering to take into account the thickness of the pen stroke.

        android.widget.FrameLayout.LayoutParams params =
                new android.widget.FrameLayout.LayoutParams(
                        (int) (CELL_WIDTH+getHighlightPaint().getStrokeWidth()),
                        (int) ((CELL_HEIGHT*mItems.size())+getHighlightPaint().getStrokeWidth()));
        setLayoutParams(params);

        // TODO: initialize any fields you need to (you may create whatever you need)
    }

    /**
     * Calculates the index of the menu item using the current finger position
     * This is specific to your menu's geometry, so override it in your Pie and Normal menu classes
     * If the finger has moved less than MIN_DIST, or is outside the bounds of the menu,
     * return -1.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    protected int essentialGeometry(PointF p) {
        /*
         * TODO: Complete the essentialGeometry function for the normal menu.
         * Remember: you should not be altering the state of your application in this function --
         * you should only return the result.
         */



        if(CELL_WIDTH < p.x || p.x < 0 ||
                (CELL_HEIGHT*mItems.size()) < p.y || p.y < 0 ||
                (p.x <= MIN_DIST && p.y <= MIN_DIST)) {
            return -1;
        } else {

            int yPosition = (int) p.y;
            int index = 0;
            while(yPosition >= CELL_HEIGHT) {
                index++;
                yPosition -= CELL_HEIGHT;
            }

            if(index == mItems.size()) {
                index = 0;
            }

            return index;
        }
    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        /*
         * TODO: Draw the menu.
         * If an option is currently selected, that option should be highlighted.
         *
         * You may change the paint properties for the menu if desired.
         * You can also choose to draw the text horizontally instead of vertically.
         */

        RectF menu = new RectF(getHighlightPaint().getStrokeWidth()/2,
                getHighlightPaint().getStrokeWidth()/2,
                (CELL_WIDTH-getHighlightPaint().getStrokeWidth()/2),
                ((CELL_HEIGHT*mItems.size())-getHighlightPaint().getStrokeWidth()/2));
        canvas.drawRect(menu, getBorderPaint());

        List words = mItems;

        for(int i = 0; i < words.size(); i++) {

            canvas.drawLine(getHighlightPaint().getStrokeWidth()/2, i * CELL_HEIGHT,
                    CELL_WIDTH-getHighlightPaint().getStrokeWidth()/2, i *CELL_HEIGHT,
                    getBorderPaint());

            canvas.drawText(words.get(i).toString(), TEXT_OFFSET, CELL_HEIGHT*7/8 + CELL_HEIGHT*i,
                    getTextPaint());
        }

        if(mState == mState.SELECTING && getCurrentIndex() != -1 ) {

            RectF button = new RectF(getHighlightPaint().getStrokeWidth()/2,
                    getHighlightPaint().getStrokeWidth()/2+(CELL_HEIGHT*getCurrentIndex()),
                    (CELL_WIDTH-getHighlightPaint().getStrokeWidth()/2),
                    (CELL_HEIGHT+(CELL_HEIGHT*getCurrentIndex())-getHighlightPaint().getStrokeWidth()/2));
            canvas.drawRect(button, getHighlightPaint());
        }
    }
}