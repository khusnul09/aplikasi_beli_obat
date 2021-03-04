package com.example.mynotes;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Rupiah {

    public static DecimalFormat kursIndonesia;

    public  static String formatUangId(Context context, double uang){
        String FORMAT;
        kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        FORMAT = kursIndonesia.format(uang);
        return FORMAT;
    }
}
