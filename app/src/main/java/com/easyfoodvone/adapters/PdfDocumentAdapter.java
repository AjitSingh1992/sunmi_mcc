package com.easyfoodvone.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends ThreadedPrintDocumentAdapter {
    Context context = null;
    static String fileName;



    public PdfDocumentAdapter(Context ctxt, String fileName) {
        super(ctxt);
        this.fileName = fileName;


    }

    @Override
    LayoutJob buildLayoutJob(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
        return (new PdfLayoutJob(oldAttributes, newAttributes,
                cancellationSignal, callback, extras));
    }

    @Override
    WriteJob buildWriteJob(PageRange[] pages,
                           ParcelFileDescriptor destination,
                           CancellationSignal cancellationSignal,
                           PrintDocumentAdapter.WriteResultCallback callback, Context ctxt) {
        return (new PdfWriteJob(pages, destination, cancellationSignal,
                callback, ctxt));
    }

    private static class PdfLayoutJob extends LayoutJob {
        PdfLayoutJob(PrintAttributes oldAttributes,
                     PrintAttributes newAttributes,
                     CancellationSignal cancellationSignal,
                     PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
            super(oldAttributes, newAttributes, cancellationSignal, callback,
                    extras);


        }

        @Override
        public void run() {

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
            } else {
                PrintDocumentInfo.Builder builder =
                        new PrintDocumentInfo.Builder(fileName);

                builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                        .build();

                callback.onLayoutFinished(builder.build(),
                        !newAttributes.equals(oldAttributes));
            }
        }
    }

    private static class PdfWriteJob extends WriteJob {
        PdfWriteJob(PageRange[] pages, ParcelFileDescriptor destination,
                    CancellationSignal cancellationSignal,
                    PrintDocumentAdapter.WriteResultCallback callback, Context ctxt) {
            super(pages, destination, cancellationSignal, callback, ctxt);
        }

        @Override
        public void run() {
            InputStream in = null;
            OutputStream out = null;

            try {
                String path ="/sdcard/Android/data/com.easyfoodvone/files/";

                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();

                File data = new File(dir, fileName);

                in = new FileInputStream(data);
                out = new FileOutputStream(destination.getFileDescriptor());

                byte[] buf = new byte[16384];
                int size;

                while ((size = in.read(buf)) >= 0
                        && !cancellationSignal.isCanceled()) {
                    out.write(buf, 0, size);
                }

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                } else {
                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                }
            } catch (Exception e) {
                callback.onWriteFailed(e.getMessage());
                Log.e(getClass().getSimpleName(), "Exception printing PDF", e);
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    Log.e(getClass().getSimpleName(),
                            "Exception cleaning up from printing PDF", e);
                }
            }
        }
    }
}
   /* PrintDocumentAdapter {

    Context context = null;
    String pathName = "";
    String fileName = "";

    public PdfDocumentAdapter(Context ctxt, String pathName, String fileName) {
        context = ctxt;
        this.pathName = pathName;
        this.fileName = fileName;
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
        } else {
            PrintDocumentInfo.Builder builder =
                    new PrintDocumentInfo.Builder(fileName);
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
            layoutResultCallback.onLayoutFinished(builder.build(),
                    !printAttributes1.equals(printAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in = null;
        OutputStream out = null;
        try {
//            File dir = new File(Environment.getExternalStorageDirectory(), "Download");
//            File file = new File(dir, "Test.pdf");

            String path = "/sdcard/Android/data/com.easyfoodvone/files/";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File data = new File(dir, fileName + ".pdf");


            *//*String path = Environment.getExternalStorageDirectory() + "/ANTI/";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File data = new File(dir, "AntiPollution.pdf");*//*


            // File file = new File(pathName);
            in = new FileInputStream(data);
            out = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            byte[] buf = new byte[16384];
            int size;

            while ((size = in.read(buf)) >= 0
                    && !cancellationSignal.isCanceled()) {
                out.write(buf, 0, size);
            }

            if (cancellationSignal.isCanceled()) {
                writeResultCallback.onWriteCancelled();
            } else {
                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        } catch (Exception e) {
            writeResultCallback.onWriteFailed(e.getMessage());
            Log.e("Exception", e.toString());
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                Log.e("IOException", e.toString());
            }
        }
    }
}
*/