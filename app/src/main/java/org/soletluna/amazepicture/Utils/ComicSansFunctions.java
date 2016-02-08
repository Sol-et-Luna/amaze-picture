package org.soletluna.amazepicture.Utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.soletluna.amazepicture.TextLayout;

/**
 * Created by David on 9/17/2014.
 */
public class ComicSansFunctions
{
    public static void changeToComicSans(TextLayout textLayout, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textLayout.getTextView().setTypeface(typeface, Typeface.NORMAL);

        textLayout.setStyle(Typeface.NORMAL);
    }

    public static void changeToComicSans(TextView textView, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textView.setTypeface(typeface, Typeface.NORMAL);
    }

    public static void changeToComicSansBold(TextView textView, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textView.setTypeface(typeface, Typeface.BOLD);
    }

    public static void changeToComicSans(EditText textView, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textView.setTypeface(typeface);
    }

    public static void changeToComicSans(CheckBox checkBox, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        checkBox.setTypeface(typeface);
    }

    public static void changeToComicSansBold(CheckBox checkBox, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        checkBox.setTypeface(typeface, Typeface.BOLD);
    }

    public static void changeToComicSansItalic(CheckBox checkBox, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        checkBox.setTypeface(typeface, Typeface.ITALIC);
    }

    public static void changeToComicSansBold(TextLayout textLayout, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textLayout.getTextView().setTypeface(typeface, Typeface.BOLD);

        textLayout.setStyle(Typeface.BOLD);
    }

    public static void changeToComicSansItalics(TextLayout textLayout, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textLayout.getTextView().setTypeface(typeface, Typeface.ITALIC);

        textLayout.setStyle(Typeface.ITALIC);
    }

    public static void changeToComicSansBoldItalics(TextLayout textLayout, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        textLayout.getTextView().setTypeface(typeface, Typeface.BOLD_ITALIC);

        textLayout.setStyle(Typeface.BOLD_ITALIC);
    }

    public static void changeToComicSans(Button button, AssetManager asset)
    {
        Typeface typeface = Typeface.createFromAsset
                (asset, "fonts/comicsans.ttf");
        button.setTypeface(typeface, Typeface.NORMAL);
    }
}
