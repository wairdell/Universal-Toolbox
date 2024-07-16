package com.sharp.ambition

import com.tencent.mmkv.MMKV
import kotlin.reflect.KProperty

/**
 *    author : fengqiao
 *    date   : 2023/10/19 14:04
 *    desc   :
 */
class MMKVIntDelegate(val mmkv: MMKV, val key: String, val defValue: Int)  {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Int {
        println("getValue() thisRef=$thisRef, property=${property.name}")
        return mmkv.getInt(key, defValue)
    }
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        println("setValue() thisRef=$thisRef, property=${property.name}, value=$value")
        mmkv.putInt(key, value)
    }
}

class MMKVStringDelegate(val mmkv: MMKV, val key: String, val defValue: String?) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): String? {
        println("getValue() thisRef=$thisRef, property=${property.name}")
        return mmkv.getString(key, defValue)
    }
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        println("setValue() thisRef=$thisRef, property=${property.name}, value=$value")
        mmkv.putString(key, value)
    }
}

fun MMKV.delegateInt(key: String, defValue: Int = 0): MMKVIntDelegate {
    return MMKVIntDelegate(this, key, defValue)
}

fun MMKV.delegateString(key: String, defValue: String? = null): MMKVStringDelegate {
    return MMKVStringDelegate(this, key, defValue)
}