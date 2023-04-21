package io.termplux;

import android.os.Bundle;

interface IInAppBillingService {

    int isBillingSupported(int apiVersion, String packageName, String type) = 0;
    Bundle getSkuDetails(int apiVersion, String packageName, String type, in Bundle skusBundle) = 1;
    Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type, String developerPayload) = 2;
    Bundle getPurchases(int apiVersion, String packageName, String type, String continuationToken) = 3;
    int consumePurchase(int apiVersion, String packageName, String purchaseToken) = 4;
    Bundle getBuyIntentToReplaceSkus(int apiVersion, String packageName, in List<String> oldSkus, String newSku, String type, String developerPayload) = 6;
    Bundle getBuyIntentV6(int apiVersion, String packageName, String sku, String type, String developerPayload, in Bundle extras) = 7;
    Bundle getPurchasesV6(int apiVersion, String packageName, String type, String continuationToken, in Bundle extras) = 8;
    int isBillingSupportedV7(int apiVersion, String packageName, String type, in Bundle extras) = 9;
    Bundle getPurchasesV9(int apiVersion, String packageName, String type, String continuationToken, in Bundle extras) = 10;
    Bundle consumePurchaseV9(int apiVersion, String packageName, String purchaseToken, in Bundle extras) = 11;
    Bundle getPriceChangeConfirmationIntent(int apiVersion, String packageName, String sku, String type, in Bundle extras) = 800;
    Bundle getSkuDetailsV10(int apiVersion, String packageName, String type, in Bundle skuBundle, in Bundle extras) = 900;
    Bundle acknowledgePurchase(int apiVersion, String packageName, String purchaseToken, in Bundle extras) = 901;
}
