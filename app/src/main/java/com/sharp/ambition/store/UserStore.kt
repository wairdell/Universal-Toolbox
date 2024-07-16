package com.sharp.ambition.store

import com.sharp.ambition.delegateInt
import com.sharp.ambition.delegateString
import com.tencent.mmkv.MMKV

/**
 *    author : fengqiao
 *    date   : 2023/10/19 14:18
 *    desc   :
 */
object UserStore {

    private val defaultMmkv by lazy { MMKV.defaultMMKV()  }

    private val userId by defaultMmkv.delegateInt("user_id", 0)

    private val userName by defaultMmkv.delegateString("user_name", "")

}