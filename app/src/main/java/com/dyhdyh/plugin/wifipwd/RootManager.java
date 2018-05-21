package com.dyhdyh.plugin.wifipwd;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author dengyuhan
 *         created 2018/2/7 18:37
 */
public class RootManager {

    public static boolean isRoot() {
        return execute(null).isRoot();
    }

    public static CommandInfo execute(String command) {
        CommandInfo info = new CommandInfo();
        boolean root = false;
        try {
            Process exec = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
            if (!TextUtils.isEmpty(command)) {
                dataOutputStream.writeBytes(command + "\n");
            }
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            //
            DataInputStream dataInputStream = new DataInputStream(exec.getInputStream());
            StringBuffer stringBuffer = new StringBuffer();
            Reader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
            }
            info.setResult(stringBuffer.toString());
            if (exec.waitFor() == 0) {
                root = true;
            }
            dataOutputStream.close();
            exec.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        info.setRoot(root);
        return info;
    }
}