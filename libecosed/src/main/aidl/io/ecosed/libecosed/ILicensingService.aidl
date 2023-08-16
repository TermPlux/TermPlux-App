package io.ecosed.libecosed;

import io.ecosed.libecosed.ILicenseResultListener;
import io.ecosed.libecosed.ILicenseV2ResultListener;

interface ILicensingService {
    oneway void checkLicense(long nonce, String packageName, ILicenseResultListener listener);
    oneway void checkLicenseV2(String packageName, ILicenseV2ResultListener listener, in Bundle extraParams);
}