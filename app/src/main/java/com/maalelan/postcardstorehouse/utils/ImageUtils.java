package com.maalelan.postcardstorehouse.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for handling image saving operations.
 * Provide functionality to store images to the device´s gallery
 * in both Android Q+ using MediaStore and older Android versions using file I/O.
 */
public class ImageUtils {

    private static final String IMAGE_FOLDER_NAME = "PostcardStoreHouse";

    /**
     * Saves a Bitmap image to the public Pictures directory under the PostcardStorehouse folder.
     * Notifies the media scanner to update the gallery.
     *
     * @param context Application context
     * @param imageBitmap Bitmap to save
     * @return A string representing the img URI (for Android Q+) or absolute file path
     * or absolute file path (for legacy Android), or null if saving failed.
     */
    public static String saveImageToGallery(Context context, Bitmap imageBitmap) {
        String imageName = generateUniqueImageName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped storage approach ( Android 10+)
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + IMAGE_FOLDER_NAME);

            Uri uri = null;

            try {
                uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri == null) throw new IOException("Failed to create new Mediastore record.");

                try (FileOutputStream out = (FileOutputStream) context.getContentResolver().openOutputStream(uri)){
                    if (out == null) throw new IOException("Failed to get output stream.");
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                }

                Toast.makeText(context, "Kuva tallennettu galleriaan", Toast.LENGTH_SHORT).show();
                return uri.toString();
            } catch (Exception e) {
                if (uri != null) {
                    // Clean up incomplete entry
                    context.getContentResolver().delete(uri, null, null);
                }
                e.printStackTrace();
                Toast.makeText(context, "Kuvan tallennus epäonnistui", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            // Pre-Android 10: use direct external storage
            File storageDir = getOrCreateImageDirectory();
            if (storageDir == null) {
                Toast.makeText(context, "Kansiota ei voitu luoda", Toast.LENGTH_SHORT).show();
                return null;
            }

            File imageFile = new File(storageDir, imageName);

            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();

                // Notify media scanner to index the new image
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(imageFile));
                context.sendBroadcast(mediaScanIntent);

                Toast.makeText(context, "Kuvatallennus galleriaan onnistui", Toast.LENGTH_SHORT).show();

                return imageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Kuvan tallennus epäonnistui", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }

    /**
     * Returns the storage directory, creating if necessary.
     *
     * @return A File pointing to the image directory, or null if creation failed
     */
    private static File getOrCreateImageDirectory() {
        File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageDir = new File(baseDir, IMAGE_FOLDER_NAME);

        if (!imageDir.exists()) {
            boolean created = imageDir.mkdirs();
            if (!created) return null;
        }

        return imageDir;
    }

    /**
     * Generates a unique image file name using current timestamp.
     *
     * @return A string formatted as "IMGyyyyMMdd_HHmmss_SSS.jpg"
     */
    private static String generateUniqueImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(new Date());
        return "IMG" + timeStamp + ".jpg";
    }
}
