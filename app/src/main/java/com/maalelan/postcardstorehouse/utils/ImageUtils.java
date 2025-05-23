package com.maalelan.postcardstorehouse.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    private static final String IMAGE_FOLDER_NAME = "PostcardStoreHouse";

    /**
     * Saves a Bitmap image to the public Pictures directory under the PostcardStorehouse folder.
     * Notifies the media scanner to update the gallery.
     *
     * @param context Application context
     * @param imageBitmap Bitmap to save
     * @return Absolute path to the saved image or null if failed
     */
    public static String saveImageToGallery(Context context, Bitmap imageBitmap) {
        File storageDir = getOrCreateImageDirectory();
        if (storageDir == null) {
            Toast.makeText(context, "Kansiota ei voitu luoda", Toast.LENGTH_SHORT).show();
            return null;
        }

        String imageName = generateUniqueImageName();
        File imageFile = new File(storageDir, imageName);

        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();

            // Notify gallery
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

    /**
     * Returns the storage directory, creating if necessary.
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
     * Generates a unique file name using current timestamp.
     */
    private static String generateUniqueImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(new Date());
        return "IMG" + timeStamp + ".jpg";
    }
}
