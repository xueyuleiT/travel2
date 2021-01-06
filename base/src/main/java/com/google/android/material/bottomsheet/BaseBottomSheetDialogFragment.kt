package com.google.android.material.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.BaseDialog
import com.jtcxw.glcxw.base.R

abstract class  BaseBottomSheetDialogFragment : BaseDialog(){
    private var waitingForDismissAllowingStateLoss: Boolean = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!, theme)
    }

    override fun dismissAllowingStateLoss() {
        if (!tryDismissWithAnimation(true)) {
            super.dismissAllowingStateLoss()
        }
    }

    /**
     * Tries to dismiss the dialog fragment with the bottom sheet animation. Returns true if possible,
     * false otherwise.
     */
    private fun tryDismissWithAnimation(allowingStateLoss: Boolean): Boolean {
        val baseDialog = dialog
        if (baseDialog is BottomSheetDialog) {
            val behavior = baseDialog.behavior
            if (behavior.isHideable && baseDialog.dismissWithAnimation) {
                dismissWithAnimation(behavior, allowingStateLoss)
                return true
            }
        }

        return false
    }

    private fun dismissWithAnimation(
        behavior: BottomSheetBehavior<*>, allowingStateLoss: Boolean
    ) {
        waitingForDismissAllowingStateLoss = allowingStateLoss

        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            dismissAfterAnimation()
        } else {
            if (dialog is BottomSheetDialog) {
                (dialog as BottomSheetDialog).removeDefaultCallback()
            }
            behavior.addBottomSheetCallback(BottomSheetDismissCallback())
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private fun dismissAfterAnimation() {
        if (waitingForDismissAllowingStateLoss) {
            super.dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    private inner class BottomSheetDismissCallback : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAfterAnimation()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }


    override fun dismiss() {
        if (fragmentManager == null) {
            return
        }
        if (!tryDismissWithAnimation(false)) {
            super.dismiss()
        }
    }


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}