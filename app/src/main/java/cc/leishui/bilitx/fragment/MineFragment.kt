package cc.leishui.bilitx.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.LoginActivity
import cc.leishui.bilitx.bean.bean.User
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.*
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView


class MineFragment : Fragment() {
    private lateinit var tvUNme: TextView
    private lateinit var tvUStatus: TextView
    private lateinit var tvUDes: TextView
    private lateinit var tvServerUrl: TextView
    private lateinit var riUAvatar: RoundedImageView
    private lateinit var tvUSub: TextView
    private lateinit var tvUFollow: TextView
    private lateinit var tvUCoin: TextView
    private lateinit var btLogout: Button
    private lateinit var user: User
    private lateinit var llServerUrl:LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        initViewData()
        initListener()
    }

    private fun initListener() {
        llServerUrl.setOnClickListener {
            Utils.dialogEdit("输入服务器地址", SPUtils.getServerUrl(),context,object : Utils.AlertEditListener{
                override fun onNegative() {

                }

                @SuppressLint("SetTextI18n")
                override fun onPositive(content: String) {
                    Repo.changeServer(content,context)
                    tvServerUrl.text = "当前服务器地址：$content"
                }
            })
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initViewData() {
        tvServerUrl.text = "当前服务器地址：${SPUtils.getServerUrl()}"
        if (SPUtils.getIsLogin()) {
            btLogout.text = "注销"
            btLogout.setOnClickListener {
                SPUtils.saveIsLogin(false)
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                ActivityCompat.finishAffinity(requireActivity())
            }
            user = SPUtils.getUser()
            initUserInfo()
            refresh()
        } else {
            btLogout.text = "登录"
            btLogout.setOnClickListener { startActivity(Intent(context,LoginActivity::class.java)) }
            var status = ""
            tvUStatus.text = status
        }
    }

    private fun refresh() {
        Repo.getUserInfo(user.userId,{response ->
            run {
                if (response.body()?.status == StatusType.SUCCESSFUL) {
                    user = response.body()!!.content
                    SPUtils.saveUser(user)
                    initUserInfo()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initUserInfo() {
        tvUNme.text = user.user_name
        tvUSub.text = "关注：${user.subscription_count}"
        tvUFollow.text = "粉丝：${user.fan_count}"
        tvUCoin.text = "积分：${user.wallet}"
        tvUDes.text = user.des
        var status = ""
        tvUStatus.text = status
        Glide.with(requireContext()).load(user.avatar_url).placeholder(R.drawable.ic_gank)
            .error(R.drawable.ic_error).into(riUAvatar)
    }

    private fun initViews(view: View) {
        tvUNme = view.findViewById(R.id.tv_mine_user_name)
        tvUSub = view.findViewById(R.id.tv_mine_subscribe)
        tvUFollow = view.findViewById(R.id.tv_mine_follower)
        tvUCoin = view.findViewById(R.id.tv_mine_coin)
        riUAvatar = view.findViewById(R.id.ri_mine_avatar)
        tvUDes = view.findViewById(R.id.tv_mine_des)
        btLogout = view.findViewById(R.id.bt_mine_logout)
        tvUStatus = view.findViewById(R.id.tv_mine_status)
        llServerUrl = view.findViewById(R.id.f_mine_server_url)
        tvServerUrl = view.findViewById(R.id.f_mine_tv_server_url)
        //tvServerUrl.text=SPUtils.getServerUrl()
    }
}