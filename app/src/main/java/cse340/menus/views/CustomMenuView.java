package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public class CustomMenuView extends MenuExperimentView {

    public CustomMenuView(Context context, ExperimentTrial trial) { super(context, trial); }
    public CustomMenuView(Context context, List<String> items) { super(context, items); }

    /** The width of a single selectable icon within the menu*/
    private int randomMapWidth;

    /** The height of a single selectable icon within the menu*/
    private int randomMapHeight;

    /** A list of selectable items that will be placed within the menu*/
    private List mItems;

    /** The distance from the right edge of a selectable item to the center
     * of a circle within the item. Note this is only for x-axis.*/
    private int mCircleOffset;

    /** The width of the bounding box surrounding the created custom menu*/
    private int boundingWidth;

    /** The height of the bounding box surrounding the created custom menu */
    private int boundingHeight;

    /** A list containing the proportions (width, height, starting point, etc.) of every created
     * selectable item for the custom menu*/
    private java.util.ArrayList<RectF> allRectangles;

    /** The paint brush used to draw the borders of of the menu
     * (in this case just selectable icons)*/
    private Paint mBorderBrush;

    /** The paint brush used to draw highlighted outlines of selected boxes (in this case
     * also used for circles and lines)*/
    private Paint mHighlightBrush;

    /** The paint brush used to draw text within selectable icons*/
    private Paint mTextBrush;

    /** A paint brush used to make parts of the screen white in order to hide
     * the rest of the menu.*/
    private Paint mWhiteBrush;

    /** A recording of the index of the last selected iconA*/
    private int mLastSelectedIndex;

    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    @Override
    protected void setup() {
        // TODO: set initial state to START
        mState = mState.START;
        mItems = getItems();
        // TODO: set layout parameters with proper width and height

        randomMapWidth = (int) (.3*mDisplayMetrics.widthPixels);
        randomMapHeight = (int) (.05*mDisplayMetrics.heightPixels);
        boundingWidth = (int)(randomMapWidth*7/2+getHighlightPaint().getStrokeWidth());
        boundingHeight = (int)(randomMapHeight+randomMapHeight*3/2*(mItems.size()-1)+
                               getHighlightPaint().getStrokeWidth());

        android.widget.FrameLayout.LayoutParams params =
                new android.widget.FrameLayout.LayoutParams(boundingWidth, boundingHeight);
        setLayoutParams(params);


        //       remembering to take into account the thickness of the pen stroke.
        // TODO: initialize any fields you need to (you may create whatever you need)
        mCircleOffset = (int)(randomMapWidth*.15);
         allRectangles = new java.util.ArrayList<RectF>();

         mBorderBrush = getBorderPaint();
         mHighlightBrush = getHighlightPaint();
         mTextBrush = getTextPaint();
         mWhiteBrush = new Paint();
         mWhiteBrush.setColor(Color.WHITE);
         mLastSelectedIndex = 0;
    }

    /**
     * Calculates the essential geometry for the custom menu.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    protected int essentialGeometry(PointF p) {
        // TODO: implement this
        int index = -1;
        RectF currentlySelectedBox = new RectF();


        for(int i = 0; i < allRectangles.size(); i++) {
            if(allRectangles.get(i).contains(p.x, p.y) &&
                    !allRectangles.get(i).equals(currentlySelectedBox)) {

                index = i;
                currentlySelectedBox = allRectangles.get(i);
            }
        }

        return index;

    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: implement this
        //createLeftBox(canvas, 0, 0);

        int odd = 1;
        int size = mItems.size()/2;

        for(int i = 0; i <= size; i+= 2) {

            createRightBox(canvas,0, i*randomMapHeight*3/2, i);
            canvas.drawLine(randomMapWidth-mCircleOffset,
                    i*randomMapHeight*3/2+randomMapHeight/2,
                    randomMapWidth*3/2+mCircleOffset,
                    i*randomMapHeight*3/2+randomMapHeight/2, mHighlightBrush);

            canvas.drawLine(randomMapWidth*3/2+mCircleOffset,
                    i*randomMapHeight*3/2+randomMapHeight/2,
                    randomMapWidth*3/2+mCircleOffset,
                    2*randomMapHeight+i*randomMapHeight*3/2, mHighlightBrush);

            createLeftBox(canvas, randomMapWidth*3/2,
                    odd*randomMapHeight*3/2,odd);

            canvas.drawLine(randomMapWidth*3/2+mCircleOffset,
                    odd*randomMapHeight*3/2+randomMapHeight/2,
                    randomMapWidth-mCircleOffset,
                    odd*randomMapHeight*3/2+randomMapHeight/2, mHighlightBrush);

            canvas.drawLine(randomMapWidth-mCircleOffset,
                    odd*randomMapHeight*3/2+randomMapHeight/2,
                    randomMapWidth-mCircleOffset,
                    2*randomMapHeight+odd*randomMapHeight*3/2, mHighlightBrush);

            odd +=2;

        }

        if((mItems.size()) % 2 != 0) {
            createRightBox(canvas, 0,
                    (mItems.size()-1)*(randomMapHeight)*3/2, mItems.size()-1);
        } else {
            createRightBox(canvas, 0, (mItems.size()-2)*(randomMapHeight)*3/2,
                    mItems.size()-2);

            canvas.drawLine(randomMapWidth-mCircleOffset,
                    (mItems.size()-2)*randomMapHeight*3/2+mCircleOffset,
                    randomMapWidth*3/2+mCircleOffset,
                    (mItems.size()-2)*randomMapHeight*3/2+mCircleOffset, mHighlightBrush);

            canvas.drawLine(randomMapWidth*3/2+mCircleOffset,
                    (mItems.size()-2)*randomMapHeight*3/2+mCircleOffset,
                    randomMapWidth*3/2+mCircleOffset,
                    2*randomMapHeight+(mItems.size()-2)*randomMapHeight*3/2, mHighlightBrush);

            createLeftBox(canvas, randomMapWidth*3/2,
                    (mItems.size()-1)*(randomMapHeight)*3/2, mItems.size()-1);
        }

        if(mState == mState.SELECTING && getCurrentIndex() != -1) {

            RectF menu = allRectangles.get(getCurrentIndex());
            canvas.drawRect(menu.left, menu.top, menu.right, menu.bottom, mHighlightBrush);
            canvas.drawRect(0, menu.bottom+getHighlightPaint().getStrokeWidth()/2,
                    boundingWidth, boundingHeight, mWhiteBrush);
            mLastSelectedIndex = getCurrentIndex();

        } else if(getCurrentIndex() == -1) {

            RectF menu = allRectangles.get(mLastSelectedIndex);
            canvas.drawRect(0, menu.bottom+getHighlightPaint().getStrokeWidth()/2,
                    boundingWidth, boundingHeight, mWhiteBrush);

        }
    }

    /**
     * A method used to create a selectable box with a red circle on the right side of the box.
     * @param canvas The canvas to draw on.
     * @param xChange The distance from the left side of the bounding box to the left of the selectable box.
     * @param yChange The distance from the top of the bounding box to the top of the selectable box.
     * @param index The index of the selectable box that is being constructed.
     */
    public void createRightBox(Canvas canvas, int xChange, int yChange, int index) {
        RectF menu = new RectF(xChange+mHighlightBrush.getStrokeWidth()/2,
                yChange+mHighlightBrush.getStrokeWidth()/2,
                (xChange+randomMapWidth-mHighlightBrush.getStrokeWidth()/2),
                (yChange+randomMapHeight-mHighlightBrush.getStrokeWidth()/2));

        canvas.drawRect(menu, mBorderBrush);
        if(!allRectangles.contains(menu)) {
            allRectangles.add(menu);
        }

        canvas.drawCircle(xChange+randomMapWidth-mCircleOffset, yChange+randomMapHeight/2,
                randomMapHeight/5, mHighlightBrush);

        canvas.drawText(mItems.get(index).toString(),
                xChange+randomMapWidth/4, yChange+randomMapHeight*3/4, mTextBrush);
    }

    /**
     * A method used to create a selectable box with a red circle on the left side of the box.
     * @param canvas The canvas to draw on.
     * @param xChange The distance from the left side of the bounding box to the left of the selectable box.
     * @param yChange The distance from the top of the bounding box to the top of the selectable box.
     * @param index The index of the selectable box that is being constructed.
     */
    public void createLeftBox(Canvas canvas, int xChange, int yChange, int index) {

        RectF menu = new RectF(xChange+mBorderBrush.getStrokeWidth()/2,
                yChange+mBorderBrush.getStrokeWidth()/2,
                (xChange+randomMapWidth-mBorderBrush.getStrokeWidth()/2),
                (yChange+randomMapHeight-mBorderBrush.getStrokeWidth()/2));
        canvas.drawRect(menu, mBorderBrush);

       if(!allRectangles.contains(menu)) {
           allRectangles.add(menu);
       }

        canvas.drawCircle(xChange+mCircleOffset, yChange+randomMapHeight/2,
                randomMapHeight/6, mHighlightBrush);

        canvas.drawText(mItems.get(index).toString(), xChange+randomMapWidth/4,
                yChange+randomMapHeight*3/4, mTextBrush);

    }

    /**
     * End the menu selection by resetting the last selected index to an index of 0
     * a trial (if in experiment mode).
     * @param point The current position of the mouse.
     * @param menuItem the menu item that was selected by the user.
     */
    @Override
    protected void endSelection(int menuItem, PointF point) {
        mLastSelectedIndex = 0;
        super.endSelection(menuItem, point);
    }
}