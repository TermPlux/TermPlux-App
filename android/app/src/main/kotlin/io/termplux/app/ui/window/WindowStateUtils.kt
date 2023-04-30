package io.termplux.app.ui.window

import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
fun isBookPosture(
    foldFeature: FoldingFeature?
): Boolean {
    contract {
        returns(value = true) implies (foldFeature != null)
    }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED
            && foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}

@OptIn(ExperimentalContracts::class)
fun isSeparating(foldFeature: FoldingFeature?): Boolean {
    contract {
        returns(value = true) implies (foldFeature != null)
    }
    return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}