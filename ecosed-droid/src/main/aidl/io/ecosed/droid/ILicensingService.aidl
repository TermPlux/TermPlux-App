package io.ecosed.droid;

import io.ecosed.droid.ILicenseResultListener;
import io.ecosed.droid.ILicenseV2ResultListener;

interface ILicensingService {
    oneway void checkLicense(long nonce, String packageName, ILicenseResultListener listener);
    oneway void checkLicenseV2(String packageName, ILicenseV2ResultListener listener, in Bundle extraParams);
}