package com.rnLadPrintPdf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class PdfDocumentAdapter extends PrintDocumentAdapter {

    Context context ;
    Uri pathName ;
    byte[] data ;
    public PdfDocumentAdapter(Context ctxt, Uri pathName) {
        context = ctxt;
        this.pathName = pathName;
    }
    public PdfDocumentAdapter(Context ctxt, String pathName) {
        context = ctxt;
        this.pathName = Uri.fromFile(new File(pathName));
    }
    public PdfDocumentAdapter(Context ctxt,byte[] data) {
        context = ctxt;
        this.data =data;
    }
    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
        }
        else {
            PrintDocumentInfo.Builder builder=
                    new PrintDocumentInfo.Builder(" file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
            layoutResultCallback.onLayoutFinished(builder.build(),
                    !printAttributes1.equals(printAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in=null;
        OutputStream out=null;
        try {
            if (this.data.length > 0){
                in = new ByteArrayInputStream(this.data);
            }else{
                in =  context.getContentResolver().openInputStream(pathName);
            }


            out=new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            byte[] buf=new byte[16384];
            int size;

            while ((size=in.read(buf)) >= 0
                    && !cancellationSignal.isCanceled()) {
                out.write(buf, 0, size);
            }

            if (cancellationSignal.isCanceled()) {
                writeResultCallback.onWriteCancelled();
            }
            else {
                writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
            }
        }
        catch (Exception e) {
            writeResultCallback.onWriteFailed(e.getMessage());
            Log.e(context.getPackageName(),e.getMessage());
        }
        finally {
            try {
                if(in != null){
                    in.close();
                }

                if(out != null){
                    out.close();
                }
            }
            catch (IOException e) {
                Log.e(context.getPackageName(),e.getMessage());
            }
        }
    }}