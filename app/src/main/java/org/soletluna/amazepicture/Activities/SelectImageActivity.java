package org.soletluna.amazepicture.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.soletluna.amazepicture.R;
import org.soletluna.amazepicture.Utils.ComicSansFunctions;

public class SelectImageActivity extends AppCompatActivity
{
    private static final int SELECT_PICTURE = 1;
    public final static String IMAGE_EXTRA = "IMAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set toolbar font
        TextView toolbarText = (TextView) toolbar.findViewById(R.id.toolbar_text);
        ComicSansFunctions.changeToComicSans(toolbarText, getAssets());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();
                Intent intent = new Intent(this, ImageAdjustmentsActivity.class);
                intent.putExtra(IMAGE_EXTRA, selectedImageUri);
                startActivity(intent);
            }
        }
    }

    public void selectImage(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
}
