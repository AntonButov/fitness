package pro.butovanton.fitnes2.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Runtime;
import com.yanzhenjie.permission.source.ContextSource;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import pro.butovanton.fitnes2.R;

public class AndPermissionHelper {

    private static final String TAG = AndPermissionHelper.class.getSimpleName();

    public static void fileRequest(AppCompatActivity activity, AndPermissionHelperListener1 listener1) {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        HashMap<String, Integer> rationales = new HashMap<>(1);
        rationales.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.permission_dialog_rationale_storage);

        AndPermissionRequest
                .with(activity)
                .permission(permissions)
                .rationales(rationales)
                .listener1(listener1)
                .start();
    }

    public static void cameraRequest(AppCompatActivity activity, AndPermissionHelperListener1 listener1) {
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        HashMap<String, Integer> rationales = new HashMap<>(1);
        rationales.put(Manifest.permission.CAMERA, R.string.permission_dialog_rationale_camera);
        rationales.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, R.string.permission_dialog_rationale_storage);
        AndPermissionRequest
                .with(activity)
                .permission(permissions)
                .rationales(rationales)
                .listener1(listener1)
                .start();
    }

    public static void blePermissionRequest(AppCompatActivity activity, AndPermissionHelperListener1 listener1) {
        String[] permissions = new String[]{
                //LOCATION
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        HashMap<String, Integer> rationales = new HashMap<>();

        rationales.put(Manifest.permission.ACCESS_FINE_LOCATION, R.string.permission_dialog_rationale_location);
        AndPermissionRequest
                .with(activity)
                .permission(permissions)
                .rationales(rationales)
                .listener1(listener1)
                .start();
    }

    public interface AndPermissionHelperListener1 {
        void onSuccess();
    }

    /**
     * 只支持 Support v4 中的 FragmentActivity 和 Fragment
     */
    private static class AndPermissionRequest {

        public static AndPermissionRequest with(FragmentActivity activity) {
            return new AndPermissionRequest(activity);
        }

        public static AndPermissionRequest with(Fragment fragment) {
            return new AndPermissionRequest(fragment);
        }

        public static AndPermissionRequest with(Context context) {
            return new AndPermissionRequest(context);
        }

        private String[] mPermissions;
        private HashMap<String, Integer> mRationales;
        private AndPermissionHelperListener1 mListener1;

        private Runtime mAndPermission;
        private ContextSource mSource;
        private FragmentManager mFragmentManager;

        private AndPermissionRequest(FragmentActivity activity) {
            mAndPermission = AndPermission.with(activity).runtime();
            mSource = new ContextSource(activity);
            mFragmentManager = activity.getSupportFragmentManager();
        }

        private AndPermissionRequest(Fragment fragment) {
            mAndPermission = AndPermission.with(fragment).runtime();
           // mSource = new SupportFragmentSource(fragment);
            mFragmentManager = fragment.getFragmentManager();
        }

        private AndPermissionRequest(Context context) {
            mAndPermission = AndPermission.with(context).runtime();
            mSource = new ContextSource(context);
            mFragmentManager = null;
        }

        public AndPermissionRequest permission(String... permissions) {
            mPermissions = permissions;
            return this;
        }

        /**
         * 用于显示rationales信息
         *
         * @param rationales 权限为key，intres为value
         * @return
         */
        public AndPermissionRequest rationales(HashMap<String, Integer> rationales) {
            mRationales = rationales;
            return this;
        }


        public AndPermissionRequest listener1(AndPermissionHelperListener1 listener1) {
            mListener1 = listener1;
            return this;
        }

        public void start() {
            mAndPermission
                    .permission(mPermissions)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            checkResult();
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            checkResult();
                        }
                    })
                    .rationale(new Rationale<List<String>>() {
                        @Override
                        public void showRationale(Context context, List<String> data, RequestExecutor executor) {
                            showRationaleInner(executor);
                        }
                    })
                    .start();
        }

        private void showRationaleInner(final RequestExecutor executor) {
            if (mRationales == null || mRationales.size() <= 0 || mFragmentManager == null) return;
            String message = "";
            List<Integer> addedList = new ArrayList<>(mRationales.size());//同样的ResId只添加一次
            for (String permission : mPermissions) {
                boolean shouldRationale = mSource.isShowRationalePermission(permission);
                if (shouldRationale) {
                    Integer resId = mRationales.get(permission);
                    if (resId != null && resId != 0) {
                        boolean added = false;
                        for (int i = 0; i < addedList.size(); i++) {
                            Integer v = addedList.get(i);
                            if (v.intValue() == resId.intValue()) {
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            addedList.add(resId);
                            if (!TextUtils.isEmpty(message)) message += "\n";
                            message += (mSource.getContext().getString(resId));
                        }
                    }
                }
            }
            RationaleDialog dialog = RationaleDialog.newInstance(message);
            dialog.setPositiveListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    executor.execute();
                }
            });
            dialog.setNegativeListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    executor.cancel();
                }
            });
            dialog.show(mFragmentManager, null);
        }

        private void showSetting() {
            if (mFragmentManager == null) return;
            SettingDialog dialog = SettingDialog.newInstance();
            dialog.setPositiveListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAndPermission.setting().start();
                }
            });
            dialog.show(mFragmentManager, null);
        }

        private void checkResult() {
            List<String> deniedPermissions = checkPermissionAgain(mSource.getContext(), mPermissions);
            if (deniedPermissions == null || deniedPermissions.size() <= 0) {//success
                //Check listener and callback
                if (mListener1 != null) {
                    mListener1.onSuccess();
                }
            } else {//failed
                // 是否有不再提示并拒绝的权限。
                boolean hasAlwaysDeniedPermission = false;
                if (mSource.getContext() instanceof Activity) {
                    hasAlwaysDeniedPermission = AndPermission.hasAlwaysDeniedPermission((Activity) mSource.getContext(), deniedPermissions);
                }
                if (hasAlwaysDeniedPermission) {
                    showSetting();
                }
                //Check listener and callback
            }
        }


        /**
         * 检查这一组权限是否全部授权了
         *
         * @param context
         * @param permissions
         * @return 没有授权的权限
         */
        @SuppressLint("WrongConstant")
        @Nullable
        private static List<String> checkPermissionAgain(Context context, List<String> permissions) {
            if (context == null || permissions == null || permissions.size() <= 0) return null;
            List<String> deniedPermissions = new ArrayList<>(permissions.size());
            for (String permission : permissions) {
                if (TextUtils.isEmpty(permission)) continue;
                //BUG，部分手机检测权限会报错，加个异常捕获
                try {
                    if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return deniedPermissions;
        }

        @Nullable
        private static List<String> checkPermissionAgain(Context context, String[] permission) {
            if (permission == null || permission.length <= 0) return null;
            return checkPermissionAgain(context, Arrays.asList(permission));
        }

    }

    public static class RationaleDialog extends AppCompatDialogFragment {
        private static final String EXTRA_MESSAGE = "message";

        public static RationaleDialog newInstance(String message) {
            RationaleDialog dialog = new RationaleDialog();
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_MESSAGE, message);
            dialog.setArguments(bundle);
            return dialog;
        }

        public DialogInterface.OnClickListener mPositiveListener;
        public DialogInterface.OnClickListener mNegativeListener;

        public void setPositiveListener(DialogInterface.OnClickListener listener) {
            this.mPositiveListener = listener;
        }

        public void setNegativeListener(DialogInterface.OnClickListener listener) {
            this.mNegativeListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext())
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(getArguments().getString(EXTRA_MESSAGE))
                    .setPositiveButton(R.string.permission_dialog_rationale_sure, mPositiveListener)
                    .setNegativeButton(R.string.permission_dialog_rationale_cancel, mNegativeListener)
                    .create();
        }
    }

    public static class SettingDialog extends AppCompatDialogFragment {

        public static SettingDialog newInstance() {
            return new SettingDialog();
        }

        public DialogInterface.OnClickListener mPositiveListener;

        public void setPositiveListener(DialogInterface.OnClickListener listener) {
            this.mPositiveListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext())
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(R.string.permission_dialog_setting_message)
                    .setPositiveButton(R.string.permission_dialog_setting_sure, mPositiveListener)
                    .setNegativeButton(R.string.permission_dialog_setting_cancel, null)
                    .create();
        }
    }

    public static class Utils {

        public static String del2dot(String device) {
            return device.replace(":","");
        }

        public static String longDateToString(Long longDate) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("gmt"));
            String dateString = formatter.format(longDate);
            dateString = dateString.replace(" ", "T");
            return dateString;
        }

        /**
         * Calculate the distance based on the number of steps and the step size(km)
         *
         * @param step       number of steps
         * @param stepLength step size(m)
         * @return distance(km)
         */
        public static float step2Km(int step, float stepLength) {
            return (stepLength * step) / (1000);
        }

        /**
         * Calculate calories based on distance and weight(kCal)
         *
         * @param km     distance(km)
         * @param weight weight(kg)
         * @return calories(kCal)
         */
        public static float km2Calories(float km, float weight) {
            return 0.78f * weight * km;
        }

        /**
         * Calculate the step size based on height and gender(m)
         * @param height     height(cm)
         * @param man        gender，True for male, false for female
         * @return step size(m)
         */
        public static float getStepLength(float height,boolean man) {
            float stepLength = height * (man ? 0.415f : 0.413f);
            if (stepLength < 30) {
                stepLength = 30.f;//30cm，Default minimum step size 30cm
            }
            if (stepLength > 100) {
                stepLength = 100.f;//100cm，Default maximum step size 100cm
            }
            return stepLength / 100;
        }

        @ColorInt
        public static int getColor(Context context, @AttrRes int attr) {
            int[] attrsArray = new int[]{attr};
            TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
            int color = typedArray.getColor(0, 0);
            typedArray.recycle();
            return color;
        }

        /**
         * Get the start point of the hour
         */
        public static Date getHourStartTime(Calendar calendar, Date date) {
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }

        /**
         * Get the end point of the hour
         */
        public static Date getHourEndTime(Calendar calendar, Date date) {
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        }

        /**
         * Get day start time
         */
        public static Date getDayStartTime(Calendar calendar, Date date) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }

        /**
         * Get day end time
         */
        public static Date getDayEndTime(Calendar calendar, Date date) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        }

        public static Date getExpireLimitTime(Calendar calendar, int dayLimit) {
            Date date = new Date();
            date = getDayStartTime(calendar, date);
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - dayLimit);//设置时间到n天之前
            return calendar.getTime();
        }

        public static boolean isToday(Date date) {
            Date today = new Date();
            return date.getYear() == today.getYear()
                    && date.getMonth() == today.getMonth()
                    && date.getDate() == today.getDate();
        }

        public static int sp2px(Context context, float spValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        }

        // I added a generic return type to reduce the casting noise in client code
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }

        public static float roundDownFloat(double value, int scale) {
            return (float) round(String.valueOf(value), scale, BigDecimal.ROUND_DOWN);
        }

        private static double round(String value, int scale, int roundingMode) {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(scale, roundingMode);
            return bd.doubleValue();
        }

        public static String toMD5(@NonNull String inStr) {
            StringBuilder sb = new StringBuilder();
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(inStr.getBytes());
                byte b[] = md.digest();
                int i;
                for (byte aB : b) {
                    i = aB;
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        sb.append("0");
                    sb.append(Integer.toHexString(i));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return inStr;
        }

        /**
         * 获得当前目录的剩余容量，即可用大小
         *
         * @return 可用余量，单位为MB
         */
        public static double getAvailableSpace(@NonNull File file) {
            StatFs stat = null;
            try {
                stat = new StatFs(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stat == null) {
                return 0;
            }
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize / 1024.0f / 1024.0f;//MB
        }
    }
}
