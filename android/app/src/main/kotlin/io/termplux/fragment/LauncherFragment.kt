package io.termplux.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.kongzue.baseframework.BaseApp
import io.termplux.R
import io.termplux.utils.ChineseCaleUtils
import io.termplux.databinding.LauncherBinding
import kotlin.math.hypot

class LauncherFragment constructor(
    appBar: AppBarLayout
) : Fragment(), Runnable {


    private val mAppBarLayout: AppBarLayout
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mContentLinear: LinearLayoutCompat

    private var _binding: LauncherBinding? = null
    private val binding get() = _binding!!

    init {
        mAppBarLayout = appBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // 加载XML布局
        _binding = LauncherBinding.inflate(inflater, container, false)
        // 屏闪动画LOGO
        mSplashLogo = AppCompatImageView(
            requireActivity()
        ).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_termplux_24
                )
            )
        }
        // 页面内容
        mContentLinear = LinearLayoutCompat(requireActivity()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                mAppBarLayout,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                binding.root,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            )
        }
        // 跟布局
        return  FrameLayout(
            requireActivity()
        ).apply {
            addView(
                mContentLinear,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                mSplashLogo,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContentLinear.visibility = View.INVISIBLE
        mContentLinear.post(this@LauncherFragment)

        binding.textViewCale.text = getDayLunar()
        binding.textViewCarrier.text = getCarrierName()

    }

    private fun getDayLunar(): String {
        return ChineseCaleUtils.getChineseCale()
    }

    private fun getCarrierName(): String {
        val telephony: TelephonyManager = requireActivity().getSystemService(
            BaseApp.TELEPHONY_SERVICE
        ) as TelephonyManager
        return telephony.simOperatorName
    }

    override fun run() {
        val cx = mSplashLogo.x + mSplashLogo.width / 2f
        val cy = mSplashLogo.y + mSplashLogo.height / 2f
        val startRadius = hypot(
            x = mSplashLogo.width.toFloat(),
            y = mSplashLogo.height.toFloat()
        )
        val endRadius = hypot(
            x = mContentLinear.width.toFloat(),
            y = mContentLinear.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mContentLinear,
                cx.toInt(),
                cy.toInt(),
                startRadius,
                endRadius
            )
            .setDuration(
                splashPart2AnimatorMillis.toLong()
            )
        mSplashLogo.animate()
            .alpha(0f)
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(
                splashPart1AnimatorMillis.toLong()
            )
            .withEndAction {
                mSplashLogo.visibility = View.GONE
            }
            .withStartAction {
                mContentLinear.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mContentLinear.removeCallbacks(this@LauncherFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {

        fun newInstance(
            appBar: AppBarLayout
        ): LauncherFragment {
            return LauncherFragment(
                appBar = appBar
            )
        }

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800
    }
}