package cc.leishui.bilitx.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.VideoPageAdapter
import cc.leishui.bilitx.bean.biliBean.Rcmd.DataEntity.ItemEntity
import cc.leishui.bilitx.constant.ResourceType
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.fragment.video.CommentFragmentVLayout
import cc.leishui.bilitx.fragment.video.IntroFragment
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.network.bili.BiliRepo
import cc.leishui.bilitx.utils.Utils
import cc.leishui.bilitx.utils.Utils.getScreenWidth
import cc.leishui.bilitx.utils.Utils.serializable
import cc.leishui.bilitx.view.AppBarStateChangeListener
import cc.leishui.bilitx.view.LandLayoutVideo
import cc.leishui.bilitx.view.LandLayoutVideo.ShowOrHideListener
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class NormalVideoActivity : AppCompatActivity() {
    private var review: View? = null
    private lateinit var adapter: VideoPageAdapter
    private var isPlay = false
    private var isPause = false
    private var isSmall = false
    private var orientationUtils: OrientationUtils? = null
    private var detailPlayer: LandLayoutVideo? = null
    private var appBar: AppBarLayout? = null
    private var fab: FloatingActionButton? = null
    private var root: CoordinatorLayout? = null
    private var tl: TabLayout? = null
    private var tb: Toolbar? = null
    private var vp: ViewPager? = null
    private var toolBarLayout: CollapsingToolbarLayout? = null
    private var curState: AppBarStateChangeListener.State? = null
    private lateinit var lesson: ItemEntity
    private lateinit var commentFragmentVLayout: CommentFragmentVLayout

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (orientationUtils != null && GSYVideoManager.backFromWindowFull(this@NormalVideoActivity)) {
                        orientationUtils!!.backToProtVideo()
                        return
                    }
                    finish()
                }
            })
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        review = LayoutInflater.from(this).inflate(
            R.layout.activity_normal_video, null
        )
        setTheme(R.style.XUIPictureStyle)
        setContentView(R.layout.activity_normal_video)
        lesson = intent.serializable("lesson")!!
        initView()
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(lesson.pic)
            .placeholder(R.drawable.ic_gank)
            .error(R.drawable.ic_error).into(imageView)

        //imageView.setImageResource(R.mipmap.xxx1)
        resolveNormalVideoUI()

        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, detailPlayer)
        //初始化不打开外部的旋转
        orientationUtils?.isEnable = false
        orientationUtils?.isRotateWithSystem = false
        val hashMap = HashMap<String, String>()
        hashMap["referer"] = "https://www.bilibili.com/"
        hashMap["User-Agent"] = "NintendoSwitch"
        hashMap["Origin"] = "https://www.bilibili.com"
        hashMap["Accept"] = "*/*"
        hashMap["Connection"] = "keep-alive"
        hashMap["Accept-Encoding"] = "gzip, deflate, br"
        //Log.e(TAG, "onCreate: "+hashMap.values.toString())
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption.setThumbImageView(imageView)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
            .setCacheWithPlay(false)
            .setVideoTitle(lesson.title).setMapHeadData(hashMap)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onStartPrepared(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onStartPrepared(url, *objects)
                    Log.d(TAG, "onStartPrepared: ")
                    setScroll(false)
                }

                override fun onClickStop(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickStop(url, *objects)
                    Log.d(TAG, "onClickStop: ")
                    setScroll(true)
                    //orientationUtils!!.isEnable = false
                }

                override fun onClickResume(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickResume(url, *objects)
                    Log.d(TAG, "onClickResume: ")
                    setScroll(false)
                    //orientationUtils!!.isEnable = false
                }

                override fun onClickResumeFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickResumeFullscreen(url, *objects)
                    Log.d(TAG, "onClickResumeFullscreen: ")
                    setScroll(false)
                    //orientationUtils!!.isEnable = true
                }

                override fun onClickStopFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickStopFullscreen(url, *objects)
                    Log.d(TAG, "onClickStopFullscreen: ")
                    //orientationUtils!!.isEnable = false
                    setScroll(true)
                }

                override fun onPrepared(
                    url: String,
                    vararg objects: Any
                ) {
                    Debuger.printfError("***** onPrepared **** " + objects[0])
                    Debuger.printfError("***** onPrepared **** " + objects[1])
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    //orientationUtils!!.isEnable = detailPlayer!!.isRotateWithSystem
                    isPlay = true
                    root!!.removeView(fab)
                }

                override fun onEnterFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onEnterFullscreen(url, *objects)
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[1]) //当前全屏player
                    //orientationUtils!!.isEnable = true
                }

                override fun onQuitFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //当前非全屏player
                    orientationUtils?.backToProtVideo()
                    //orientationUtils!!.isEnable = false
                }

                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    setScroll(true)
                    //orientationUtils!!.isEnable = false
                    super.onAutoComplete(url, *objects)
                }
            })
            .setLockClickListener { _, lock ->
                //配合下方的onConfigurationChanged
                orientationUtils!!.isEnable = !lock
            }
            .setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
                Debuger.printfLog(
                    " progress $progress secProgress $secProgress currentPosition $currentPosition duration $duration"
                )
            }
        BiliRepo.getPlayUrl(lesson.bvid, lesson.cid, {
            gsyVideoOption.setUrl(it.data?.durl?.get(0)?.url ?: "").build(detailPlayer)
        })

        detailPlayer!!.fullscreenButton
            .setOnClickListener { //直接横屏
                orientationUtils?.resolveByClick()

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer?.startWindowFullscreen(this@NormalVideoActivity, true, true)
            }
        detailPlayer?.setLinkScroll(true)
        val videoTopBg = findViewById<View>(R.id.video_top_bg)
        detailPlayer?.setShowOrHideListener(object : ShowOrHideListener {
            override fun show() {
                if (!isPause) {
                    tb?.visibility = View.VISIBLE
                    videoTopBg?.visibility = View.VISIBLE
                }

            }

            override fun hide() {
                if (!isPause) {
                    tb?.visibility = View.INVISIBLE
                    videoTopBg?.visibility = View.INVISIBLE
                }
            }
        })
    }


    override fun onPause() {
        curPlay!!.onVideoPause()
        //初始化不打开外部的旋转
        //orientationUtils?.isEnable = false
        //orientationUtils?.isRotateWithSystem = false
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        //curPlay!!.onVideoResume()
        setScroll(true)
        //初始化不打开外部的旋转
        orientationUtils?.isEnable = false
        orientationUtils?.isRotateWithSystem = false
        //appBar!!.addOnOffsetChangedListener(appBarStateChangeListener)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            curPlay!!.release()
        }
        orientationUtils?.releaseListener()
    }

    private fun initView() {
        tb = findViewById(R.id.toolbar_video)
        detailPlayer = findViewById(R.id.detail_player)
        tl = findViewById(R.id.tl_video_scroll)
        vp = findViewById(R.id.vp_video_scroll)
        initContent()
        root = findViewById(R.id.root_layout)
        setSupportActionBar(tb)
        toolBarLayout = findViewById(R.id.toolbar_layout)
//        toolBarLayout?.title = " "
        tb?.title = ""
//        toolBarLayout!!.setCollapsedTitleTextColor(Color.WHITE)
//        toolBarLayout!!.setExpandedTitleColor(Color.TRANSPARENT)
//        toolBarLayout!!.title = lesson.name
        fab = findViewById(R.id.fab)
        fab!!.setOnClickListener {
            detailPlayer!!.startPlayLogic()
            root!!.removeView(fab)
        }
        appBar = findViewById(R.id.app_bar)
        val bvParams = detailPlayer!!.layoutParams
        val abParams = appBar!!.layoutParams
        val height = getScreenWidth() * 9 / 16 + Utils.getStatusBarHeight()
        bvParams.height = height
        abParams.height = height
        appBar!!.layoutParams = abParams
        detailPlayer!!.layoutParams = bvParams
        detailPlayer?.setPadding(0, Utils.getStatusBarHeight(), 0, 0)
        appBar!!.addOnOffsetChangedListener(appBarStateChangeListener)
    }

    fun setAccount(account: Int) {
        adapter.setTitle("评论($account)")
    }

    private fun initContent() {
        commentFragmentVLayout = CommentFragmentVLayout(
            lesson.bvid
        )
        adapter = VideoPageAdapter(
            this, supportFragmentManager, IntroFragment(lesson), commentFragmentVLayout
        )

        initVpAndTb()
    }


    private fun sendComment(comment_id: Long, content: String, user_id: Long) {
        Repo.saveComment(comment_id, content, user_id, ResourceType.LESSON, {
            if (it.body()?.status == StatusType.SUCCESSFUL) {
                Utils.toast("发送成功")
                commentFragmentVLayout.initData(0)
                return@saveComment
            }
            Utils.toast("发送失败：" + it.body()?.msg)
        }, { Utils.toast("发送失败") })
    }

    private fun initVpAndTb() {
        vp!!.adapter = adapter
        tl!!.setupWithViewPager(vp)
    }


    private fun resolveNormalVideoUI() {
        //增加title
        detailPlayer!!.titleTextView.visibility = View.GONE
        detailPlayer!!.backButton.visibility = View.GONE
    }

    private val curPlay: GSYVideoPlayer?
        get() = if (detailPlayer!!.fullWindowPlayer != null) {
            detailPlayer!!.fullWindowPlayer
        } else detailPlayer

    private var appBarStateChangeListener: AppBarStateChangeListener =
        object : AppBarStateChangeListener() {
            override fun onStateChanged(
                appBarLayout: AppBarLayout,
                state: State
            ) {
                println("--------------$state")
                when (state) {
                    State.EXPANDED -> {
                        //展开状态
                        curState = state
                        //toolBarLayout?.title = ""
                    }
                    State.COLLAPSED -> {
                        //折叠状态
                        //如果是小窗口就不需要处理
                        //toolBarLayout?.title = ""
                        //toolBarLayout.setTitle("Title");
                        if (!isSmall && isPlay) {
                            isSmall = true
                            val size = CommonUtil.dip2px(
                                this@NormalVideoActivity,
                                150f
                            )
                            //detailPlayer.showSmallVideo(new Point(size, size), true, true);
                            //orientationUtils!!.isEnable = false
                        }
                        curState = state
                    }
                    else -> {
                        if (curState == State.COLLAPSED) {
                            //由折叠变为中间状态
                            //toolBarLayout?.title = "";
                            if (isSmall) {
                                isSmall = false
                                //orientationUtils!!.isEnable = true
                                //必须
                                //                        detailPlayer.postDelayed(new Runnable() {
                                //                            @Override
                                //                            public void run() {
                                //                                detailPlayer.hideSmallVideo();
                                //                            }
                                //                        }, 50);
                            }
                        }
                        curState = state
                        //中间状态
                    }
                }
            }
        }

    private fun setScroll(bool: Boolean) {
        isPause = bool
        if (bool) {
            tb?.visibility = View.VISIBLE
        }
        val layoutParams =
            toolBarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (bool) layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED else layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        toolBarLayout!!.layoutParams = layoutParams
        findViewById<ViewPager>(R.id.vp_video_scroll).isClickable = false
//        val l = ll_fuck.layoutParams as CoordinatorLayout.LayoutParams
//        l.behavior = CoordinatorLayout
    }

    companion object {
        private const val TAG = "*****"
    }
}