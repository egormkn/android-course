package ru.ifmo.droid2016.okdemo.ok.api;

import android.support.v4.util.Pools;

/**
 * Created by dtrunin on 11.04.2015.
 */
class OkApiParam {

    String name;
    String value;

    @Override
    public String toString() {
        return name + "=" + value;
    }

    private static final Pools.Pool<OkApiParam> pool = new Pools.SynchronizedPool<>(100);

    private OkApiParam() {}

    static OkApiParam obtain() {
        OkApiParam param = pool.acquire();
        if (param == null) {
            param = new OkApiParam();
        }
        return param;
    }

    void recycle() {
        name = null;
        value = null;
        pool.release(this);
    }
}
