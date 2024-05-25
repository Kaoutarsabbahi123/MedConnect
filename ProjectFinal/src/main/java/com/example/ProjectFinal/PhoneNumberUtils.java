package com.example.ProjectFinal;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneNumberUtils {

    private static final String COUNTRY_CODE_MOROCCO = "MA"; // Code pays pour le Maroc

    public static String formatPhoneNumber(String phoneNumberStr) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumber = phoneNumberUtil.parse(phoneNumberStr, COUNTRY_CODE_MOROCCO);
            return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            System.err.println("Numéro de téléphone invalide: " + phoneNumberStr);
            return null;
        }
    }
}
