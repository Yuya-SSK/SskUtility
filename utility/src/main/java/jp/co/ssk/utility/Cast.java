package jp.co.ssk.utility;

import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public final class Cast {
    @SuppressWarnings("unchecked")
    public static <T> T auto(@Nullable Object object) {
        return (T) object;
    }
}
