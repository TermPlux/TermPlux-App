package io.ecosed.droid;

interface ILicenseResultListener {
    oneway void verifyLicense(int responseCode, String signedData, String signature);
}