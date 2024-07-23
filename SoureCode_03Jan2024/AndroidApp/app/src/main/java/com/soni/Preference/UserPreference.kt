package com.soni.Preference

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.soni.services.web.models.StaticUrls
import com.soni.services.web.models.UserModel


var sharedPreferences: SharedPreferences? = null
var editor: SharedPreferences.Editor? = null

var sharedPreferencestoken: SharedPreferences? = null

val SHARED_PREF_NAME = "USER_DATA_PREFS"
val SHARED_PREF_FCM_TOKEN = "USER_DATA_PREFS_TOKEN"
val LOGIN_TOKEN = "LOGIN_TOKEN"
val USER_ID = "USER_ID"
val FCM_TOKEN = "fcmtoken"
val CURRENT_LANGUAGE_ID = "CURRENT_LANGUAGE_ID"
val USERDATA = "USERDATA"
val URLS = "URLS"
val LANGUAGESET = "LANGUAGESET"


fun Context.initPreferences() {
    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferencestoken = getSharedPreferences(SHARED_PREF_FCM_TOKEN, Context.MODE_PRIVATE)
}

fun storeLoginToken(mLOGIN_TOKEN: String) {
    editor = sharedPreferencestoken?.edit()
    editor?.putString(LOGIN_TOKEN, mLOGIN_TOKEN)
    editor?.commit()
}

fun getLoginToken(): String {
    return sharedPreferencestoken?.getString(LOGIN_TOKEN, "") ?: ""
}

fun storeUserID(mID: String) {
    editor = sharedPreferencestoken?.edit()
    editor?.putString(USER_ID, mID)
    editor?.commit()
}

fun getUserID(): String {
    return sharedPreferencestoken?.getString(USER_ID, "0") ?: "0"
}
fun storeFCMToken(fcm_token: String) {
    editor = sharedPreferencestoken?.edit()
    editor?.putString(FCM_TOKEN, fcm_token)
    editor?.commit()
}

fun getFcmToken(): String {
    return sharedPreferencestoken?.getString(FCM_TOKEN, "") ?: ""
}
fun storeCurrentLanguageID(mLanguage: String) {
    editor = sharedPreferences?.edit()
    editor?.putString(CURRENT_LANGUAGE_ID, mLanguage)
    editor?.commit()
}

fun getCurrentLanguageID(): String {
    return sharedPreferences?.getString(CURRENT_LANGUAGE_ID, "1") ?: "1"
}

fun StoreUserData(user: UserModel){
    editor = sharedPreferences?.edit()
    val gson = Gson()
    val json = gson.toJson(user)
    editor?.putString(USERDATA, json)
    editor?.commit()
}
fun getUserData():UserModel?{
    val gson = Gson()
    val json: String = sharedPreferences?.getString(USERDATA, "")?:""
    var user:UserModel? = gson.fromJson<UserModel>(json, UserModel::class.java)
    return user
}

fun StoreURLS(urls: StaticUrls){
    editor = sharedPreferences?.edit()
    val gson = Gson()
    val json = gson.toJson(urls)
    editor?.putString(URLS, json)
    editor?.commit()
}
fun getURLS():StaticUrls?{
    val gson = Gson()
    val json: String = sharedPreferences?.getString(URLS, "")?:""
    var user:StaticUrls? = gson.fromJson<StaticUrls>(json, StaticUrls::class.java)
    return user
}
fun storeLanguageSet(mLanguage: Boolean) {
    editor = sharedPreferences?.edit()
    editor?.putBoolean(LANGUAGESET, mLanguage)
    editor?.commit()
}

fun getLanguageSet(): Boolean {
    return sharedPreferences?.getBoolean(LANGUAGESET, false) ?: false
}