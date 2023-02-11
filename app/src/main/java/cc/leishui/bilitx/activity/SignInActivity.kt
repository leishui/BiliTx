package cc.leishui.bilitx.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cc.leishui.bilitx.CodeCountDownTimer
import cc.leishui.bilitx.R
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.SPUtils
import cc.leishui.bilitx.utils.Utils


class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var countDownTimer: CodeCountDownTimer
    private lateinit var etPhone: EditText
    private lateinit var etVerification: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var ibClose: ImageButton
    private lateinit var tvLogin: TextView
    private lateinit var btRegister: Button
    private lateinit var btGetCode: Button

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        init()
    }

    private fun init() {
        initViews()
        countDownTimer = CodeCountDownTimer(btGetCode)
        initListener()
    }

    private fun initViews() {
        initAlertDialog()
        tvLogin = findViewById(R.id.activity_register_tv_login)
        etPhone = findViewById(R.id.activity_register_et_phone)
        etVerification = findViewById(R.id.activity_register_et_verification)
        etPassword = findViewById(R.id.activity_register_et_password)
        etRepeatPassword = findViewById(R.id.activity_register_et_repeat_password)
        ibClose = findViewById(R.id.activity_register_ib_close)
        btRegister = findViewById(R.id.activity_register_bt_register)
        btGetCode = findViewById(R.id.activity_register_bt_get_verification_code)
        //不允许输入空格
        Utils.setEditTextInhibitInputSpace(etPassword)
        Utils.setEditTextInhibitInputSpace(etRepeatPassword)
    }

    private fun initAlertDialog() {
        alertDialog = AlertDialog.Builder(this).setMessage("正在注册...").setCancelable(false).create()
    }

    private fun initListener() {
        btRegister.setOnClickListener(this)
        tvLogin.setOnClickListener(this)
        ibClose.setOnClickListener(this)
        btGetCode.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            btRegister.id -> {
                if (
                    etPhone.text.isNotBlank() &&
                    etVerification.text.isNotBlank() &&
                    etPassword.text.isNotBlank() &&
                    etRepeatPassword.text.isNotBlank()
                ) {
                    if (etPassword.text.toString() == etRepeatPassword.text.toString()) {
                        alertDialog.show()
                        Repo.signIn(
                            etPhone.text.toString().toLong(),
                            etPassword.text.toString(),
                            etVerification.text.toString().toInt(),
                            1,
                            1, { response ->
                                if (response.body()?.status == StatusType.SUCCESSFUL) {
                                    Utils.toast(
                                        "注册成功：" + response.body()?.msg.toString()
                                    )
                                    alertDialog.dismiss()
                                    SPUtils.saveIsLogin(true)
                                    SPUtils.saveUser(response.body()!!.content)
                                    startActivity(
                                        Intent(
                                            this@SignInActivity,
                                            MainActivity::class.java
                                        ).apply {
                                            flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK
                                        })
                                    ActivityCompat.finishAffinity(this@SignInActivity)
                                } else {
                                    Utils.toast(
                                        "注册失败：" + response.body()?.msg.toString()
                                    )
                                    alertDialog.dismiss()
                                }
                            }
                        )
                    } else
                        Utils.toast("密码不一致")
                } else if (etPhone.text.length != 11)
                    Utils.toast("请输入正确的11位手机号")
                else
                    Utils.toast("请将信息填写完整")
            }
            tvLogin.id -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            ibClose.id -> finish()
            btGetCode.id -> {
                if (etPhone.text.length != 11)
                    Utils.toast("请输入正确的11位手机号")
                else {
                    countDownTimer.start()
                    Repo.getCode(etPhone.text.toString().toLong(), {
                        if (it.body()?.status == StatusType.SUCCESSFUL) {
                            Utils.toast(
                                "获取成功：" + it.body()?.msg.toString()
                            )
                        } else {
                            Utils.toast(
                                "获取失败：" + it.body()?.msg.toString()
                            )
                            countDownTimer.cancel()
                            countDownTimer.onFinish()
                        }
                    }, {
                        Utils.toast("获取失败：网络请求错误")
                        countDownTimer.cancel()
                        countDownTimer.onFinish()
                    })
                }
            }
        }
    }
}