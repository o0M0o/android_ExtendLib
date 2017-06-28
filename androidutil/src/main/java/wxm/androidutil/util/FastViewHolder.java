package wxm.androidutil.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder 辅助类
 * Created by ookoo on 2017/2/22.
 */
public class FastViewHolder {
    private Context mContext;
    private View mConvertView;

    //在viewholder中用来存放列表项中的多个控件的数组
    private SparseArray<View> viewList;

    private FastViewHolder(Context context, int layoutId, ViewGroup parentView) {
        viewList = new SparseArray<>();
        mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parentView , false);
        mConvertView.setTag(this);
    }

    private FastViewHolder(Context mContext, int layoutId) {
        viewList = new SparseArray<>();
        mConvertView = LayoutInflater.from(mContext).inflate(layoutId, null);
        mConvertView.setTag(this);

    }

    /**
     * 拿到一个viewholder对象
     * @param context           上下文
     * @param convertView       转换视图
     * @param layoutId          节点layout id
     * @return                  ViewHolder
     */
    public static FastViewHolder get(Context context, View convertView, int layoutId) {
        if (convertView == null) {
            return new FastViewHolder(context, layoutId);
        }

        return (FastViewHolder) convertView.getTag();
    }

    /**
     * 得到当前列表项的布局
     * @return          当前view
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 拿到列表项中的控件
     * @param viewId        id for view
     * @param <T>           view类类型
     * @return              view
     */
    public <T extends View> T getView(int viewId) {
        View childView = viewList.get(viewId);
        //还未绑定列表项中的控件到ViewHolder中
        if (childView == null) {
            childView = mConvertView.findViewById(viewId);
            viewList.put(viewId, childView);
        }

        return (T) childView;
    }


    /**
     * 设置文本
     * @param viewId    id for view
     * @param text      text for show
     * @return          ViewHolder后续操作可多次调用该方法（连续点）
     */
    public FastViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);

        return this;
    }


    /**
     * 设置textView的文本的颜色
     * @param viewId    id for view
     * @param color     color for show
     * @return          ViewHolder后续操作可多次调用该方法（连续点）
     */
    @TargetApi(Build.VERSION_CODES.M)
    public FastViewHolder setTextColor(int viewId, int color) {
        Resources res = mContext.getResources();
        TextView textView = getView(viewId);
        textView.setTextColor(res.getColor(color, mContext.getTheme()));

        return this;
    }


    /**
     * 设置imageView的图像
     * @param viewId        id for view
     * @param drawableId    drawable for view
     * @return              ViewHolder后续操作可多次调用该方法（连续点）
     */
    public FastViewHolder setImageResource(int viewId, int drawableId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(drawableId);

        return this;
    }


    /**
     * 设置imageView的图像
     * @param viewId        id for view
     * @param drawableId    id for drawable
     * @return              ViewHolder后续操作可多次调用该方法（连续点）
     */
    public FastViewHolder setImageBackground(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setBackgroundResource(drawableId);
        return this;
    }


    /**
     * 设置imageView的图像
     * @param viewId        id for view
     * @param bm            bitmap for view
     * @return              ViewHolder后续操作可多次调用该方法（连续点）
     */
    public FastViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }
}
