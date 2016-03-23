package org.soletluna.amazepicture.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.soletluna.amazepicture.R;
import org.soletluna.amazepicture.SquareLayout;
import org.soletluna.amazepicture.TouchImageView;
import org.soletluna.amazepicture.util.ComicSansUtil;
import org.soletluna.amazepicture.util.Util;

import java.io.IOException;

public class ImageAdjustmentsActivity extends AppCompatActivity
{
    private TouchImageView mainImage = null;
    private SquareLayout squareLayout = null;

    private static String TEMP = "temp.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_adjustment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set toolbar font
        TextView toolbarText = (TextView) toolbar.findViewById(R.id.toolbar_text);
        ComicSansUtil.changeToComicSans(toolbarText, getAssets());

        squareLayout = (SquareLayout) findViewById(R.id.image_adjustments_square_layout);
        mainImage = (TouchImageView) findViewById(R.id.image_view);

        // Get intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(intent.hasExtra(SelectImageActivity.IMAGE_EXTRA))
        {
            Uri imageUri = (Uri) intent.getParcelableExtra(SelectImageActivity.IMAGE_EXTRA);
            setTouchImageFromUri(imageUri);
        }
        else if(Intent.ACTION_SEND.equals(action) && type != null)
        {
            if(type.startsWith("image/"))
            {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                setTouchImageFromUri(imageUri);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_adjustments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_continue)
        {
            // Continue to Text Editor
            Bitmap bitmap = Util.layoutToBitmap(squareLayout);
            Util.internalSaveJPGFromBitmap(bitmap, TEMP, getApplicationContext());

            Intent intent = new Intent(this, TextEditorActivity.class);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void rotateImage(View view)
    {
        mainImage.setRotation(mainImage.getRotation() + 90);
    }

    private void setTouchImageFromUri(Uri uri)
    {
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            mainImage.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
