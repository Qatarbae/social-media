package com.eltex.androidschool.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.eltex.androidschool.R
import com.eltex.androidschool.model.AttachmentType

class AttachmentTypeDialogFragment : DialogFragment() {

    private val choiceAttachmentItems = arrayOf(
        AttachmentType.IMAGE,
        AttachmentType.VIDEO,
        AttachmentType.AUDIO
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_type))
            .setItems(choiceAttachmentItems.map { it.toString() }
                .toTypedArray()) { _, which ->
                val mimeType = when (choiceAttachmentItems[which]) {
                    AttachmentType.IMAGE -> "image/*"
                    AttachmentType.VIDEO -> "video/*"
                    AttachmentType.AUDIO -> "audio/*"
                }

                setFragmentResult("requestKey", bundleOf("mimeType" to mimeType))
            }
            .create()
}