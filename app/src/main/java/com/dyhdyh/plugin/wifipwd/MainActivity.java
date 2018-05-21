package com.dyhdyh.plugin.wifipwd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    ListView lv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        btn = findViewById(R.id.btn);

        clickRoot(null);
    }


    private void bindWifiListData(String configString) {
        final List<WiFiInfo> infoList = getWiFiInfoList(configString);
        if (infoList == null) {
            btn.setVisibility(View.VISIBLE);
            btn.setText("重新获取");
        } else {
            btn.setVisibility(View.GONE);
            lv.setAdapter(new WiFiInfoAdapter(infoList));
        }
    }

    private void toastRoot() {
        btn.setVisibility(View.VISIBLE);
        Toast.makeText(this, "需要获取Root权限才能查看密码", Toast.LENGTH_SHORT).show();
    }

    private List<WiFiInfo> getWiFiInfoList(String configString) {
        try {
            List<WiFiInfo> infos = new ArrayList<>();
            Matcher matcher = Pattern.compile("network=\\{([^\\}]+)\\}").matcher(configString);
            while (matcher.find()) {
                String group = matcher.group();
                Matcher ssidMatcher = Pattern.compile("ssid=\"([^\"]+)\"").matcher(group);
                if (ssidMatcher.find()) {
                    WiFiInfo info = new WiFiInfo();
                    info.setSsid(ssidMatcher.group(1));
                    Matcher pwdMatcher = Pattern.compile("psk=\"([^\"]+)\"").matcher(group);
                    if (pwdMatcher.find()) {
                        info.setPassword(pwdMatcher.group(1));
                    }
                    Matcher priorityMatcher = Pattern.compile("priority=[\\d]{1,4}").matcher(group);
                    try {
                        if (priorityMatcher.find()) {
                            String priorityText = priorityMatcher.group().replace("priority=", "");
                            info.setPriority(Integer.parseInt(priorityText));
                        }
                    } catch (NumberFormatException e) {

                    }
                    infos.add(info);
                }
            }
            Collections.sort(infos, new Comparator<WiFiInfo>() {
                @Override
                public int compare(WiFiInfo o1, WiFiInfo o2) {
                    if (o1.getPriority() == o2.getPriority()) {
                        return 0;
                    }
                    return o1.getPriority() > o2.getPriority() ? -1 : 1;
                }
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "获取失败", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void clickRoot(View view) {
        if (RootManager.isRoot()) {
            String command = "cat /data/misc/wifi/wpa_supplicant.conf";
            CommandInfo info = RootManager.execute(command);
            if (info.isRoot()) {
                bindWifiListData(info.getResult());
            } else {
                toastRoot();
            }
        } else {
            toastRoot();
        }
    }

    public void clickBack(View view) {
        finish();
    }
}
