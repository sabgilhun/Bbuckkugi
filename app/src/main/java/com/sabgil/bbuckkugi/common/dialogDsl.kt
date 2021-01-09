package com.sabgil.bbuckkugi.common

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sabgil.bbuckkugi.databinding.WidgetCommonDialogBinding
import java.io.Serializable


class DialogScope : Serializable {

    var title: String = ""

    var content: String = ""

    var confirmButtonText: String = "확인"

    var cancelButtonText: String = "취소"

    var isVisibleCancelButton: Boolean = false

    var isCancelable: Boolean = false

    private var onConfirm: () -> Unit = {}

    private var onCancel: () -> Unit = {}

    fun onConfirm(block: () -> Unit) {
        onConfirm = block
    }

    fun onCancel(block: () -> Unit) {
        onCancel = block
    }

    fun build(context: Activity): Dialog {
        val binding = WidgetCommonDialogBinding.inflate(context.layoutInflater, null, false)

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        with(binding) {
            titleTextView.text = title
            contentTextView.text = content
            confirmButton.text = confirmButtonText
            cancelButton.text = cancelButtonText
            if (!isVisibleCancelButton) {
                cancelButton.visibility = View.GONE
            }
            confirmButton.setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }
            cancelButton.setOnClickListener {
                onCancel()
                dialog.dismiss()
            }
        }

        return dialog
    }
}

class CommonDialogFragment : DialogFragment() {

    private var _isCancelable: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogScope = requireNotNull(arguments?.get("dialogScope")) as DialogScope
        _isCancelable = dialogScope.isCancelable
        return dialogScope.build(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = _isCancelable
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {

        fun show(dialogScope: DialogScope, fragmentManager: FragmentManager) {
            CommonDialogFragment().apply {
                arguments = bundleOf("dialogScope" to dialogScope)
            }.show(fragmentManager, this::class.qualifiedName)
        }
    }
}