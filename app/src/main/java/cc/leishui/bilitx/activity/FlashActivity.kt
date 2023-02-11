package cc.leishui.bilitx.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.leishui.bilitx.R
import cc.leishui.bilitx.utils.SPUtils
import kotlin.concurrent.thread


class FlashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)
        init()
    }

    private fun init() {
        thread(start = true) {
            //Thread.sleep(2000)
            //PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
            if (SPUtils.getServerUrl() == "")
                SPUtils.saveServerUrl("https://yztx.leishui.cc/")
            if (!SPUtils.getIsPicker())
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
            else if (!SPUtils.getIsLogin())
                startActivity(Intent(this, LoginActivity::class.java))
            else startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
