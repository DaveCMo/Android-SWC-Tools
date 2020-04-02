package com.swctools.common.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import android.util.Base64;

import com.swctools.R;
import com.swctools.common.enums.Droidekas;
import com.swctools.common.enums.Factions;
import com.swctools.layouts.LayoutHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageHelpers {
    private static final String TAG = "ImageHelpers";

    public static String imageToByteArrayString(Bitmap image, int quality) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return encodedImage;

    }

    public static Bitmap decodeImageByteStrToDrawable(String byteStr) {
        InputStream is = new ByteArrayInputStream(byteStr.getBytes());
        return BitmapFactory.decodeStream(is, null, null);
    }

    public static byte[] bitmaptoByte(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }

    public static Bitmap bytesToBitmap(byte[] b) {

        try {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e) {
            return null;
        }

    }

    public static Bitmap decodeUriToBitmap(Uri selectedImage, Context context, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static Drawable factionIcon(String faction, Context context) {
        Drawable icon;
        if (faction.toLowerCase().equalsIgnoreCase(Factions.REBEL.getFactionName().toLowerCase())) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_rebel_foreground);
            icon.setTint(context.getResources().getColor(R.color.red));
        } else if (faction.toLowerCase().equalsIgnoreCase(Factions.EMPIRE.getFactionName().toLowerCase())) {
            icon = ContextCompat.getDrawable(context, R.mipmap.ic_empire_foreground);
            icon.setTint(context.getResources().getColor(R.color.imperial_blue));
        } else {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_help_outline_black_24dp);
            icon.setTint(context.getResources().getColor(R.color.colorPrimary));
        }
        return icon;
    }

    public static Drawable getFavouriteIcon(String favouriteValue, Context context) {

        try {
            if (favouriteValue.equalsIgnoreCase(LayoutHelper.YES)) {

                return ContextCompat.getDrawable(context, R.drawable.ic_star_solid_24dp);
            } else {
                return ContextCompat.getDrawable(context, R.drawable.ic_star_border_black_24dp);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(context, R.drawable.ic_star_border_black_24dp);
        }
    }

    public static Drawable getDroidPlatform(String droidekas, Context context) {
//        Droidekas droidekas {
//
//        }
        if (droidekas.equalsIgnoreCase(Droidekas.SENTINEL.toString())) {
            return ContextCompat.getDrawable(context, R.mipmap.sentplatform_fg);
        } else if (droidekas.equalsIgnoreCase(Droidekas.OPPRESSOR.toString())) {

            return ContextCompat.getDrawable(context, R.mipmap.sentplatform_fg);
        } else {
            return ContextCompat.getDrawable(context, R.mipmap.opplatform_fg);
        }


    }

    public static Drawable getImageFromStoredId(int id, Context context) {
        return ContextCompat.getDrawable(context, id);
    }

    public static Drawable getPlanetImage(String planet, Context context) {
        Drawable planetImg = null;
        if (planet.equalsIgnoreCase("Tatooine")) {
            planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_tatooine_round);
        } else if (planet.equalsIgnoreCase("Dandoran")) {
            planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_dandoran_round);
        } else if (planet.equalsIgnoreCase("Takodana")) {
            planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_takodana_round);
        } else if (planet.equalsIgnoreCase("Erkit")) {
            planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_erkit_round);
        } else if (planet.equalsIgnoreCase("Hoth")) {
            planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_hoth_round);
        } else if (planet.equalsIgnoreCase("Yavin 4")) {
            planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_yavin4_round);

        }
//        planetImg = ContextCompat.getDrawable(context, R.mipmap.planet_tatooine_round);
        return planetImg;
    }

    public static Drawable getDroidImage(int level, String type, Context context) {
//        Droidekas.OPPRESSOR
        Drawable droidImage = null;
        if (level >= 1 && level < 10) {
            if (type.equalsIgnoreCase(Droidekas.OPPRESSOR.toString())) {
                return ContextCompat.getDrawable(context, R.mipmap.deka_op1_fg);
            } else {
                return ContextCompat.getDrawable(context, R.mipmap.deka_sent1_fg);
            }
        } else if (level >= 10 && level < 20) {
            if (type.equalsIgnoreCase(Droidekas.OPPRESSOR.toString())) {
                return ContextCompat.getDrawable(context, R.mipmap.deka_op10_fg);

            } else {
                return ContextCompat.getDrawable(context, R.mipmap.deka_sent10_fg);
            }
        } else if (level >= 20 && level < 30) {
            if (type.equalsIgnoreCase(Droidekas.OPPRESSOR.toString())) {
                return ContextCompat.getDrawable(context, R.mipmap.deka_op20_fg);

            } else {
                return ContextCompat.getDrawable(context, R.mipmap.deka_sent20_fg);
            }
        } else if (level >= 30 && level < 40) {
            if (type.equalsIgnoreCase(Droidekas.OPPRESSOR.toString())) {
                return ContextCompat.getDrawable(context, R.mipmap.deka_op30_fg);
            } else {
                return ContextCompat.getDrawable(context, R.mipmap.deka_sent30_fg);
            }
        } else {
            if (type.equalsIgnoreCase(Droidekas.OPPRESSOR.toString())) {
                return ContextCompat.getDrawable(context, R.mipmap.deko_op_40_fg);
            } else {
                return ContextCompat.getDrawable(context, R.mipmap.deka_sent_40_fg);
            }
        }
    }
}
