package com.escort.carriage.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.escort.carriage.android.entity.DataBinding;
import com.escort.carriage.android.entity.ItemBean;
import com.escort.carriage.android.utils.FileUtils;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  签名画板
 */
public class SignatureActivity extends ProjectBaseActivity {

    @BindView(R.id.backBt)
    Button backBtn;
    @BindView(R.id.clearBt)
    Button clearBtn;
    @BindView(R.id.saveBt)
    Button saveBtn;
    @BindView(R.id.signaturePad)
    SignaturePad signaturePad;

    private ItemBean itemBean;
    public static final String TAG = SignatureActivity.class.getSimpleName();
    DataBinding dataBinding = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_pannel);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {

    }

    @OnClick({R.id.backBt, R.id.clearBt, R.id.saveBt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backBt://返回
                    finish();
                    break;
                case R.id.clearBt://清除
                    signaturePad.clear();
                    break;
            case R.id.saveBt://保存
                Bitmap bitmap  =  signaturePad.getSignatureBitmap();
                if(null!=bitmap){
                   int atate  =  FileUtils.saveImageToGallery(bitmap,this);
                   Log.e("FileUtils>>>","===atate==>>"+atate);
                }
                  break;
        }
    }



//    public class SignatureViewModel{
//        public void clickEvent(View v) {
//            switch (v.getId()){
//                case R.id.backBt://返回
//                    finish();
//                    break;
//                case R.id.clearBt://清除
//                    //dataBinding.signaturePad.clear();
//                    break;
//                case R.id.saveBt://保存
////                    Bitmap signatureBitmap = dataBinding.signaturePad.getSignatureBitmap();
////                    if (addJpgSignatureToGallery(signatureBitmap)) {
////                        ToastUtil.showShort(SignatureActivity.this,"保存成功！");
////                        finish();
////                    } else {
////                        ToastUtil.showShort(SignatureActivity.this,"保存失败！");
////                        finish();
////                    }
//                    break;
//            }
//        }
//    }






    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }



//    public boolean addJpgSignatureToGallery(Bitmap signature) {
//        boolean result = false;
//        try {
//            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
//            saveBitmapToJPG(signature, photo);
//            itemBean.setValue(photo.getPath());//签名图片保存地址  photo.getPath()
//            EventBus.getDefault().post(new MsgEvent(MsgEvent.SIGNATURE,itemBean));//EventBus发送签名图片保存地址  photo.getPath()
//            result = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }


    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

}
