package it.mobistego;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.mobistego.beans.MobiStegoItem;
import it.mobistego.tasks.DecodeTask;
import it.mobistego.tasks.EncodeTask;
import it.mobistego.utils.Constants;

/**
 * <pre>
 * author : zhouyang
 * time   : 2018/11/26
 * desc   :
 * version:
 * </pre>
 */
public class Test {
    private static File mPasswordFile;
    private static File mAppkeyFile;
    private static File mAppSecretFile;

    public static void doTest(Activity activity) {
        doEncode(activity);
    }

    private static void doEncode(Activity activity) {

        try {
            getPath(activity);

            EncodeTask task = new EncodeTask(activity);
            MobiStegoItem item = new MobiStegoItem(TestConfigure.PASSWORD, mPasswordFile, Constants.NO_NAME, false, "");
            task.execute(item);

            Thread.sleep(2000);

            String password = SHA(TestConfigure.PASSWORD);
            String appkey = AESUtils.encrypt(password, TestConfigure.APPKEY);
            String appSecret = AESUtils.encrypt(password, TestConfigure.APPSECRET);

            task = new EncodeTask(activity);
            item = new MobiStegoItem(appkey, mAppkeyFile, Constants.NO_NAME, false, "");
            task.execute(item);

            Thread.sleep(2000);

            task = new EncodeTask(activity);
            item = new MobiStegoItem(appSecret, mAppSecretFile, Constants.NO_NAME, false, "");
            task.execute(item);

//            Thread.sleep(2000);

//            File password = new File(   "/storage/emulated/0/appSec/result/pwd_visible.png");
//            File appkey  = new File(     "/storage/emulated/0/appSec/result/founds_gone.png");
//            File appsecret  = new File(  "/storage/emulated/0/appSec/result/founds_visible.png");
//            DecodeTask taskDecode = new DecodeTask(activity);
//            taskDecode.execute(password);
//            taskDecode.execute(appkey);
//            taskDecode.execute(appsecret);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static String SHA(final String strText) {
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if (!TextUtils.isEmpty(strText)) {
            try {
                // SHA 加密开始
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                byte byteBuffer[] = messageDigest.digest();
                strResult = AESUtils.toHex(byteBuffer);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    private static void getPath(Activity activity) {
//        InputStream password = activity.getClass().getResourceAsStream("/assets/src/pwd_visible.png");
//        InputStream appkey = activity.getClass().getResourceAsStream("/assets/src/founds_gone.png");
//        InputStream appSecret = activity.getClass().getResourceAsStream("/assets/src/founds_visible.png");
//        //若要想要转换成String类型
//
//        try {
//            mPasswordFile = new File(activity.getFilesDir().getAbsolutePath() + "pwd_visible.png");
//            mAppkeyFile = new File(activity.getFilesDir().getAbsolutePath() + "founds_gone.png");
//            mAppSecretFile = new File(activity.getFilesDir().getAbsolutePath() + "founds_visible.png");
//
//            inputstreamtofile(password, mPasswordFile);
//            inputstreamtofile(appkey, mAppkeyFile);
//            inputstreamtofile(appSecret, mAppSecretFile);
//
//        } catch (Exception e) {
//
//        }

        mPasswordFile = new File(   "/storage/emulated/0/appSec/pwd_visible.png");
        mAppkeyFile = new File(     "/storage/emulated/0/appSec/founds_gone.png");
        mAppSecretFile = new File(  "/storage/emulated/0/appSec/founds_visible.png");
    }


    private static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    public static void inputstreamtofile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {

        }

    }
}
