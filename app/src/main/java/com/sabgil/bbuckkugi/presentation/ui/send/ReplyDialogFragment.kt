package com.sabgil.bbuckkugi.presentation.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.MessageCardList
import com.sabgil.bbuckkugi.databinding.FragmentReplyBinding
import com.sabgil.extra.extra

class ReplyDialogFragment : DialogFragment() {

    private val isAgreed: Boolean by extra()
    private val messageCardIndex: Int by extra()

    private lateinit var binding: FragmentReplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reply, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            handler = Handler()

            emojiTextView.text =
                if (isAgreed) "\uD83E\uDD73"
                else "\uD83D\uDE2D"

            titleTextView.text =
                if (isAgreed) "상대방이 수락했어요."
                else "상대방이 거절했어요."

            messageCardImageView.setImageResource(
                if (isAgreed) MessageCardList.acceptMessageCards[messageCardIndex]
                else MessageCardList.rejectMessageCard[messageCardIndex]
            )
        }
    }

    fun ifNotAddedShow(fragmentManager: FragmentManager) {
        val tag = this::class.simpleName
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            show(fragmentManager, tag)
        }
    }

    inner class Handler {

        fun activityFinish() = activity?.finish()
    }

    companion object {

        fun newInstance(isAgreed: Boolean, messageCardIndex: Int): ReplyDialogFragment {
            val fragment = ReplyDialogFragment()
            val bundle = bundleOf("isAgreed" to isAgreed, "messageCardIndex" to messageCardIndex)
            fragment.arguments = bundle
            return fragment
        }
    }
}