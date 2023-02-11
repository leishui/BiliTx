package cc.leishui.bilitx.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cc.leishui.bilitx.R
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.SPUtils
import cc.leishui.bilitx.utils.Utils


class LoginActivity : AppCompatActivity() {
    private lateinit var etPhone: EditText
    private lateinit var etServer: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var ibClose: ImageButton
    private lateinit var tvRegister: TextView
    private lateinit var tvForgetPassword: TextView

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        initViews()
        initControllers()
    }

    private fun initControllers() {
        btLogin.setOnClickListener {
            when {
                etPhone.text.length != 11 -> Utils.toast("请输入11位正确手机号")
                etPassword.text.isBlank() -> Utils.toast("密码未填写")
                else -> {
                    SPUtils.saveServerUrl(etServer.text.toString())
                    alertDialog.show()
                    Repo.loginPhone(etPhone.text.toString().toLong(),
                        etPassword.text.toString(), { response ->
                            Log.d("TAG", "onResponse: " + response.body()?.msg.toString())
                            if (response.body()?.status == StatusType.SUCCESSFUL) {
                                Utils.toast("登录成功")
                                SPUtils.saveIsLogin(true)
                                SPUtils.saveUser(response.body()!!.content)
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                )
                                ActivityCompat.finishAffinity(this@LoginActivity)
                            } else {
                                Utils.toast(
                                    "登录失败：" + response.body()?.msg.toString()
                                )
                            }
                            alertDialog.dismiss()
                        }, {
                            Utils.toast("登录失败")
                            alertDialog.dismiss()
                        })
                }
            }

        }
        tvForgetPassword.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RetrievePasswordActivity::class.java
                )
            )
        }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        ibClose.setOnClickListener { finish() }
    }

    private fun initViews() {
        alertDialog = AlertDialog.Builder(this).setMessage("登录中...").setCancelable(false).create()
        etPhone = findViewById(R.id.activity_login_et_phone)
        etPassword = findViewById(R.id.activity_login_et_password)
        btLogin = findViewById(R.id.activity_login_bt_login)
        ibClose = findViewById(R.id.activity_login_ib_close)
        tvRegister = findViewById(R.id.activity_login_tv_register)
        tvForgetPassword = findViewById(R.id.activity_login_tv_forget_password)
        etServer = findViewById(R.id.activity_login_et_server_url)
        //不允许输入空格
        Utils.setEditTextInhibitInputSpace(etPassword)
        etServer.setText(SPUtils.getServerUrl())
    }
}