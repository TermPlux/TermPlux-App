package io.ecosed.libecosed;

interface ILicenseResultListener {
    oneway void verifyLicense(int responseCode, String signedData, String signature);
}