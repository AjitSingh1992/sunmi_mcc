package com.easyfoodvone.controller.child;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.easyfoodvone.R;
import com.easyfoodvone.app_common.separation.ObservableField;
import com.easyfoodvone.app_common.viewdata.DataPageNewOffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ControllerNewOfferSelectImage {

    private static final int PICK_IMAGE_CAMERA = 223;
    private static final int PICK_IMAGE_GALLERY = 623;

    public final DataPageNewOffer.ImagePickerData data;

    public ControllerNewOfferSelectImage(DataPageNewOffer.ImagePickerData.InputEvents inputEventHandler) {
        this.data = new DataPageNewOffer.ImagePickerData(outputEvents, inputEventHandler, new ObservableField<>(null));
    }

    public @NonNull String base64EncodeImage() {
        @Nullable Bitmap bitmap = data.getPickedImage().get();

        if (bitmap == null) {
            return "";

        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }
    }

    private final DataPageNewOffer.ImagePickerData.OutputEvents outputEvents
            = new DataPageNewOffer.ImagePickerData.OutputEvents() {

        @Override
        public void onClickSelectImage(@NonNull FragmentActivity activity) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                selectImage(activity);
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE },
                        100);
            }
        }

        @Override
        public void onActivityResult(@NonNull FragmentActivity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == PICK_IMAGE_CAMERA) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File destination = new File(Environment.getExternalStorageDirectory() + "/" +
                            activity.getResources().getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ControllerNewOfferSelectImage.this.data.getPickedImage().set(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == PICK_IMAGE_GALLERY) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);

                            ControllerNewOfferSelectImage.this.data.getPickedImage().set(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private final DialogOutputEvents typePickerOutputEvents = new DialogOutputEvents() {
        @Override
        public void onClickTakePhoto() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            data.getInputEvents().startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }

        @Override
        public void onClickChooseFromGallery() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            data.getInputEvents().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
        }
    };

    private void selectImage(@NonNull FragmentActivity activity) {
        try {
            PackageManager pm = activity.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, activity.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                showSelectImageDialog(typePickerOutputEvents, activity);
            } else {
                Toast.makeText(activity, "Camera Permission error", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Camera Permission error", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO move this into the view now it has no other embedded code
    private void showSelectImageDialog(DialogOutputEvents outputEvents, @NonNull FragmentActivity activity) {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    outputEvents.onClickTakePhoto();
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    outputEvents.onClickChooseFromGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    interface DialogOutputEvents {
        void onClickTakePhoto();
        void onClickChooseFromGallery();
    }
}
