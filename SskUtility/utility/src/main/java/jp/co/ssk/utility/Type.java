package jp.co.ssk.utility;

import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public final class Type {
    @SuppressWarnings("unchecked")
    public static <T> T cast(@Nullable Object object) {
        return (T) object;
    }
}
