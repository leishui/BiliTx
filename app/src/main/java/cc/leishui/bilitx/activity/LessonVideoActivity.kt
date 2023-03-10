package cc.leishui.bilitx.activity

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.LessonPageAdapter
import cc.leishui.bilitx.bean.bean.LessonSet
import cc.leishui.bilitx.constant.ResourceType
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.fragment.video.CommentFragment
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.SPUtils
import cc.leishui.bilitx.utils.Utils
import cc.leishui.bilitx.utils.Utils.getScreenWidth
import cc.leishui.bilitx.view.AppBarStateChangeListener
import cc.leishui.bilitx.view.LandLayoutVideo
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

class LessonVideoActivity : AppCompatActivity() {
    private var review: View? = null
    private var lessonId: Long = 0
    private lateinit var adapter: LessonPageAdapter
    private var isPlay = false
    private var isPause = false
    private var isSmall = false
    private var orientationUtils: OrientationUtils? = null
    private var detailPlayer: LandLayoutVideo? = null
    private var appBar: AppBarLayout? = null
    private var fab: FloatingActionButton? = null
    private var root: CoordinatorLayout? = null
    private var tb: TabLayout? = null
    private var vp: ViewPager? = null
    private var toolBarLayout: CollapsingToolbarLayout? = null
    private var curState: AppBarStateChangeListener.State? = null
    private lateinit var lessonSet: LessonSet
    private var isComment = false
    private var popupWindow: PopupWindow? = null
    private var popComment: PopupWindow? = null
    private lateinit var commentFragmentVLayout: CommentFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        review = LayoutInflater.from(this).inflate(
            R.layout.activity_lesson_video, null
        )
        setContentView(R.layout.activity_lesson_video)
        lessonSet = intent.getSerializableExtra("lessonSet") as LessonSet
        lessonId = lessonSet.lessons[0].lessonId
        initView()
        //????????????
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(lessonSet.cover_url)
            .placeholder(R.drawable.ic_gank)
            .error(R.drawable.ic_error).into(imageView)
        //imageView.setImageResource(R.mipmap.xxx1)
        resolveNormalVideoUI()

        //????????????????????????????????????
        orientationUtils = OrientationUtils(this, detailPlayer)
        //?????????????????????????????????
        orientationUtils!!.isEnable = false
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption.setThumbImageView(imageView)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
            .setUrl(lessonSet.lessons[0].resource_url)
            .setCacheWithPlay(false)
            .setVideoTitle(lessonSet.title)
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
                }

                override fun onClickResume(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickResume(url, *objects)
                    Log.d(TAG, "onClickResume: ")
                    setScroll(false)
                }

                override fun onClickResumeFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickResumeFullscreen(url, *objects)
                    Log.d(TAG, "onClickResumeFullscreen: ")
                    setScroll(false)
                }

                override fun onClickStopFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickStopFullscreen(url, *objects)
                    Log.d(TAG, "onClickStopFullscreen: ")
                    setScroll(true)
                }

                override fun onPrepared(
                    url: String,
                    vararg objects: Any
                ) {
                    Debuger.printfError("***** onPrepared **** " + objects[0])
                    Debuger.printfError("***** onPrepared **** " + objects[1])
                    super.onPrepared(url, *objects)
                    //????????????????????????????????????
                    orientationUtils!!.isEnable = detailPlayer!!.isRotateWithSystem
                    isPlay = true
                    root!!.removeView(fab)
                }

                override fun onEnterFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onEnterFullscreen(url, *objects)
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[1]) //????????????player
                }

                override fun onQuitFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //???????????????player
                    if (orientationUtils != null) {
                        orientationUtils!!.backToProtVideo()
                    }
                }
            })
            .setLockClickListener { _, lock ->
                if (orientationUtils != null) {
                    //???????????????onConfigurationChanged
                    orientationUtils!!.isEnable = !lock
                }
            }
            .setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
                Debuger.printfLog(
                    " progress $progress secProgress $secProgress currentPosition $currentPosition duration $duration"
                )
            }
            .build(detailPlayer)
        detailPlayer!!.fullscreenButton
            .setOnClickListener { //????????????
                orientationUtils!!.resolveByClick()

                //?????????true??????????????????actionbar????????????true??????????????????statusbar
                detailPlayer!!.startWindowFullscreen(this@LessonVideoActivity, true, true)
            }
        detailPlayer!!.setLinkScroll(true)
    }

    override fun onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        curPlay!!.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        curPlay!!.onVideoResume()
        //setScroll(false)
        appBar!!.addOnOffsetChangedListener(appBarStateChangeListener)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            curPlay!!.release()
        }
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    /**
     * orientationUtils ???  detailPlayer.onConfigurationChanged ????????????????????????????????????
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //????????????????????????
        if (isPlay && !isPause) {
            detailPlayer!!.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
        }
    }

    private fun initView() {
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar_lesson)
        detailPlayer = findViewById(R.id.detail_player_lesson)
        tb = findViewById(R.id.tl_video_scroll_lesson)
        vp = findViewById(R.id.vp_video_scroll_lesson)
        initContent()
        root = findViewById(R.id.root_layout_lesson)
        setSupportActionBar(toolbar)
        toolBarLayout = findViewById(R.id.toolbar_layout_lesson)
        toolBarLayout!!.setCollapsedTitleTextColor(Color.WHITE)
        toolBarLayout!!.setExpandedTitleColor(Color.TRANSPARENT)
        toolBarLayout!!.title = lessonSet.title
        fab = findViewById(R.id.fab_lesson)
        fab!!.setOnClickListener(View.OnClickListener {
            detailPlayer!!.startPlayLogic()
            root!!.removeView(fab)
        })
        appBar = findViewById(R.id.app_bar_lesson)
        val bvParams = detailPlayer!!.layoutParams
        val abParams = appBar!!.layoutParams
        val height = getScreenWidth() * 9 / 16
        bvParams.height = height
        abParams.height = height
        appBar!!.layoutParams = abParams
        detailPlayer!!.layoutParams = bvParams
        appBar!!.addOnOffsetChangedListener(appBarStateChangeListener)
    }

    private fun initContent() {
        commentFragmentVLayout = CommentFragment(
            "lessonSet.lessons[0].lessonId"
        )
//        adapter = LessonPageAdapter(
//            this,
//            supportFragmentManager,
//            IntroFragment(lessonSet.lessons[0]),
//            CatalogueFragment(lessonSet).also {
//                it.setCatalogueItemClick(object : CatalogueAdapter.OnCatalogueItemClick {
//                    override fun onClick(position: Int) {
//                        val lesson = lessonSet.lessons[position]
//                        detailPlayer?.setUp(lesson.resource_url, false, lesson.name)
//                        detailPlayer?.startPlayLogic()
//                        commentFragmentVLayout.initData(0)
//                        lessonId = lesson.lessonId
//                    }
//                })
//            },
//            commentFragmentVLayout
//        )
        initPopComment()
        initPopOpen()
        initVpAndTb()
    }

    private fun initPopComment() {
        popComment = PopupWindow(this)
        popComment?.apply {
            contentView =
                LayoutInflater.from(this@LessonVideoActivity).inflate(R.layout.popup_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            //animationStyle = R.style.PopupSchool
            isFocusable = true
            val et = contentView.findViewById<EditText>(R.id.et_comment_content)
            contentView.findViewById<Button>(R.id.bt_comment_send)
                .setOnClickListener {
                    popComment?.dismiss()
                    sendComment(
                        lessonId,
                        et.text.toString(),
                        SPUtils.getUser().userId
                    )
                    et.text.clear()
                }
        }
    }

    private fun sendComment(comment_id: Long, content: String, user_id: Long) {
        Repo.saveComment(
            comment_id,
            content,
            user_id,
            ResourceType.LESSON,
            {
                if (it.body()?.status == StatusType.SUCCESSFUL) {
                    Utils.toast("????????????")
                    commentFragmentVLayout.initData(0)
                    return@saveComment
                }
                Utils.toast("???????????????" + it.body()?.msg)
            }, { Utils.toast("????????????") })
    }

    private fun initVpAndTb() {
        vp!!.adapter = adapter
        tb!!.setupWithViewPager(vp)
        tb!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 2) {
                    isComment = true
                    popupWindow?.showAtLocation(
                        review,
                        Gravity.BOTTOM,
                        0,
                        0
                    )
                } else {
                    isComment = false
                    popupWindow?.dismiss()
                }
            }
        })
    }

    private fun initPopOpen() {
        popupWindow = PopupWindow(this)
        popupWindow?.apply {
            contentView =
                LayoutInflater.from(this@LessonVideoActivity)
                    .inflate(R.layout.popup_open_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            animationStyle = R.style.PopupSchool
            //isFocusable = true
            contentView.findViewById<TextView>(R.id.tv_open_comment)
                .setOnClickListener {
                    //popupWindow?.dismiss()
                    popComment?.showAtLocation(
                        review,
                        Gravity.BOTTOM,
                        0,
                        0
                    )
                    val edit =
                        popComment?.contentView?.findViewById<EditText>(R.id.et_comment_content)
                    edit?.requestFocus()
                    val imm =
                        edit?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
                }
        }
    }

    private fun resolveNormalVideoUI() {
        //??????title
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
                        //????????????
                        curState = state
                        //toolBarLayout.setTitle("");
                    }
                    State.COLLAPSED -> {
                        //????????????
                        //????????????????????????????????????
                        //toolBarLayout.setTitle("Title");
                        if (!isSmall && isPlay) {
                            isSmall = true
                            val size = CommonUtil.dip2px(
                                this@LessonVideoActivity,
                                150f
                            )
                            //detailPlayer.showSmallVideo(new Point(size, size), true, true);
                            orientationUtils!!.isEnable = false
                        }
                        curState = state
                    }
                    else -> {
                        if (curState == State.COLLAPSED) {
                            //???????????????????????????
                            //toolBarLayout.setTitle("");
                            if (isSmall) {
                                isSmall = false
                                orientationUtils!!.isEnable = true
                                //??????
                                //                        detailPlayer.postDelayed(new Runnable() {
                                //                            @Override
                                //                            public void run() {
                                //                                detailPlayer.hideSmallVideo();
                                //                            }
                                //                        }, 50);
                            }
                        }
                        curState = state
                        //????????????
                    }
                }
            }
        }

    private fun setScroll(bool: Boolean) {

        val layoutParams =
            toolBarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (bool) layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED else layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        toolBarLayout!!.layoutParams = layoutParams
        val vp_video_scroll_lesson = findViewById<ViewPager>(R.id.vp_video_scroll_lesson)
        vp_video_scroll_lesson.isClickable = false
//        val l = ll_fuck.layoutParams as CoordinatorLayout.LayoutParams
//        l.behavior = CoordinatorLayout
    }

    companion object {
        private const val TAG = "*****"
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP && isComment) {
            popupWindow?.showAtLocation(
                review,
                Gravity.BOTTOM,
                0,
                0
            )
        } else if (ev?.action == MotionEvent.ACTION_DOWN && isComment) popupWindow?.dismiss()
        Log.d(TAG, "dispatchTouchEvent: " + ev.toString())
        return super.dispatchTouchEvent(ev)
    }
}