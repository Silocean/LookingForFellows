package com.hblg.lookingfellow.pla.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class FlowView extends ImageView implements View.OnClickListener, View.OnLongClickListener {

    public Bitmap bitmap;
    private int columnIndex;
    private String fileName;
    private int ItemWidth;
    private Handler viewHandler;
    private String _url;

    public FlowView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        Init();
    }

    public FlowView(Context c, AttributeSet attrs) {
        super(c, attrs);
        Init();
    }

    public FlowView(Context c) {
        super(c);
        Init();
    }

    private void Init() {

        setOnClickListener(this);
        setOnLongClickListener(this);
        setAdjustViewBounds(true);

    }

    @Override
    public boolean onLongClick(View v) {
        Log.d("FlowView", "LongClick");
        return true;
    }

    @Override
    public void onClick(View v) {
        Log.d("FlowView", "Click");
    }

    /**
     * 鍥炴敹鍐呭瓨
     */
    public void recycle() {
        setImageBitmap(null);
        if ((this.bitmap == null) || (this.bitmap.isRecycled()))
            return;
        this.bitmap.recycle();
        this.bitmap = null;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getItemWidth() {
        return ItemWidth;
    }

    public void setItemWidth(int itemWidth) {
        ItemWidth = itemWidth;
    }

    public Handler getViewHandler() {
        return viewHandler;
    }

    public FlowView setViewHandler(Handler viewHandler) {
        this.viewHandler = viewHandler;
        return this;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

}
