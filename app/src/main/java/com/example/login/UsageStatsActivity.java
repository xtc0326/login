package com.example.login;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.login.R;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;



public class UsageStatsActivity extends AppCompatActivity implements View.OnClickListener,Runnable {

    private static final String TAG = "UsageStatsActivity";
    private static final int REQUEST_USAGE_STATS = 1000;

    private Button bn_open_permission1;
    private TextView tv_content1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_stats);

        bn_open_permission1 = findViewById(R.id.bn_open_permission1);
        tv_content1 = findViewById(R.id.tv_content1);
        bn_open_permission1.setOnClickListener(this);

        /*if (AppUsageHelper.isStatsEnabled(this)) {
            refresh();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUsageHelper.isStatsEnabled(this)) {
            bn_open_permission1.setVisibility(View.GONE);
            tv_content1.setVisibility(View.VISIBLE);
            refresh();
        } else {
            bn_open_permission1.setVisibility(View.VISIBLE);
            tv_content1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        AppUsageHelper.requestStats(this, REQUEST_USAGE_STATS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_USAGE_STATS) {
            if (AppUsageHelper.isStatsEnabled(this)) {
                refresh();
            } else {
                Toast.makeText(this, "未开启权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void refresh() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        synchronized (MainActivity.class){
            final Map<String, Long> statsMap = AppUsageHelper.getStatsInfoOfToday(UsageStatsActivity.this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    if (statsMap != null) {
                        PackageManager pm = getPackageManager();
                        for (Map.Entry<String, Long> entry : statsMap.entrySet()) {
                            String label = "";
                            try {
                                ApplicationInfo info = pm.getApplicationInfo(entry.getKey(), 0);
                                label = pm.getApplicationLabel(info).toString();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            sb.append(label)
                                    .append("(").append(entry.getKey()).append(")")
                                    .append('\n')
                                    .append("\t\t").append(AppUsageHelper.getTimeLengthName(entry.getValue()))
                                    .append('\n');
                        }
                    }
                    tv_content1.setText(sb);
                }
            });
        }
    }

    /**
     * 应用统计类
     */
    public static class AppUsageHelper {

        private static final String TAG = "AppUsageHelper";

        private static final int ACTIVITY_STOPPED = 23;

        /**
         * 是否获取权限
         *
         * @param context
         * @return
         */
        public static boolean isStatsEnabled(Context context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true;
            }
            AppOpsManager aom = (AppOpsManager) context.getSystemService(APP_OPS_SERVICE);
            int mode = aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }

        /**
         * 获取权限
         *
         * @param context
         * @param requestCode
         */
        public static void requestStats(Activity context, int requestCode) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            try {
                context.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                intent.setData(null);
                context.startActivityForResult(intent, requestCode);
            }
        }

        /**
         * 获取所有应用使用时长
         *
         * @param context
         * @param beginTime
         * @param endTime
         * @return
         */
        public static Map<String, Long> getStatsInfo(Context context, long beginTime, long endTime) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                return null;
            }
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
            UsageEvents events = usm.queryEvents(beginTime, endTime);
            if (events == null) {
                return null;
            }

            //生成事件列表
            Map<String, List<UsageEvents.Event>> eventMap = new HashMap<>();
            while (events.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                events.getNextEvent(event);
                String packageName = event.getPackageName();
                List<UsageEvents.Event> eventList = eventMap.get(packageName);
                if (eventList == null) {
                    eventList = new ArrayList<>();
                    eventMap.put(packageName, eventList);
                }
                eventList.add(event);
            }
            //计算应用时长
            LinkedHashMap<String, Long> statsMap = new LinkedHashMap<>();
            for (Map.Entry<String, List<UsageEvents.Event>> entry : eventMap.entrySet()) {
                long totalTime = calcAppUsageTime(entry.getValue()); //计算
                if (totalTime > 0) statsMap.put(entry.getKey(), totalTime);
            }
            //删除自己
            statsMap.remove(context.getPackageName());
            //按时间排序
            List<Map.Entry<String, Long>> entryList = new ArrayList<>(statsMap.entrySet());
            Collections.sort(entryList, new Comparator<Map.Entry<String, Long>>() {
                @Override
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                    return (int) (o2.getValue() - o1.getValue());
                }
            });
            statsMap.clear();
            for (Map.Entry<String, Long> entry : entryList) {
                //Log.i(TAG, entry.getKey() + ", " + entry.getValue());
                statsMap.put(entry.getKey(), entry.getValue());
            }
            return statsMap;
        }

        /**
         * 获取单个应用使用时长
         * @param context
         * @param packageName
         * @param beginTime
         * @param endTime
         * @return
         */
        public static long getStatsInfo(Context context, String packageName, long beginTime, long endTime) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                return -1;
            }
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
            UsageEvents events = usm.queryEvents(beginTime, endTime);
            if (events == null) {
                return -1;
            }

            //生成事件列表
            List<UsageEvents.Event> eventList = new ArrayList<>();
            while (events.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                events.getNextEvent(event);
                if (packageName.equals(event.getPackageName())) {
                    eventList.add(event);
                }
            }
            //计算应用时长
            return calcAppUsageTime(eventList);
        }

        /**
         * 计算应用使用时长
         * @param eventList
         * @return
         */
        private static long calcAppUsageTime(List<UsageEvents.Event> eventList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startTimeOfDay = calendar.getTimeInMillis(); //今天0点

            Set<String> componentList = new HashSet<>();
            long resumeTime = 0;
            long totalTime = 0;
            boolean isFirstToBack = true;
            for (UsageEvents.Event event : eventList) {
                int eventType = event.getEventType();
                String className = event.getClassName();
                switch (eventType) {
                    case UsageEvents.Event.MOVE_TO_FOREGROUND:
                        if (resumeTime == 0) resumeTime = event.getTimeStamp();
                        componentList.add(className);
                        isFirstToBack = false;
                        break;
                    case UsageEvents.Event.MOVE_TO_BACKGROUND:
                    case ACTIVITY_STOPPED:
                        componentList.remove(className);
                        if (componentList.isEmpty()) {
                            if (resumeTime != 0) {
                                long usageTime = event.getTimeStamp() - resumeTime;
                                totalTime += usageTime;
                                resumeTime = 0;
                            } else {
                                if (eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                                    if (isFirstToBack) {
                                        long usageTime = event.getTimeStamp() - startTimeOfDay;
                                        totalTime += usageTime;
                                        isFirstToBack = false;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
            return totalTime;
        }


        /**
         * 获取当天的应用使用时长
         *
         * @param context
         * @return
         */
        public static Map<String, Long> getStatsInfoOfToday(Context context) {
            long now = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return getStatsInfo(context, calendar.getTimeInMillis(), now);
        }

        /**
         * 获取指定日期应用时长
         *
         * @param context
         * @param date 日期字串
         * @return
         */
        public static Map<String, Long> getStatsInfoOfDate(Context context, String date) {
            try {
                Date mDate = new SimpleDateFormat("yyyy-M-d", Locale.CHINESE).parse(date);
                Log.i(TAG, "" + mDate.getYear());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long beginTime = calendar.getTimeInMillis();
                calendar.add(Calendar.DATE, 1);
                long endTime = calendar.getTimeInMillis();
                long now = System.currentTimeMillis();
                return getStatsInfo(context, beginTime, endTime > now ? now : endTime);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String getTimeLengthName(long ms) {
            if (ms <= 0) {
                return "";
            }
            long secondTotal = ms / 1000;
            secondTotal = secondTotal == 0 ? 1 : secondTotal;
            long minuteTotal = secondTotal / 60;
            long hour = secondTotal / 3600;
            long minute = secondTotal % 3600 / 60;
            long second = secondTotal % 3600 % 60;
            StringBuilder sb = new StringBuilder();
            if (hour != 0) {
                sb.append(hour).append("小时");
            }
            if (minute != 0) {
                sb.append(minute).append(second == 0 ? "分钟" : "分");
            }
            if (second != 0) {
                sb.append(second).append("秒");
            }
            return sb.toString();
        }
    }
}




