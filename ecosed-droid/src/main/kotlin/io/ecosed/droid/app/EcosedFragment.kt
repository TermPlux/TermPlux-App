package io.ecosed.droid.app

import android.content.ContextWrapper
import androidx.fragment.app.Fragment

class EcosedFragment<YourFragment: Fragment> : ContextWrapper(null), EcosedFragmentImpl {


}