package io.ecosed.libecosed;

interface ILicenseV2ResultListener {
    oneway void verifyLicense(int responseCode, in Bundle responsePayload);
}