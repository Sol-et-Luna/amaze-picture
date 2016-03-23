package org.soletluna.amazepicture.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by poo on 11/1/2015.
 */
public class Util
{
    public static Bitmap layoutToBitmap (ViewGroup layout)
    {
        //Setup to get bitmap
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();

        //Get layout bitmap
        Bitmap bitmap = layout.getDrawingCache();

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                bitmap.getConfig());

        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap, layout.getMatrix(), null);

        return overlayBitmap;
    }

    public static void externalSaveJPGFromBitmap(Bitmap bitmap, String externalDir, String fileName,
                                                 ContentResolver contentResolver, boolean refreshGallery)
    {
        // Create dir if necessary
        String dirString = Environment.getExternalStorageDirectory() + "/" + externalDir;
        File dir = new File(dirString);

        if(!dir.exists())
        {
            dir.mkdir();
        }

        File file = new File(dirString + "/" + fileName);

        try
        {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));

            //Add metadata
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, dirString + "/" + fileName);

            //Image shows up in gallery
            if(refreshGallery)
            {
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void internalSaveJPGFromBitmap(Bitmap bitmap, String fileName, Context context)
    {
        File file = new File(context.getFilesDir().toString() + "/" + fileName);

        try
        {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static String getCurrentTimeJPGString()
    {
        return String.valueOf(System.currentTimeMillis()/1000) + ".jpg";
    }

    public static float pxToDp(float px, Context context)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPx(float dp, Context context)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
