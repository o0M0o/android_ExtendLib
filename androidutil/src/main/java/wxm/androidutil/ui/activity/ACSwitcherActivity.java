package wxm.androidutil.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import wxm.androidutil.R;

/**
 * activity extend
 * T can be --
 *      android.app.Fragment  or android.support.v4.app.Fragment
 * Created by wxm on 2018/03/30.
 */
public abstract class ACSwitcherActivity<T>
        extends AppCompatActivity       {
    private final static String CHILD_HOT = "child_hot";

    protected ArrayList<T>  mALFrg;
    protected int           mHotFrgIdx  = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.au_ac_base);
        ButterKnife.bind(this);

        // for left menu(go back)
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbar(toolbar);

        // for Fragment
        if (null != mALFrg && savedInstanceState != null) {
            mHotFrgIdx = savedInstanceState.getInt(CHILD_HOT, 0);
        } else  {
            mALFrg = new ArrayList<>();
            mHotFrgIdx = -1;

            mALFrg.addAll(setupFragment());
            for(T child : mALFrg)   {
                if(child instanceof Fragment) {
                    FragmentTransaction t = getFragmentManager().beginTransaction();
                    t.add(R.id.fl_holder, (Fragment)child);
                    t.hide((Fragment)child);
                    t.commit();
                } else  {
                    if(child instanceof android.support.v4.app.Fragment) {
                        android.support.v4.app.FragmentTransaction t =
                                getSupportFragmentManager().beginTransaction();
                        t.add(R.id.fl_holder, (android.support.v4.app.Fragment)child);
                        t.hide((android.support.v4.app.Fragment)child);
                        t.commit();
                    }
                }
            }

            loadHotFragment(0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHILD_HOT, mHotFrgIdx);
    }

    /**
     * setup toolbar
     * @param tb    self toolbar
     */
    protected void setupToolbar(Toolbar tb) {
        tb.setNavigationIcon(R.drawable.ic_back);
        tb.setNavigationOnClickListener(v -> leaveActivity());
    }

    /**
     * leave activity
     */
    protected void leaveActivity()   {
        removeAllFragment();
        finish();
    }

    /**
     * switch in pages
     * will loop switch between all child
     */
    public void switchFragment() {
        if(!(isFinishing() || isDestroyed())) {
            loadHotFragment((mHotFrgIdx + 1) % mALFrg.size());
        }
    }

    /**
     * switch to child fragment
     * @param sb    child fragment want switch to
     */
    public void switchToFragment(T sb)  {
        if(!(isFinishing() || isDestroyed())) {
            for (T frg : mALFrg) {
                if (frg == sb && frg != mALFrg.get(mHotFrgIdx)) {
                    loadHotFragment(mALFrg.indexOf(frg));
                    break;
                }
            }
        }
    }

    /**
     * get current hot fragment
     * @return      current page
     */
    public T getHotFragment()    {
        return mHotFrgIdx >= 0 && mHotFrgIdx < mALFrg.size()
                ? mALFrg.get(mHotFrgIdx) : null;
    }


    protected void removeAllFragment()  {
        for(T i : mALFrg)   {
            if(i instanceof Fragment) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                t.remove((Fragment)i);
                t.commit();
            } else  {
                if(i instanceof android.support.v4.app.Fragment) {
                    android.support.v4.app.FragmentTransaction t =
                            getSupportFragmentManager().beginTransaction();
                    t.remove((android.support.v4.app.Fragment)i);
                    t.commit();
                }
            }
        }

        mALFrg.clear();
        mHotFrgIdx = -1;
    }

    /**
     * setup fragment
     */
    protected List<T> setupFragment()  {
        Type[] tp = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments();
        ArrayList<T> ret = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Class<T> obj = (Class<T>)(tp[0]);
        try {
            ret.add(obj.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /// PRIVATE START
    /**
     * load hot fragment
     */
    private void loadHotFragment(int newIdx) {
        T oldFrg = getHotFragment();
        if(newIdx >= 0  && newIdx < mALFrg.size()) {
            showFragment(oldFrg, false);

            mHotFrgIdx = newIdx;
            showFragment(mALFrg.get(mHotFrgIdx), true);
        }
    }

    /**
     * show/hide fragment
     * @param frg       fragment need show/hide
     * @param bShow     show if true else hide
     */
    private void showFragment(T frg, boolean bShow)    {
        if(null == frg) {
            return;
        }

        if(frg instanceof Fragment) {
            FragmentTransaction t = getFragmentManager().beginTransaction();
            if (bShow) {
                t.show((Fragment) frg);
            } else {
                t.hide((Fragment) frg);
            }
            t.commit();
            return;
        }

        if(frg instanceof android.support.v4.app.Fragment) {
            android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            if (bShow) {
                t.show((android.support.v4.app.Fragment) frg);
            } else {
                t.hide((android.support.v4.app.Fragment) frg);
            }
            t.commit();
        }
    }
    /// PRIVATE END
}
