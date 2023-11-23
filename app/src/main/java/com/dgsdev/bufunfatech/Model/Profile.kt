package com.dgsdev.bufunfatech.Model

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class Profile(context:Context){
    val mContext:Context = context
    val name = "Douglas P Ramos"//getData()?.displayName.toString()
    val profilePic = "https://avatars.githubusercontent.com/u/65875860?v=4"//getData()?.photoUrl
    val email = "douglas@bufunfatech.com"//getData()?.email
    val account = getData()

    fun getData(): GoogleSignInAccount? {
        val acct = GoogleSignIn.getLastSignedInAccount(mContext)
        return acct
    }

}