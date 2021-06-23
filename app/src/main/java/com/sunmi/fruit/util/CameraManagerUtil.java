package com.sunmi.fruit.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.sunmi.fruit.MyApp;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static android.content.Context.CAMERA_SERVICE;
import static android.os.Looper.getMainLooper;

public class CameraManagerUtil implements TextureView.SurfaceTextureListener {

    private final static String TAG = CameraManagerUtil.class.getSimpleName();
    private static CameraManagerUtil cameraManagerUtil = new CameraManagerUtil();

    public static CameraManagerUtil getInstance() {
        return cameraManagerUtil;
    }

    CameraManager cameraManager;
    CameraDevice mCameraDevice;
    Handler mainHandler;
    CaptureRequest.Builder captureRequestBuilder;
    ImageReader mImageReader;
    CameraCaptureSession mCameraCaptureSession;
    AutoFitTextureView mTexture;
    CameraResults gameraResult;

    public void init(AutoFitTextureView mTexture) {

        this.mTexture=mTexture;
        this.mTexture.setSurfaceTextureListener(this);
        cameraManager =(CameraManager) MyApp.mApp.getSystemService(CAMERA_SERVICE);
        mainHandler = new Handler(getMainLooper());
        mImageReader = ImageReader.newInstance(mTexture.getWidth(), mTexture.getHeight(), ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                // 拿到拍照照片数据

                try {
                    Image image = imageReader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);//由缓冲区存入字节数组
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (bitmap != null) {
//                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                        String fileName = "IMG_"+timeStamp+".jpg";
//                        File filePic = new File(FileUtil.INSTANCE.getRootFolderPath() + File.separator + fileName);
//                        if (!filePic.exists()) {
//                            filePic.getParentFile().mkdirs();
//                            filePic.createNewFile();
//                        }
//                        FileOutputStream fos = new FileOutputStream(filePic);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                        fos.flush();
//                        fos.close();

                        String s=Utils.bitmapToBase64(bitmap);
                        LogSunmi.e(TAG, "s="+"请求");
                        HttpUtil.getInstance().post(s, new HttpUtil.HttpResults() {
                            @Override
                            public void onResponse(String name) {
                                if(gameraResult!=null){
                                    gameraResult.getResult(name);
                                }
                            }
                            @Override
                            public void onFailure() {
                                LogSunmi.e(TAG, "s="+"请求onFailure");
                            }
                        });

                    }
                    image.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, mainHandler);

        if (ActivityCompat.checkSelfPermission(MyApp.mApp, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            cameraManager.openCamera(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT), new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    LogSunmi.e(TAG, "onOpened");
                    mCameraDevice = cameraDevice;
                    afterOpenCamera();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    LogSunmi.e(TAG, "onDisconnected");
                    mCameraDevice.close();

                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int i) {
                    Log.e(TAG, cameraDevice.toString() + i);
                }
            }, mainHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        init(mTexture);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        LogSunmi.e(TAG, "onSurfaceTextureSizeChanged" + i + "  ==" + i1);

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        LogSunmi.e(TAG, "onSurfaceTextureDestroyed");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    //打开摄像头之后要做的事情
    public void afterOpenCamera() {
        SurfaceTexture surfaceTexture = mTexture.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mTexture.getWidth(), mTexture.getHeight());
        Surface surface = new Surface(surfaceTexture);
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        captureRequestBuilder.addTarget(surface);
        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        mCameraCaptureSession = cameraCaptureSession;
                        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {

                        }, mainHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    LogSunmi.e(TAG, "onConfigureFailed");
                }
            }, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    //拍照
    public void takePicture() {
        takePicture(null);
    }
    public void takePicture(CameraResults gameraResult) {
        this.gameraResult = gameraResult;
        if(captureRequestBuilder==null){
            LogSunmi.e(TAG, "captureRequestBuilder==null");
            return;
        }
        captureRequestBuilder.addTarget(mImageReader.getSurface());
        try {
            mCameraCaptureSession.capture(captureRequestBuilder.build(), null, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void releaseCamera() {
        if (mCameraCaptureSession != null)
            mCameraCaptureSession.close();
        mCameraCaptureSession = null;
        if (mCameraDevice != null)
            mCameraDevice.close();
        mCameraDevice = null;
        if (mImageReader != null)
            mImageReader.close();
        mImageReader = null;
//        mTexture=null;

    }

    public interface CameraResults{
        void getResult(String pic);
    }
}