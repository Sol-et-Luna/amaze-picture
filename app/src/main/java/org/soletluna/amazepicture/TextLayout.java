package org.soletluna.amazepicture;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by David on 8/23/2015.
 */
public class TextLayout extends FrameLayout
{
    private RelativeLayout bigDaddy = null;

    private TextView textView = null;
    private ImageView highlight = null;
    private ImageView delete = null;
    private int style = Typeface.NORMAL;

    public TextLayout(Context context)
    {
        super(context);

        setClipChildren(false);

        //========== BIG DADDY ==========
        bigDaddy = new RelativeLayout(context);
        bigDaddy.setLayoutParams(new SquareLayout.LayoutParams(10000, 10000, Gravity.CENTER));

        //========== TEXT VIEW ==========
        textView = new TextView(context);
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                Gravity.LEFT
        ));
        textView.setSingleLine(true);

        //========== HIGHLIGHT RECTANGLE ==========
        highlight = new ImageView(context);
        highlight.setImageResource(R.drawable.highlight_rectangle);
        highlight.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                Gravity.LEFT
        ));

        highlight.setVisibility(VISIBLE);

        //========== DELETE ICON ==========
        delete = new ImageView(context);
        delete.setImageResource(R.drawable.ic_delete);
        delete.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.LEFT | Gravity.TOP
        ));

        delete.setVisibility(VISIBLE);

        //Place delete icon
        delete.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    private boolean hasRun = false;

                    @Override
                    public void onGlobalLayout()
                    {
                        if (!hasRun) {
                            textView.setPadding(delete.getHeight() / 2 + 10, delete.getHeight() / 2,
                                    delete.getHeight() / 2 + 10, 0);
                            highlight.setPadding(delete.getHeight() / 2, delete.getHeight() / 2,
                                    delete.getHeight() / 2, 0);

//                            delete.setY(delete.getY() - delete.getHeight() / 2);
//                            delete.setX(delete.getX() - delete.getWidth() / 2);

                            hasRun = true;
                        }
                    }
                }
        );

        //========== ADD VIEWS ==========
        addView(textView);
        addView(highlight);
        addView(delete);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        setLayoutParams(params);

        bigDaddy.addView(this);
        bigDaddy.setClipChildren(false);

        setClipChildren(false);
    }

    public TextView getTextView()
    {
        return textView;
    }

    public void setFocus(boolean focus)
    {
        if (focus)
        {
            highlight.setVisibility(VISIBLE);
            delete.setVisibility(VISIBLE);
        }
        else
        {
            highlight.setVisibility(INVISIBLE);
            delete.setVisibility(INVISIBLE);
        }
    }

    public ImageView getDeleteIcon()
    {
        return delete;
    }

    public void setStyle(int style)
    {
        this.style = style;
    }

    public int getStyle()
    {
        return style;
    }

    public RelativeLayout getBigDaddy()
    {
        return bigDaddy;
    }
}
