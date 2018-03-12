package vgroup.demoweek4.camera.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import vgroup.demoweek4.camera.Photo;
import vgroup.demoweek4.camera.RequestView;

public class PhotoHandler implements PictureCallback {

    public static Uri imageUri;
    private final Context context;
    public File pictureFile;
    RequestView requestView;
    private ImageView imageView;


    public PhotoHandler(Context context) {
        this.context = context;

    }

    public Bitmap rotateImage(int angle, byte[] data) {
        //requestView= (RequestView) context;
        Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);

//        Matrix mat = new Matrix();
//        mat.postRotate("angle");  // angle is the desired angle you wish to rotate
//        storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0, storedBitmap.getWidth(), storedBitmap.getHeight(), mat, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(storedBitmap, 0, 0, storedBitmap.getWidth(), storedBitmap.getHeight(), matrix, true);


    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFileDir = getDir();
        Realm realm = Realm.getDefaultInstance();
        Bitmap bitmap;
//        bitmap=rotateImage(270,data);
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

//            Log.d(MenuActivity.DEBUG_TAG, "Can't create directory to save image.");
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        final String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
//            Toast.makeText(context, "New Image saved:" + photoFile,
//                    Toast.LENGTH_LONG).show();

//            imageView=context.getResources().
            imageUri = getImageContentUri(context, pictureFile);

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Photo picture = realm.createObject(Photo.class);
                    picture.setName(photoFile);
                    picture.setImageUri(imageUri.toString());
                    picture.setLocation(pictureFile.toString());
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    //Toast.makeText(context, "Image Save Successfull", Toast.LENGTH_SHORT).show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(context, "Realm: Image Save Error", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception error) {
//            Log.d(MakePhotoActivity.DEBUG_TAG, "File" + filename + "not saved: "+ error.getMessage());
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "HolaCamera");
    }


}