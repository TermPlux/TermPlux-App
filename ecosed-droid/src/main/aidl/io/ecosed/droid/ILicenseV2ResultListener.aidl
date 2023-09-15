package io.ecosed.droid;

interface ILicenseV2ResultListener {
    oneway void verifyLicense(int responseCode, in Bundle responsePayload);
}