package com.dgsdev.bufunfatech.fragments.Authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.dgsdev.bufunfatech.databinding.FragmentUserSignUpBinding
import java.util.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import com.dgsdev.bufunfatech.R


class UserSignUp : Fragment() {
    lateinit var binding:FragmentUserSignUpBinding
    lateinit var client: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getActivity()?.getWindow()?.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.background))
        binding=  FragmentUserSignUpBinding.inflate(inflater, container, false)

        setUpSignUp()
        binding.googleSignUp.setOnClickListener{
            signIn()
        }
        return binding.root
    }

    private fun setUpSignUp() {
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (account!=null){
           goToNextPage()
        }
        googleCall()
    }
    fun googleCall(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        client = GoogleSignIn.getClient(requireActivity(), gso)
    }
    private fun signIn() {
        val signInIntent: Intent = client.signInIntent
        getResult.launch(signInIntent)
    }
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            goToNextPage()
            // if (it.resultCode == Activity.RESULT_OK) {
            //     goToNextPage()
            // }else{
            //     notifyUser("Verifique sua conexão de rede e tente novamente")
            // }
        }

    private fun notifyUser(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    private fun goToNextPage() {
        findNavController(requireActivity(),R.id.fragmentContainerView2)
            .navigate(R.id.goToUserDetails,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.userSignUp,
                        true).build()
            )
    }

/*
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
        }
    }

    private fun handle(){
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity())
        val credential =
            GoogleAccountCredential.usingOAuth2(requireContext(), Collections.singleton(Scopes.DRIVE_FILE))
        credential.selectedAccount = googleSignInAccount!!.account
        val googleDriveService = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName(getString(com.dgsdev.bufunfatech.R.string.app_name))
            .build()
        Thread {
            Download(googleDriveService)
        }.start()


    }

    private fun handleSignInIntent(data: Intent?) {

        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnSuccessListener {
                val credential: GoogleAccountCredential = GoogleAccountCredential
                    .usingOAuth2(requireActivity(), Collections.singleton(DriveScopes.DRIVE_FILE))
                credential.setSelectedAccount(it.account)
                val googleDriveDevices: Drive = Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential)
                    .setApplicationName("Bufunfatech")
                    .build()

                Thread(Runnable {
                     Download(googleDriveDevices)
                }).start()
            }
            .addOnFailureListener{

            }

    }

    private fun upload(googleDriveDevices:Drive){
        var storageFile:com.google.api.services.drive.model.File? = null
        storageFile?.setParents(Collections.singletonList("appDataFolder"))
        storageFile?.setName("Bufunfatech_Backup_Transaction")

        val filePath:java.io.File = java.io.File(dbPath)
        val mediaContent:FileContent = FileContent("",filePath)
        try {
            val file: com.google.api.services.drive.model.File? = googleDriveDevices.files().create(storageFile,mediaContent).execute();
            if (file != null) {
                Log.w("@@@","Filename: %s File ID: %s ${file.getName()}, ${file.getId()}")
            }
        }
        catch(e: UserRecoverableAuthIOException){
            Log.w("@@@","errorAuthIO:"+e.message.toString())
        }
        catch (e:Exception) {
            Log.w("@@@","error:"+e.message.toString())
        }
    }

    private fun Download(googleDriveService:Drive) {
        try {
            val dir = File("/data/data/com.dgsdev.bufunfatech/databases")
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    File(dir, children[i]).delete()
                }
            }
            val files: FileList = googleDriveService.files().list()
                .setSpaces("appDataFolder")
                .setFields("nextPageToken, files(id, name, createdTime)")
                .setPageSize(10)
                .execute()
            if (files.files.size == 0) Log.e("@@@", "No DB file exists in Drive")
            for (file in files.files) {
                Log.e("@@@", "Found file: ${file.name}, ${file.id}, ${file.createdTime}")
                if (file.name.equals("BufunfaTech_Backup_Transaction")) {
                    val outputStream: OutputStream = FileOutputStream(dbPath)
                    googleDriveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)
                }
            }
        } catch (e: IOException) {
            Log.w("@@@","error:"+e.message.toString())
        }
    }*/

}