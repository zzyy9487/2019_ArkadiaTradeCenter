package com.example.shop

import android.content.Context

class SharedPreferences(context: Context) {
    val preference = context.getSharedPreferences("Day1", Context.MODE_PRIVATE)

    fun setResName(resname:String){
        preference.edit().putString("resname", resname).apply()
    }

    fun setResAccount(resaccount:String){
        preference.edit().putString("resaccount", resaccount).apply()
    }

    fun setResPssword(respassword:String){
        preference.edit().putString("respassword", respassword).apply()
    }

    fun setResStatus(resstatus:String){
        preference.edit().putString("resstatus", resstatus).apply()
    }

    fun setLoginName(loginName:String){
        preference.edit().putString("loginName", loginName).apply()
    }

    fun setToken(token:String){
        preference.edit().putString("token", token).apply()
    }

    fun setAccount(account:String){
        preference.edit().putString("account", account).apply()
    }

    fun setBalance(balance:String){
        preference.edit().putString("balancce", balance).apply()
    }

    fun setLevel(level:String){
        preference.edit().putString("level", level).apply()
    }

    fun setType(type:String){
        preference.edit().putString("type", type).apply()
    }

    fun setScore(score:String){
        preference.edit().putString("score", score).apply()
    }

    fun setId(id:String){
        preference.edit().putString("id", id).apply()
    }

    fun setScore2(score2:String){
        preference.edit().putString("score2", score2).apply()
    }

}