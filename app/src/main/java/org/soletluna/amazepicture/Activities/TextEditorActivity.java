package org.soletluna.amazepicture.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.soletluna.amazepicture.R;
import org.soletluna.amazepicture.SquareLayout;
import org.soletluna.amazepicture.TextLayout;
import org.soletluna.amazepicture.util.ComicSansUtil;
import org.soletluna.amazepicture.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextEditorActivity extends AppCompatActivity
{
    private SquareLayout squareLayout = null;
    private RelativeLayout optionsLayout = null;
    private EditText editText = null;
    private SeekBar textSizeSeekBar = null;
    private SeekBar rotateTextSeekBar = null;
    private CheckBox bold = null;
    private CheckBox italic = null;

    private List<TextLayout> textList = new ArrayList<TextLayout>();
    private TextLayout lastTouched = null;

    private static String AMAZE_PIC_DIR = "Amaze Picture";
    private static String TEMP = "temp.jpg";

    private final String[] randomWords = {"wow", "amaze", "many words", "very text",
            "pls", "such image"};

    // Permission IDs
    private final int WRITE_EXTERNAL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set toolbar font
        TextView toolbarText = (TextView) toolbar.findViewById(R.id.toolbar_text);
        ComicSansUtil.changeToComicSans(toolbarText, getAssets());

        squareLayout = (SquareLayout) findViewById(R.id.text_editor_square_layout);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        // Add temp.jpg to imageView
        Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().toString() + "/" + TEMP);
        imageView.setImageBitmap(bitmap);

        // Setup options elements
        optionsLayout = (RelativeLayout) findViewById(R.id.text_editor_options_layout);
        editText = (EditText) findViewById(R.id.text_editor_edit_text);
        textSizeSeekBar = (SeekBar) findViewById(R.id.text_editor_size_seekbar);
        rotateTextSeekBar = (SeekBar) findViewById(R.id.text_editor_rotate_seekbar);
        bold = (CheckBox)findViewById(R.id.text_editor_bold_checkbox);
        italic = (CheckBox)findViewById(R.id.text_editor_italic_checkbox);

        setupOptionsElements();

        // Request Permissions
        requestStoragePermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_image)
        {
            if (getLastTouched() != null)
            {
                getLastTouched().setFocus(false);
            }

            Util.externalSaveJPGFromBitmap(Util.layoutToBitmap(squareLayout), AMAZE_PIC_DIR,
                    Util.getCurrentTimeJPGString(),
                    getContentResolver(),
                    true);

            if (getLastTouched() != null)
            {
                getLastTouched().setFocus(true);
            }

            Snackbar snackbar = Snackbar.make(squareLayout, "Image Saved", Snackbar.LENGTH_LONG);

            // Snackbar to comic sans
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            ComicSansUtil.changeToComicSans(tv, getAssets());

            snackbar.show();
        }
        else if (id == R.id.share_image)
        {
            if (getLastTouched() != null)
            {
                getLastTouched().setFocus(false);
            }

            shareBitmapAsJPG(Util.layoutToBitmap(squareLayout),
                    AMAZE_PIC_DIR,
                    Util.getCurrentTimeJPGString());

            if (getLastTouched() != null)
            {
                getLastTouched().setFocus(true);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        backDialog();
    }

    /***********************************************************************************************
    * Private functions
    ***********************************************************************************************/

    private void requestStoragePermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // Show an explanation to the user
                Snackbar snackbar = Snackbar.make(squareLayout,
                        getString(R.string.storage_permission_explanation),
                        Snackbar.LENGTH_INDEFINITE);

                snackbar.setAction("Ok", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ActivityCompat.requestPermissions(TextEditorActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_EXTERNAL);
                    }
                });

                // Snackbar to comic sans
                View view = snackbar.getView();
                TextView tv = (TextView) view.findViewById(
                        android.support.design.R.id.snackbar_text);
                ComicSansUtil.changeToComicSans(tv, getAssets());

                snackbar.show();
            }
            // No explanation needed, we can request the permission.
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL);
            }
        }
    }

    private void setupOptionsElements()
    {
        editText.clearFocus();

        TextWatcher watcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                final TextView currentText = getLastTouched().getTextView();
                if (currentText != null) {
                    currentText.setText(editable.toString());
                    currentText.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            currentText.requestLayout();
                        }
                    });
                }
            }
        };

        editText.addTextChangedListener(watcher);
        ComicSansUtil.changeToComicSans(editText, getAssets());

        //========== Init Text Size Seek Bar ==========
        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                if (getLastTouched() != null)
                {
                    final TextView textView = getLastTouched().getTextView();
                    textView.setTextSize(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        //========== Init Text Rotation Seek Bar ==========
        rotateTextSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                if (getLastTouched() != null)
                {
                    getLastTouched().setRotation(progress - 180);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        //========== Style Checkboxes ==========
        ComicSansUtil.changeToComicSansBold(bold, getAssets());
        ComicSansUtil.changeToComicSansItalic(italic, getAssets());

        bold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (getLastTouched() != null)
                {
                    if (isChecked && !italic.isChecked())
                        ComicSansUtil.changeToComicSansBold(getLastTouched(), getAssets());
                    else if (isChecked && italic.isChecked())
                        ComicSansUtil.changeToComicSansBoldItalics(getLastTouched(), getAssets());
                    else if (!isChecked && italic.isChecked())
                        ComicSansUtil.changeToComicSansItalics(getLastTouched(), getAssets());
                    else
                        ComicSansUtil.changeToComicSans(getLastTouched(), getAssets());
                }
            }
        });

        italic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (getLastTouched() != null)
                {
                    if (isChecked && !bold.isChecked())
                        ComicSansUtil.changeToComicSansItalics(getLastTouched(), getAssets());
                    else if (isChecked && bold.isChecked())
                        ComicSansUtil.changeToComicSansBoldItalics(getLastTouched(), getAssets());
                    else if (!isChecked && bold.isChecked())
                        ComicSansUtil.changeToComicSansBold(getLastTouched(), getAssets());
                    else
                        ComicSansUtil.changeToComicSans(getLastTouched(), getAssets());
                }
            }
        });
    }

    private void shareBitmapAsJPG(Bitmap bitmap, String tempFileDirectory, String tempFileName)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");

        Util.externalSaveJPGFromBitmap(bitmap, tempFileDirectory, tempFileName,
                getContentResolver(), true);

        File file = new File(Environment.getExternalStorageDirectory() + "/" + tempFileDirectory
                + "/" + tempFileName);
        Uri uri = Uri.fromFile(file);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private String getRandomText()
    {
        Random random = new Random();
        int randomIndex = random.nextInt(randomWords.length);

        return randomWords[randomIndex];
    }

    private TextLayout getLastTouched()
    {
        return lastTouched;
    }

    private void setLastTouched(TextLayout textLayout)
    {
        lastTouched = textLayout;
    }

    private void deleteTextLayout()
    {
        TextLayout textLayout = getLastTouched();

        if (textLayout == null)
        {
            return;
        }

        // Remove from list
        textList.remove(textLayout);

        // Remove from layout
        squareLayout.removeView(textLayout.getBigDaddy());

        // Highlight first textlayout if exists
        if (textList.size() != 0)
        {
            setLastTouched(textList.get(0));
            getLastTouched().setFocus(true);

            // Set seekbars n stuff
            updateOptions(getLastTouched());
        }
        else
        {
            hideOptions();
        }
    }

    private void deleteDialog()
    {
        // Vibrate
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(75);

        // Text view
        TextView title = new TextView(this);
        title.setText("\n    Delete \"" + getLastTouched().getTextView().getText() + "\"?");
        ComicSansUtil.changeToComicSansBold(title, getAssets());
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                deleteTextLayout();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // Buttons to comic sans
        AlertDialog alertDialog = builder.show();
        Button okayButton   = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        ComicSansUtil.changeToComicSans(okayButton, getAssets());
        ComicSansUtil.changeToComicSans(cancelButton, getAssets());
    }

    private void backDialog()
    {
        // Text view
        TextView title = new TextView(this);
        title.setText("\n    Exit editor?");
        ComicSansUtil.changeToComicSansBold(title, getAssets());
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                back();
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.show();
        Button okayButton   = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        ComicSansUtil.changeToComicSans(okayButton, getAssets());
        ComicSansUtil.changeToComicSans(cancelButton, getAssets());
    }

    private void back()
    {
        super.onBackPressed();
    }

    private void updateOptions(TextLayout textLayout)
    {
        if (textLayout == null)
            return;

        TextView textView = textLayout.getTextView();

        // Set seekbars n stuff
        editText.setText(textView.getText());
        editText.setSelection(textView.getText().length());
        textSizeSeekBar.setProgress((int) Util.pxToDp(textView.getTextSize(),
                getApplicationContext()));
        rotateTextSeekBar.setProgress((int) textLayout.getRotation() + 180);

        //Check box stuff
        int style = textLayout.getStyle();

        if(style == Typeface.NORMAL)
        {
            bold.setChecked(false);
            italic.setChecked(false);
        }
        else if(style == Typeface.BOLD)
        {
            bold.setChecked(true);
            italic.setChecked(false);
        }
        else if(style == Typeface.ITALIC)
        {
            bold.setChecked(false);
            italic.setChecked(true);
        }
        else
        {
            bold.setChecked(true);
            italic.setChecked(true);
        }
    }

    private void showOptions()
    {
        final RelativeLayout optionsLayout = (RelativeLayout)
                findViewById(R.id.text_editor_options_layout);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        // Setup slide up animations
//        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
//
//        TranslateAnimation optionsAnimation = new TranslateAnimation(0, 0,
//                optionsLayout.getHeight(), 0);
//        optionsAnimation.setDuration(animTime);
//
//        TranslateAnimation fabAnimation = new TranslateAnimation(0, 0, 0,
//                -optionsLayout.getHeight() + (fab.getHeight()/2) +
//                        getResources().getDimension(R.dimen.activity_vertical_margin));
//        fabAnimation.setDuration(animTime);
//
//        // Set animation listeners
//        optionsAnimation.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation)
//            {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
//                optionsLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation)
//            {
//
//            }
//        });
//
//        fabAnimation.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation)
//            {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
//                fab.setY(optionsLayout.getY() - (fab.getHeight() / 2));
//
//                //Get rid of flicker
//                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
//                animation.setDuration(1);
//                fab.startAnimation(animation);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation)
//            {
//
//            }
//        });
//
//        optionsLayout.startAnimation(optionsAnimation);
//        fab.startAnimation(fabAnimation);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.anchorGravity = Gravity.TOP | Gravity.END | Gravity.RIGHT;

        optionsLayout.setVisibility(View.VISIBLE);
    }

    private void hideOptions()
    {
        final RelativeLayout optionsLayout = (RelativeLayout)
                findViewById(R.id.text_editor_options_layout);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        // Setup slide down animations
//        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
//
//
//        TranslateAnimation optionsAnimation = new TranslateAnimation(0, 0, 0,
//                optionsLayout.getHeight());
//        optionsAnimation.setDuration(animTime);
//
//        TranslateAnimation fabAnimation = new TranslateAnimation(0,0,0,
//                optionsLayout.getHeight() - (fab.getHeight()/2) -
//                        getResources().getDimension(R.dimen.activity_vertical_margin));
//        fabAnimation.setDuration(animTime);
//
//        optionsAnimation.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation)
//            {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
//                optionsLayout.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation)
//            {
//
//            }
//        });
//
//        fabAnimation.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation)
//            {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
////                // Set anchor
////                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams)
////                        fab.getLayoutParams();
////
////                p.anchorGravity = Gravity.BOTTOM | Gravity.END | Gravity.RIGHT;
//                fab.setY(fab.getY() + optionsLayout.getHeight() - fab.getHeight()/2 -
//                        getResources().getDimension(R.dimen.activity_vertical_margin));
//
//                //Get rid of flicker
//                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
//                animation.setDuration(1);
//                fab.startAnimation(animation);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation)
//            {
//
//            }
//        });

        // Start animations
//        fab.requestLayout();
//        optionsLayout.startAnimation(optionsAnimation);
//        fab.startAnimation(fabAnimation);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.anchorGravity = Gravity.BOTTOM | Gravity.END | Gravity.RIGHT;

        optionsLayout.setVisibility(View.INVISIBLE);
    }

    /***********************************************************************************************
    * Button functions
    ***********************************************************************************************/

    public void addTextView(View view)
    {
        // Setup new textLayout
        final TextLayout textLayout = new TextLayout(getApplicationContext());
        textLayout.setFocus(true);
        textList.add(textLayout);

        if(lastTouched != null)
        {
            lastTouched.setFocus(false);
        }

        lastTouched = textLayout;

        textLayout.getTextView().setTextColor(getResources().getColor(R.color.white));
        textLayout.getTextView().setText(getRandomText());
        textLayout.getTextView().setTextSize(24);
        ComicSansUtil.changeToComicSans(textLayout, getAssets());

        // Add textLayout
        squareLayout.addView(textLayout.getBigDaddy());

        updateOptions(textLayout);

        // Show options
        if(textList.size() == 1)
        {
            tabSelected(findViewById(R.id.text_editor_text_tab));
            showOptions();
        }

        //Set listener for delete icon
        ImageView delete = textLayout.getDeleteIcon();

        delete.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    deleteDialog();
                }

                return true;
            }
        });

        textLayout.getTextView().setOnTouchListener(new View.OnTouchListener()
        {
            float prevRawX, prevRawY;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    TextLayout lastTouched = getLastTouched();

                    if (lastTouched != null)
                    {
                        lastTouched.setFocus(false);
                    }

                    setLastTouched(textLayout);
                    textLayout.setFocus(true);

                    updateOptions(textLayout);

                    prevRawX = event.getRawX();
                    prevRawY = event.getRawY();
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE &&
                        textLayout == getLastTouched())
                {
                    float touchX = event.getRawX();
                    float touchY = event.getRawY();
                    float xDiff = touchX - prevRawX;
                    float yDiff = touchY - prevRawY;

                    prevRawX = touchX;
                    prevRawY = touchY;

                    textLayout.setX(textLayout.getX() + xDiff);
                    textLayout.setY(textLayout.getY() + yDiff);
                }

                return true;
            }
        });
    }

    public void setTextViewColor(View view)
    {
        if(getLastTouched() == null)
            return;

        Button color = (Button) view;
        ColorDrawable buttonColor = (ColorDrawable) color.getBackground();

        if(getLastTouched() != null)
        {
            getLastTouched().getTextView().setTextColor(buttonColor.getColor());
        }
    }

    public void tabSelected(View view)
    {
        // Tabs
        RelativeLayout textTab = (RelativeLayout) findViewById(R.id.text_editor_text_tab);
        RelativeLayout sizeRotateTab = (RelativeLayout)
                findViewById(R.id.text_editor_size_rotate_tab);

        // Tab accents
        RelativeLayout textAccent = (RelativeLayout) findViewById(R.id.text_options_tab_accent);
        RelativeLayout sizeRotateAccent = (RelativeLayout)
                findViewById(R.id.size_rotate_tab_accent);
        RelativeLayout colorAccent = (RelativeLayout) findViewById(R.id.color_tab_accent);

        // Options layouts
        RelativeLayout textLayout = (RelativeLayout)
                findViewById(R.id.text_editor_text_options_layout);
        RelativeLayout sizeRotateLayout = (RelativeLayout)
                findViewById(R.id.text_editor_size_rotate_options_layout);
        RelativeLayout colorLayout = (RelativeLayout)
                findViewById(R.id.text_editor_color_options_layout);

        // Text
        if (view == textTab)
        {
            sizeRotateLayout.setVisibility(View.GONE);
            colorLayout.setVisibility(View.GONE);
            textLayout.setVisibility(View.VISIBLE);

            sizeRotateAccent.setVisibility(View.GONE);
            colorAccent.setVisibility(View.GONE);
            textAccent.setVisibility(View.VISIBLE);
        }
        // Size/Rotate
        else if(view == sizeRotateTab)
        {
            colorLayout.setVisibility(View.GONE);
            textLayout.setVisibility(View.GONE);
            sizeRotateLayout.setVisibility(View.VISIBLE);

            colorAccent.setVisibility(View.GONE);
            textAccent.setVisibility(View.GONE);
            sizeRotateAccent.setVisibility(View.VISIBLE);
        }
        // Color
        else
        {
            sizeRotateLayout.setVisibility(View.GONE);
            textLayout.setVisibility(View.GONE);
            colorLayout.setVisibility(View.VISIBLE);

            sizeRotateAccent.setVisibility(View.GONE);
            textAccent.setVisibility(View.GONE);
            colorAccent.setVisibility(View.VISIBLE);
        }
    }
}
